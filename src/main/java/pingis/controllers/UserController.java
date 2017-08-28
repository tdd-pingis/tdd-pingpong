package pingis.controllers;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pingis.entities.Challenge;
import pingis.entities.CodeStatus;
import pingis.entities.TaskInstance;
import pingis.entities.User;
import pingis.services.ChallengeService;
import pingis.services.GameplayService;
import pingis.services.UserService;

@Controller
public class UserController {

  public enum LiveType {
    CONTINUE, CREATE, JOIN
  }
  
  @Autowired
  UserService userService;

  @Autowired
  ChallengeService challengeService;

  @Autowired
  GameplayService gameplayService;

  Logger logger = Logger.getLogger(UserController.class);


  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login(Model model) {
    logger.debug("Get /login");

    return "redirect:/oauth2/authorization/code/tmc";
  }

  @RequestMapping(value = "/user", method = RequestMethod.GET)
  public String user(Model model, Principal principal) {
    logger.debug("Get /user");

    User user = userService.handleUserAuthenticationByName(principal.getName());

    MultiValueMap<Challenge, TaskInstance> myTasksInChallenges = fetchAndSetStartedChallenges(user, 
                                                                                             model);
    fetchAndSetAvailableChallenges(myTasksInChallenges, model);
    fetchAndSetHistory(user, model);
    fetchAndSetLiveChallenge(user, model);
    fetchAndSetUnfinishedTaskInstance(user, model);
    
    model.addAttribute("userLevel", userService.levelOfCurrentUser());
    model.addAttribute("user", user);
    
    return "user";
  }

  private MultiValueMap<Challenge, TaskInstance> fetchAndSetStartedChallenges(User user, 
                                                                              Model model) {
                          
    MultiValueMap<Challenge, TaskInstance> myTasksInChallenges = new LinkedMultiValueMap<>();
    user.getTaskInstances().stream()
            .filter(e -> !e.getChallenge().getIsOpen())
            .filter(e -> e.getStatus().equals(CodeStatus.DONE))
            .forEach(e -> myTasksInChallenges.add(e.getChallenge(), e));
    model.addAttribute("myTasksInChallenges", myTasksInChallenges);
    return myTasksInChallenges;
  }

  private void fetchAndSetUnfinishedTaskInstance(User user, Model model) {
    List<TaskInstance> unfinishedTaskInstances = user.getTaskInstances().stream()
            .filter(e -> e.getStatus() == CodeStatus.IN_PROGRESS)
            .collect(Collectors.toList());
    
    TaskInstance lastUnfinished = null;
    if (!unfinishedTaskInstances.isEmpty()) {
      logger.info("Found " + unfinishedTaskInstances.size() + " unfinished task-instances.");
      // Get last taskInstance by timestamp
      lastUnfinished = unfinishedTaskInstances.stream()
              .max(new TaskInstanceTimestampComparator()).get();
    }
    
    if (lastUnfinished != null) {
      model.addAttribute("unfinishedTaskInstance", lastUnfinished);
      logger.info("Found latest unfinished task Instance '"
              + lastUnfinished.getTask().getName() + "'");
    } else {
      logger.info("No unfinished taskinstances found.");
    }
  }

  private void fetchAndSetLiveChallenge(User user, Model model) {
    Challenge liveChallenge = gameplayService.getParticipatingLiveChallenge();
    Challenge randomLiveChallenge = challengeService.getRandomLiveChallenge(user);

    LiveType liveType = null;
    if (liveChallenge == null && randomLiveChallenge == null) {
      logger.debug("Live challenge type = Create");
      model.addAttribute("liveChallengeType", LiveType.CREATE);
    } else if (liveChallenge == null) {
      logger.debug("Live challenge type = Join");
      liveChallenge = randomLiveChallenge;
      model.addAttribute("liveChallengeType", LiveType.JOIN);
    } else {
      logger.debug("Live challenge type = Continue");
      model.addAttribute("liveChallengeType", LiveType.CONTINUE);
    }
    model.addAttribute("liveChallenge", liveChallenge);
  }

  private void fetchAndSetHistory(User user, Model model) {
    List<TaskInstance> history = user.getTaskInstances().stream()
            .sorted(new TaskInstanceTimestampComparator())
            .collect(Collectors.toList());
    model.addAttribute("history", history);
    logger.info("Found " + history.size() + " done task-instances.");
  }

  private void fetchAndSetAvailableChallenges(MultiValueMap<Challenge, TaskInstance> 
                                              myTasksInChallenges, Model model) {
    List<Challenge> availableChallenges = challengeService.findAll().stream()
            .filter(e -> !e.getIsOpen())
            .filter(e -> e.getLevel() <= userService.levelOfCurrentUser())
            .filter(e -> !myTasksInChallenges.containsKey(e))
            .collect(Collectors.toList());
    
    model.addAttribute("availableChallenges", availableChallenges);
  }

  @RequestMapping(value = "/admin", method = RequestMethod.GET)
  public String admin(Model model) {
    logger.debug("Get /admin");

    return "admin";
  }

  public class TaskInstanceTimestampComparator implements Comparator<TaskInstance> {
    @Override
    public int compare(TaskInstance t1, TaskInstance t2) {
      if (t1.getCreationTime().after(t2.getCreationTime())) {
        return -1;
      } else {
        return 1;
      }
    }
  }

}

