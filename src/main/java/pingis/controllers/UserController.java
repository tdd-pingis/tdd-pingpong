package pingis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pingis.entities.TmcUserDto;
import pingis.services.ChallengeService;
import pingis.services.UserService;

@Controller
public class UserController {

  @Autowired
  UserService userService;

  @Autowired
  ChallengeService challengeService;

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login(Model model) {
    return "redirect:/oauth2/authorization/code/tmc";
  }

  @RequestMapping(value = "/user", method = RequestMethod.GET)
  public String user(Model model, @AuthenticationPrincipal TmcUserDto user) {
    userService.handleOAuthUserAuthentication(user);
    model.addAttribute("randomChallenge", challengeService.getRandomChallenge());
    return "user";
  }

  @RequestMapping(value = "/admin", method = RequestMethod.GET)
  public String admin(Model model) {
    return "admin";
  }
}
