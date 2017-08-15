package pingis.controllers;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pingis.services.UserService;

@Controller
public class IndexController {
  
  @RequestMapping("/")
  public String index(Principal principal) {
    if (principal != null) { // Check if SecurityContext is populated with authenticated principal
      return "redirect:/user";
    }
    return "index";
  }
}
