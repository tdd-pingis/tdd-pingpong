package pingis.controllers;

import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @RequestMapping("/")
  public String index(Principal principal) {
    logger.debug("Request to /");

    if (principal != null) { // Check if SecurityContext is populated with authenticated principal
      return "redirect:/user";
    }
    return "index";
  }
}
