package pingis.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import pingis.utils.JavaSyntaxChecker;

/**
 *
 * @author authority
 */
@Controller
public class SyntaxCheckController {

  @MessageMapping("/javaparser/{taskInstanceId}")
  public String[] submissionSyntaxChecker(
          @DestinationVariable Long taskInstanceId, String submissionCode) {
    return JavaSyntaxChecker.parseCode(submissionCode);
  }
}
