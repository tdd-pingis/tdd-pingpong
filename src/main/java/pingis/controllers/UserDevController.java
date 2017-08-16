package pingis.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
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
import pingis.entities.ChallengeType;
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
    
    MultiValueMap<Challenge, TaskInstance> myTasksInChallenges = new LinkedMultiValueMap<>();
    MultiValueMap<Challenge, Task> myTasksInOpenChallenges = new LinkedMultiValueMap<>();
    
    user.getTaskInstances().stream()
            .filter(e -> !e.getChallenge().isOpen())
            .forEach(e -> myTasksInChallenges.add(e.getChallenge(), e));
    
    user.getTaskInstances().stream()
            .filter(e -> e.getChallenge().isOpen())
            .forEach(e -> myTasksInOpenChallenges.add(e.getChallenge(), e.getTask()));
    
    List<Challenge> availableChallenges = challengeService.findAll().stream()
            .filter(e -> !e.isOpen())
            .filter(e -> e.getLevel() <= user.getLevel())
            .filter(e -> !myTasksInChallenges.containsKey(e))
            .collect(Collectors.toList());
    
    model.addAttribute("availableChallenges", availableChallenges);
    model.addAttribute("tasksDoneWithinChallenge", myTasksInChallenges);
    model.addAttribute("tasksDoneWithinOpenChallenges", myTasksInOpenChallenges);
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
