package pingis.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        @RequestParam("notify") String notify) {

    logger.debug("TOKEN::::::{}", token);
    logger.debug("NOTIFY:::::{}", notify);

    //NOTE: Making STOMP messages persist makes this obsolete
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ex) {
      logger.error("Sandbox thread stopped unexpectedly");
    }

    fakeSandboxRestService.postSubmissionResults(token, notify);

    SubmissionResponse sr = new SubmissionResponse();
    sr.setStatus("ok");

    logger.debug("Returning response");
    return ResponseEntity.status(HttpStatus.OK).body(sr);
  }
}
