package pingis.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pingis.entities.Challenge;
import pingis.entities.CodeStatus;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.User;
import pingis.services.ChallengeService;
import pingis.services.TaskInstanceService;
import pingis.services.UserService;

@Profile("dev")
@Controller
public class UserDevController {
  
  @Autowired
  TaskInstanceService taskInstanceService;
  
  @Autowired
  ChallengeService challengeService;
  
  Logger logger = Logger.getLogger(UserDevController.class);

  @RequestMapping(value = "/user", method = RequestMethod.GET)
  public String user(Model model, Principal principal) {
    User user = userService.handleUserAuthenticationByName(principal.getName());
    
    MultiValueMap<Challenge, Task> tasksDoneWithinChallenge = new LinkedMultiValueMap<>();
    MultiValueMap<Challenge, TaskInstance> tasksInProgressWithinChallenge = new LinkedMultiValueMap<>();
    List<Challenge> newChallengesAvailable = new ArrayList<>();
    
    user.getTaskInstances().stream()
            .filter(e -> e.getStatus() == CodeStatus.DONE)
            .forEach(e -> tasksDoneWithinChallenge.add(e.getChallenge(), e.getTask()));
    
    user.getTaskInstances().stream()
            .filter(e -> e.getStatus() == CodeStatus.IN_PROGRESS)
            .forEach(e -> tasksInProgressWithinChallenge.add(e.getChallenge(), e));
    
    user.getTaskInstances().stream()
            .filter(e -> e.getStatus() == CodeStatus.IN_PROGRESS)
            .forEach(e -> tasksInProgressWithinChallenge.add(e.getChallenge(), e));
    
    model.addAttribute("tasksDoneWithinChallenge", tasksDoneWithinChallenge);
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
