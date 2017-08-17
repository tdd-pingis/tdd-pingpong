package pingis.controllers;

import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pingis.services.sandbox.SandboxService;

@Controller
public class TmcSubmissionController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private SandboxService sandboxService;

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
    int exitCodeValue = Integer.parseInt(exitCode.trim());

    boolean updated = sandboxService.updateSubmissionResult(submissionId, testOutput, stdout,
        stderr, validations, vmLog, status, exitCodeValue);

    if (!updated) {
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity(HttpStatus.OK);
  }
}
