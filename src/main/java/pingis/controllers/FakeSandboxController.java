package pingis.controllers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pingis.entities.sandbox.RequestedStatus;
import pingis.services.sandbox.FakeSandboxRestService;
import pingis.services.sandbox.SubmissionResponse;

/**
 *
 * @author authority
 */
@Controller
@Profile("dev")
public class FakeSandboxController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private FakeSandboxRestService fakeSandboxRestService;

  @RequestMapping("/tasks.json")
  public ResponseEntity<SubmissionResponse> tasks(
        @RequestParam("token") String token,
        @RequestParam("notify") String notify,
        @RequestParam("file") MultipartFile statusAsFile) throws IOException {

    logger.debug("Fake sandbox received request");

    RequestedStatus requestedStatus;
    try {
      requestedStatus = getRequestedStatusFromFile(statusAsFile);
    } catch (IllegalArgumentException ex) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //NOTE: Making STOMP messages persist makes this obsolete
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ex) {
      logger.error("Sandbox thread stopped unexpectedly: {}", ex.getMessage());
    }

    fakeSandboxRestService.postSubmissionResults(token, notify, requestedStatus);

    SubmissionResponse sr = new SubmissionResponse();
    sr.setStatus("ok");

    logger.debug("Returning response");
    return ResponseEntity.status(HttpStatus.OK).body(sr);
  }

  private RequestedStatus getRequestedStatusFromFile(
          MultipartFile statusAsFile) throws IOException, IllegalArgumentException {

    try {
      byte[] bytes = statusAsFile.getBytes();
      String statusName = new String(bytes);
      return RequestedStatus.valueOf(statusName);

    } catch (IllegalArgumentException ex) {
      logger.debug("The file should be a value of RequestedStatus");
      throw ex;
    }
  }
}
