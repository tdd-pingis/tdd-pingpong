package pingis.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
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
import pingis.entities.sandbox.Logs;
import pingis.entities.sandbox.ResultMessage;
import pingis.entities.sandbox.ResultStatus;
import pingis.entities.sandbox.Submission;
import pingis.entities.sandbox.SubmissionStatus;
import pingis.entities.sandbox.TestOutput;
import pingis.repositories.sandbox.SubmissionRepository;
import pingis.services.TaskInstanceService;

@Controller
public class TmcSubmissionController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private SubmissionRepository submissionRepository;
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
    logger.debug("TOKEN::::" + token);
    Submission submission = submissionRepository.findOne(submissionId);

    if (submission == null) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    } else if (submission.getStatus() != SubmissionStatus.PENDING) {
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
    
    TaskType type = submission.getTaskInstance().getTask().getType();
    new Thread(() -> {
      try {
        Thread.sleep(6000);
        sendResults(submission, type);
      } catch (InterruptedException ex) {
        java.util.logging.Logger.getLogger(TmcSubmissionController.class.getName()).log(Level.SEVERE, null, ex);
      }
      
    }).start();

    return new ResponseEntity(HttpStatus.OK);
  }

  private void sendResults(Submission submission, TaskType type) {
    ResultMessage message = createMessage(submission, type);
    if (message.isSuccess()) {
      taskInstanceService.markAsDone(submission.getTaskInstance());
    }
    //Broadcasts the submission to /topic/results
    this.template.convertAndSend("/topic/results/" + submission.getId(), message);
    logger.debug("Sent the TMC sandbox results to /topic/results");
  }

  private ResultMessage createMessage(Submission submission, TaskType type) {
    TestOutput top = submission.getTestOutput();
    Logs logs = top.getLogs();
    ResultStatus status = top.getStatus();

    ResultMessage message = new ResultMessage();
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
