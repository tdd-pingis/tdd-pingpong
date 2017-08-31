package pingis.controllers;

import java.util.ArrayList;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.Realm;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskPair;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.services.ChallengeService;
import pingis.services.GameplayService;
import pingis.services.GameplayService.TurnType;
import pingis.services.TaskInstanceService;
import pingis.services.TaskService;
import pingis.services.UserService;
import pingis.utils.CodeStubBuilder;
import pingis.utils.TestStubBuilder;

@Controller
public class ChallengeController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  ChallengeService challengeService;
  @Autowired
  TaskService taskService;
  @Autowired
  TaskInstanceService taskInstanceService;
  @Autowired
  UserService userService;
  @Autowired
  GameplayService gameplayService;

  @RequestMapping(value = "/newchallenge")
  public String newChallenge(@ModelAttribute Challenge challenge) {

    return "newchallenge";
  }

  @RequestMapping("/startChallenge/{challengeId}")
  public String startChallenge(Model model, @PathVariable long challengeId) {
    logger.debug("Starting challenge");

    Challenge challenge = challengeService.findOne(challengeId);
    model.addAttribute("challenge", challenge);
    return "startchallenge";
  }

  @RequestMapping(value = "/createChallenge", method = RequestMethod.POST)
  public String createChallenge(@Valid @ModelAttribute Challenge newChallenge,
      BindingResult bindingResult, RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      return "newchallenge";
    }

    newChallenge = challengeService.createChallenge(newChallenge);
    logger.debug("Created new challenge with an ID: {}", newChallenge.getId());
    redirectAttributes.addFlashAttribute("challengeId", newChallenge.getId());
    return "redirect:/playChallenge/" + newChallenge.getId();
  }

  @RequestMapping(value = "/newtaskpair/{challengeId}", method = RequestMethod.GET)
  public String newTaskPair(@PathVariable Long challengeId, @ModelAttribute TaskPair taskpair,
      Model model) {
    logger.debug("Displaying new task pair -form.");
    logger.debug("Currentchallenge ID: {}",  challengeId);
    model.addAttribute("challenge", challengeService.findOne(challengeId));
    model.addAttribute("minLength", GameplayService.CHALLENGE_MIN_LENGTH);
    return "newtaskpair";
  }

  @RequestMapping(value = "/createTaskPair", method = RequestMethod.POST)
  public String createTaskPair(Long challengeId, @Valid @ModelAttribute TaskPair taskpair,
      BindingResult bindingResult, Model model) {

    Challenge currentChallenge = challengeService.findOne(challengeId);

    if (bindingResult.hasErrors()) {
      logger.info("New TaskPair -form had errors");
      model.addAttribute("challenge", currentChallenge);
      model.addAttribute("minLength", GameplayService.CHALLENGE_MIN_LENGTH);
      return "newtaskpair";
    }
    
    challengeService.createTaskPair(currentChallenge, taskpair);

    return "redirect:/playChallenge/" + currentChallenge.getId();
  }

  @RequestMapping("/playChallenge/{challengeId}")
  public RedirectView playChallenge(RedirectAttributes redirectAttributes,
      @PathVariable long challengeId) {
    logger.debug("Playing challenge " + challengeId);

    Challenge currentChallenge = challengeService.findOne(challengeId);
    if (currentChallenge == null) {
      redirectAttributes.addFlashAttribute("message", "challenge not found");
      return new RedirectView("/error");
    }
    User player = userService.getCurrentUser();
    TaskInstance unfinished =  taskInstanceService.getUnfinishedInstanceInChallenge(
                                            currentChallenge, player);
    if (unfinished != null) {
      logger.debug("Found unfinished instance. Redirecting to /task.");
      return new RedirectView("/task/" + unfinished.getId());
    }

    if (currentChallenge.getType() == ChallengeType.ARCADE) {
      return playArcade(redirectAttributes, currentChallenge);
    } else if (currentChallenge.getIsOpen()) {
      return playLive(currentChallenge, redirectAttributes);
    }
    return playPractice(redirectAttributes, currentChallenge);
  }


  @RequestMapping("/closeChallenge/{challengeId}")
  public RedirectView closeChallenge(@PathVariable Long challengeId,
      RedirectAttributes redirectAttributes) {
    logger.debug("Closing challenge");

    Challenge currentChallenge = challengeService.findOne(challengeId);
    if (!gameplayService.isParticipating(currentChallenge)) {
      logger.debug("User trying to close somebody else's challenge. Redirecting to /error.");
      redirectAttributes.addFlashAttribute("message", "user not in challenge");
      return new RedirectView("/error");
    }

    challengeService.closeChallenge(currentChallenge);
    redirectAttributes.addFlashAttribute("message", "Challenge closed.");

    return new RedirectView("/user");
  }


  @RequestMapping("/newArcadeSession")
  public RedirectView newArcadeSession(RedirectAttributes redirectAttributes,
      @RequestParam String realm) {
    logger.debug("Request to /newArcadeSession");

    User player = userService.getCurrentUser();
    Realm currentRealm = null;
    try {
      logger.debug("Trying to get realm: {}", realm);
      currentRealm = Realm.valueOf(realm.toUpperCase());
    } catch (Exception e) {
      logger.debug("Realm {} does not exist. Redirecting to /error.", realm);
      return new RedirectView("/error");
    }
    Challenge challenge = gameplayService.getArcadeChallenge(currentRealm);
    return playArcade(redirectAttributes, challenge);
  }

  public RedirectView playLive(Challenge currentChallenge,
      RedirectAttributes redirectAttributes) {
    logger.debug("Playing live, challenge ID " + currentChallenge.getId());

    int index = gameplayService.getNumberOfTasks(currentChallenge) / 2;
    logger.debug("Highest index of tasks in current challenge {}", index);

    if (!gameplayService.isParticipating(currentChallenge)) {
      logger.debug("Not participating.");

      if (currentChallenge.getSecondPlayer() == null) {
        currentChallenge.setSecondPlayer(userService.getCurrentUser());
        challengeService.save(currentChallenge);
        logger.debug("Current user saved as a participant"
            + " (second player) to current challenge.");
      } else {
        logger.debug("Current user not a player in this challenge. Redirecting to /error.");
        redirectAttributes.addFlashAttribute("message",
            "this is not your challenge");
        return new RedirectView("/error");
      }
    }

    TaskInstance unfinished = challengeService.getUnfinishedTaskInstance(currentChallenge);
    if (unfinished != null
        && !unfinished.getUser().equals(userService.getCurrentUser())) {
      logger.debug("Unfinished taskinstance found, but not owned by the"
          + " current user. Not user's turn yet, redirecting to \"/user\"");
      return new RedirectView("/user");
    }

    TurnType turn = gameplayService.getTurnType(currentChallenge);

    if (turn == TurnType.IMPLEMENTATION) {
      return playImplementationTurn(redirectAttributes, currentChallenge);
    } else if (turn == TurnType.TEST) {
      return playTestTurn(redirectAttributes, currentChallenge);
    } else {
      logger.debug("Not user's turn, redirecting to \"/user\"");
      return new RedirectView("/user");
    }
  }

  private RedirectView playImplementationTurn(RedirectAttributes redirectAttributes,
      Challenge currentChallenge) {
    logger.debug("Playing implementation turn");

    Task implTask = gameplayService.getTopmostImplementationTask(currentChallenge);
    logger.debug("implementation task: " + implTask.toString());
    Task testTask = gameplayService.getTopmostTestTask(currentChallenge);
    logger.debug("test task: " + testTask.toString());
    TaskInstance testTaskInstance =
        taskInstanceService.getByTaskAndUser(testTask, testTask.getAuthor());
    logger.debug("Found uneven number of completed taskinstances, "
        + "current user has turn, "
            + "creating new task instance, redirecting to /task.");
    return newTaskInstance(implTask, testTaskInstance, redirectAttributes);
  }

  private RedirectView playTestTurn(RedirectAttributes redirectAttributes,
      Challenge currentChallenge) {
    logger.debug("Playing test turn, challenge ID being " + currentChallenge.getId());
    logger.debug("Found even number of completed taskinstances, "
        + "current user has turn, redirecting to \"/newtaskpair\"");

    return new RedirectView("/newtaskpair/" + currentChallenge.getId());
  }

  private RedirectView playArcade(RedirectAttributes redirectAttributes, Challenge challenge) {
    logger.debug("Playing arcade");

    if (taskInstanceService.getNumberOfDoneTaskInstancesInChallenge(challenge) % 2 == 0) {
      logger.debug("User has test turn. Redirecting to /newtaskpair.");
      return new RedirectView("/newtaskpair/" + challenge.getId());

    } else {
      Task implTask = gameplayService.getRandomImplementationTask(challenge);
      Task testTask = taskService.getCorrespondingTask(implTask);
      TaskInstance testTaskInstance =
          taskInstanceService.getByTaskAndUser(testTask, testTask.getAuthor());
      logger.debug("User has implementation turn.");
      return newTaskInstance(implTask, testTaskInstance, redirectAttributes);
    }
  }

  private RedirectView playPractice(RedirectAttributes redirectAttributes, Challenge challenge) {
    logger.debug("Playing practice");

    // check for unfinished instances
    User player = userService.getCurrentUser();
    logger.debug("User: {}", player.getName());

    if (challengeService.isOwnChallenge(challenge, player)) {
      logger.debug("User trying to redo their own challenge");
      redirectAttributes.addFlashAttribute("message", "Cannot re-do your own live challenge");
      return new RedirectView("/error");
    }

    Task nextTask = taskService.nextPracticeTask(challenge);
    if (nextTask == null) {
      // User has completed the practice challenge.
      challenge.addNewCompletedPlayer(player);
      player.addCompletedChallenge(challenge);
      challengeService.save(challenge);
      userService.save(player);

      logger.debug("All tasks done");
      return new RedirectView("/challengeFinished/" + challenge.getId());
    }
    if (nextTask.getType() == TaskType.TEST) {
      logger.debug("Playing test task");
      return newTaskInstance(nextTask, null, redirectAttributes);
    }
    logger.debug("Playing implementation task");
    TaskInstance testTaskInstance =
        taskInstanceService.getRandomTaskInstance(taskService.getCorrespondingTask(nextTask));
    return newTaskInstance(nextTask, testTaskInstance, redirectAttributes);
  }

  public RedirectView newTaskInstance(Task task,
      TaskInstance testTaskInstance,
      RedirectAttributes redirectAttributes) {
    logger.debug("Creating new task instance");
    TaskInstance newTaskInstance = challengeService.newTaskInstance(task, testTaskInstance);
    return new RedirectView("/task/" + newTaskInstance.getId());
  }

  @RequestMapping("/challengeFinished/{challengeId}")
  public String challengeFinished(Model model, @PathVariable long challengeId) {
    logger.debug("Challenge finished");

    Challenge challenge = challengeService.findOne(challengeId);
    model.addAttribute("challenge", challenge);
    return "challengefinished";
  }

}

