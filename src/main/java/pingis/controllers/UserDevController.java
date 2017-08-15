package pingis.controllers;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pingis.entities.User;
import pingis.services.TaskInstanceService;
import pingis.services.UserService;

@Profile("dev")
@Controller
public class UserDevController {
  
  @Autowired
  TaskInstanceService taskInstanceService;

  @RequestMapping(value = "/user", method = RequestMethod.GET)
  public String user(Model model, Principal principal) {
    User user = userService.handleUserAuthenticationByName(principal.getName());
    
    model.addAttribute("tasksDone", taskInstanceService.getByUserAndTask(user, challenge).
    model.addAttribute("user", user);
    return "user";
  }
  
  @Autowired
  UserService userService;

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login(Model model) {
    return "login";
  }

  @RequestMapping(value = "/admin", method = RequestMethod.GET)
  public String admin(Model model) {
    return "admin";
  }
}
