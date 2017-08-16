package pingis.services.sandbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.compress.archivers.ArchiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.sandbox.Logs;
import pingis.entities.sandbox.ResultMessage;
import pingis.entities.sandbox.ResultStatus;
import pingis.entities.sandbox.Submission;
import pingis.entities.sandbox.SubmissionStatus;
import pingis.entities.sandbox.TestOutput;
import pingis.repositories.sandbox.SubmissionRepository;
import pingis.services.TaskInstanceService;

@Service
public class SandboxService {

  private final Logger logger = LoggerFactory.getLogger(SandboxService.class);

  @Autowired
  private SubmissionSenderService senderService;

  @Autowired
  private SubmissionPackagingService packagingService;

  @Autowired
  private SubmissionRepository submissionRepository;

  @Autowired
  private TaskInstanceService taskInstanceService;

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  // TODO: Think of a better return type
  public boolean updateSubmissionResult(UUID submissionId, String testOutput, String stdout,
      String stderr, String validations, String vmLog, String status, int exitCode)
      throws IOException {
    Submission submission = submissionRepository.findOne(submissionId);

    if (submission == null || submission.getStatus() != SubmissionStatus.PENDING) {
      logger.error("Submission {} exists, ignoring duplicate update", submissionId);
      return false;
    }

    logger.debug("Updating submission {} result", submissionId);

    TestOutput top = new ObjectMapper().readValue(testOutput, TestOutput.class);

    submission.setExitCode(exitCode);
    submission.setStatus(status);
    submission.setStderr(stderr);
    submission.setStdout(stdout);
    submission.setTestOutput(top);
    submission.setValidations(validations);
    submission.setVmLog(vmLog);

    submissionRepository.save(submission);

    sendResults(submission);

    return true;
  }

  private void sendResults(Submission submission) {
    ResultMessage message = createMessage(submission);

    if (message.isSuccess()) {
      taskInstanceService.markAsDone(submission.getTaskInstance());
    }

    String destination = "/topic/results/" + submission.getId();

    // Send the submission to /topic/results/{id}
    messagingTemplate.convertAndSend(destination, message);

    logger.debug("Sent the TMC sandbox results to {}", destination);
  }

  private ResultMessage createMessage(Submission submission) {
    TestOutput top = submission.getTestOutput();
    Logs logs = top.getLogs();
    ResultStatus status = top.getStatus();

    ResultMessage message = new ResultMessage();
    TaskType type = submission.getTaskInstance().getTask().getType();

    message.setType(type);
    message.setStatus(status);

    if (status == ResultStatus.COMPILE_FAILED) {
      message.setStdout(logs.getStdoutString());
    } else if (status == ResultStatus.PASSED) {
      message.setSuccess(type == TaskType.IMPLEMENTATION);
      message.setTests(
          top.getTestResults().stream()
              .filter(r -> r.isPassed()).collect(Collectors.toList()));

    } else if (status == ResultStatus.TESTS_FAILED) {
      message.setSuccess(type == TaskType.TEST);
      message.setTests(
          top.getTestResults().stream()
              .filter(r -> !r.isPassed()).collect(Collectors.toList()));
    }

    return message;
  }

  public Submission submit(Map<String, byte[]> files, TaskInstance taskInstance)
      throws IOException, ArchiveException {
    logger.debug("Packaging submission files...");
    byte[] content = packagingService.packageSubmission(files);

    Submission submission = new Submission();
    submission.setId(UUID.randomUUID());
    submission.setStatus(SubmissionStatus.PENDING);
    submission.setTaskInstance(taskInstance);

    logger.debug("Created submission, id {}", submission.getId());

    submissionRepository.save(submission);

    senderService
        .sendSubmission(submission.getId(), content)
        .exceptionally(exception -> {
          logger.error("Sending submission {} failed: {}", submission.getId(), exception);
          return null;
        })
        .thenAccept(response -> {
          if (response == null || !response.getStatus().equals(SubmissionResponse.OK)) {
            submission.setStatus(SubmissionStatus.SEND_FAILED);
          }

          submissionRepository.save(submission);
        });

    return submission;
  }
}
