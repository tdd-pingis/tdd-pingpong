package pingis.controllers;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import pingis.utils.JavaSyntaxChecker;
import pingis.utils.SyntaxError;

/**
 *
 * @author authority
 */
@Controller
class SyntaxCheckController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @MessageMapping("/javaparser/{taskInstanceId}")
  public List<SyntaxError> submissionSyntaxChecker(
          @DestinationVariable Long taskInstanceId, String submissionCode) {
    logger.debug("Javaparser received message");

    return JavaSyntaxChecker
        .getSyntaxErrors(submissionCode)
        .orElse(new ArrayList<>());
  }
}
