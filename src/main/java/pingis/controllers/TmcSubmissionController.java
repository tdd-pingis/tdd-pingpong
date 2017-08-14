package pingis.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pingis.entities.TaskType;
import pingis.entities.tmc.Logs;
import pingis.entities.tmc.ResultMessage;
import pingis.entities.tmc.ResultStatus;
import pingis.entities.tmc.TestOutput;
import pingis.entities.tmc.TmcSubmission;
import pingis.entities.tmc.TmcSubmissionStatus;
import pingis.repositories.TmcSubmissionRepository;
import pingis.services.TaskInstanceService;

@Controller
public class TmcSubmissionController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private TmcSubmissionRepository submissionRepository;
  @Autowired
  private SimpMessagingTemplate template;
  @Autowired
  private TaskInstanceService taskInstanceService;

  // These request parameters are specified separately because there doesn't seem to
  // be a simple way to rename fields when doing data binding.
  @PostMapping("/submission-result")
  public ResponseEntity submissionResult(
      @RequestParam("test_output") String testOutput,
      @RequestParam String stdout,
      @RequestParam String stderr,
      @RequestParam String validations,
      @RequestParam("vm_log") String vmLog,
      @RequestParam String token,
      @RequestParam String status,
      @RequestParam("exit_code") String exitCode) throws IOException {
    logger.debug("Received a response from TMC sandbox");
    UUID submissionId = UUID.fromString(token);
    TmcSubmission submission = submissionRepository.findOne(submissionId);

    if (submission == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    } else if (submission.getStatus() != TmcSubmissionStatus.PENDING) {
      // Result is being submitted twice.
      // TODO: decide on a better response code for this...
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    int exitCodeValue = Integer.parseInt(exitCode.trim());
    ObjectMapper mapper = new ObjectMapper();
    submission.setExitCode(exitCodeValue);
    submission.setStatus(status);
    submission.setStderr(stderr);
    submission.setStdout(stdout);
    submission.setTestOutput(mapper.readValue(testOutput, TestOutput.class));
    submission.setValidations(validations);
    submission.setVmLog(vmLog);
    submissionRepository.save(submission);
    sendResults(submission);
    return new ResponseEntity(HttpStatus.OK);
  }

  private void sendResults(TmcSubmission submission) {
    ResultMessage message = createMessage(submission);
    if (message.isSuccess()) {
      taskInstanceService.markAsDone(submission.getTaskInstance());
    }
    //Broadcasts the submission to /topic/results
    this.template.convertAndSend("/topic/results/" + submission.getId(), message);
    logger.debug("Sent the TMC sandbox results to /topic/results");
  }

  private ResultMessage createMessage(TmcSubmission submission) {
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
}
