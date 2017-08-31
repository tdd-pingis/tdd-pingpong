package pingis.controllers;

import java.security.Principal;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pingis.entities.Challenge;
import pingis.entities.TaskInstance;
import pingis.entities.User;
import pingis.services.entity.ChallengeService;
import pingis.services.entity.TaskInstanceService;
import pingis.services.entity.UserService;
import pingis.services.logic.DashboardService;
import pingis.services.logic.GameplayService;


@Controller
public class UserController {

  public enum LiveType {
    CONTINUE, CREATE, JOIN
  }

  @Autowired
  DashboardService dashboardService;
  @Autowired
  UserService userService;
  @Autowired
  TaskInstanceService taskInstanceService;
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
    model.addAttribute("userLevel", userService.levelOfCurrentUser());
    model.addAttribute("user", user);

    MultiValueMap<Challenge, TaskInstance> myTasksInChallenges
            = userService
        .getCompletedTaskInstancesInUnfinishedChallenges();
    TaskInstance lastUnfinished = userService
        .getLastUnfinishedInstance(taskInstanceService);
    List<TaskInstance> history = userService.getHistory(taskInstanceService);
    List<Challenge> availableChallenges = dashboardService.getAvailableChallenges(
            myTasksInChallenges);

    model.addAttribute("myTasksInChallenges", myTasksInChallenges);
    model.addAttribute("unfinishedTaskInstance", lastUnfinished);
    model.addAttribute("history", history);
    model.addAttribute("availableChallenges", availableChallenges);

    logger.info("Found " + history.size() + " completed task-instances.");
    logger.info("Found " + user.getCompletedChallenges().size() + " completed challenges.");
    if (lastUnfinished != null) {
      logger.info("Found latest unfinished taskinstance of task "
                            + lastUnfinished.getTask().getName());
    } else {
      logger.info("No unfinished taskinstances");
    }

    // Fetch and set live challenge
    Challenge liveChallenge = dashboardService
        .getParticipatingLiveChallenge();
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

    return "user";
  }

  @RequestMapping(value = "/admin", method = RequestMethod.GET)
  public String admin(Model model) {
    logger.debug("Get /admin");

    return "admin";
  }



}

