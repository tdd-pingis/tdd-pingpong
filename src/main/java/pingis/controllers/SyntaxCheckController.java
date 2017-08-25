package pingis.controllers;

import java.util.ArrayList;
import java.util.List;
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
public class SyntaxCheckController {

  @MessageMapping("/javaparser/{taskInstanceId}")
  public List<SyntaxError> submissionSyntaxChecker(
          @DestinationVariable Long taskInstanceId, String submissionCode) {
    return JavaSyntaxChecker
        .getSyntaxErrors(submissionCode)
        .orElse(new ArrayList<>());
  }
}
