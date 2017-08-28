package pingis.controllers;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.CodeStatus;
import pingis.entities.Realm;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
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
public class LiveChallengeController {

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
  public String newChallenge(Model model) {
    logger.debug("Request to /newchallenge");

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
  public RedirectView createChallenge(String challengeName, String challengeDesc,
      String challengeType, String realm, RedirectAttributes redirectAttributes) {
    logger.debug("Creating challenge");

    Challenge newChallenge = new Challenge(challengeName,
        userService.getCurrentUser(),
        challengeDesc, ChallengeType.valueOf(challengeType));
    newChallenge.setLevel(1);
    newChallenge.setOpen(true);
    newChallenge = challengeService.save(newChallenge);
    logger.debug("Created new challenge: {}", newChallenge.toString());
    return new RedirectView("/playChallenge/" + newChallenge.getId());
  }

  @RequestMapping(value = "/newtaskpair")
  public String newTaskPair(Model model) {
    // TODO: add checks
    logger.debug("Displaying new task pair -form.");
    return "newtaskpair";
  }

  @RequestMapping(value = "/createTaskPair", method = RequestMethod.POST)
  public RedirectView createTaskPair(String testTaskName, String implementationTaskName,
      String testTaskDesc, String implementationTaskDesc,
      String testCodeStub, String implementationCodeStub,
      long challengeId,
      RedirectAttributes redirectAttributes) {
    // TODO: check turns and stuff
    logger.debug("Creating new task pair");

    Challenge currentChallenge = challengeService.findOne(challengeId);
    int highestIndex = taskService.findAllByChallenge(currentChallenge).size() / 2;
    logger.info("Challenge type: " + currentChallenge.getType());
    logger.info("Highest index: " + highestIndex);
    User player = userService.getCurrentUser();
    User otherPlayer = currentChallenge.getAuthor().equals(player) ? currentChallenge.getSecondPlayer() : currentChallenge.getAuthor();
    String testStub = "";
    String implStub = "";

    // Autogenerate code stubs
    if (currentChallenge.getType() == ChallengeType.MIXED
        || currentChallenge.getType() == ChallengeType.ARCADE
        || (currentChallenge.getType() == ChallengeType.PROJECT
        && highestIndex == 0 )) {
      logger.info("generating code stubs");
      implStub = new CodeStubBuilder(currentChallenge.getName()).build().code;
      testStub = new TestStubBuilder(implStub).withTestImports().build().code;
    } else {
      // Challenge is a project with at least one existing task instance pair. Inheriting code from previous instance pair.
      logger.info("inheriting code stubs from previous task pair");
      testStub = taskInstanceService.getByTaskAndUser(
          taskService.findByChallengeAndTypeAndIndex(currentChallenge, TaskType.TEST, highestIndex),
          otherPlayer)
          .getCode();
      implStub = taskInstanceService.getByTaskAndUser(
          taskService.findByChallengeAndTypeAndIndex(currentChallenge, TaskType.IMPLEMENTATION, highestIndex),
          player)
          .getCode();
    }

    logger.debug("Generating new task pair and instance");
    gameplayService.generateTaskPairAndTaskInstance(testTaskName,
        implementationTaskName,
        testTaskDesc,
        implementationTaskDesc,
        testStub,
        implStub,
        currentChallenge);
    return playChallenge(redirectAttributes, challengeId);
  }

  @RequestMapping("/playChallenge/{challengeId}")
  public RedirectView playChallenge(RedirectAttributes redirectAttributes,
      @PathVariable long challengeId) {
    logger.debug("Playing challenge");

    Challenge currentChallenge = challengeService.findOne(challengeId);
    if (currentChallenge == null) {
      redirectAttributes.addFlashAttribute("message", "challenge not found");
      return new RedirectView("/error");
    }
    User player = userService.getCurrentUser();
    TaskInstance unfinished =  taskInstanceService.getUnfinishedInstance(currentChallenge, player);
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

    currentChallenge.setOpen(false);
    challengeService.save(currentChallenge);
    redirectAttributes.addFlashAttribute("message", "Challenge closed.");
    logger.debug("Closed challenge {}", currentChallenge.getId());
    return new RedirectView("/user");
  }

  @RequestMapping("/newArcadeChallenge")
  public String newArcadeChallenge(Model model) {
    logger.debug("Request to /newArcadeChallenge");
    return "newarcadechallenge";
  }

  @RequestMapping(value = "/createArcadeChallenge", method = RequestMethod.POST)
  public RedirectView createArcadeChallenge(RedirectAttributes redirectAttributes,
      String challengeName,
      String challengeDesc,
      String realm,
      int level) {
    logger.debug("Creating arcade challenge");

    Challenge arcadeChallenge = new Challenge(
        challengeName,
        userService.getCurrentUser(),
        challengeDesc,
        ChallengeType.ARCADE);

    try {
      arcadeChallenge.setRealm(Realm.valueOf(realm.toUpperCase()));
    } catch (Exception e) {
      logger.debug("Realm {} does not exist. Redirecting to /error.", realm);
      return new RedirectView("/error");
    }
    arcadeChallenge.setOpen(true);
    arcadeChallenge.setLevel(level);
    challengeService.save(arcadeChallenge);
    logger.debug("Created new arcade challenge {}", arcadeChallenge.toString());
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
    logger.debug("Playing live");

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
    logger.debug("Playing test turn");

    redirectAttributes.addFlashAttribute("challengeId", currentChallenge.getId());
    redirectAttributes.addFlashAttribute("challenge", currentChallenge);
    redirectAttributes.addFlashAttribute("minLength", GameplayService.CHALLENGE_MIN_LENGTH);
    logger.debug("Found even number of completed taskinstances, "
        + "current user has turn, redirecting to \"/newtaskpair\"");

    return new RedirectView("/newtaskpair");
  }

  private RedirectView playArcade(RedirectAttributes redirectAttributes, Challenge challenge) {
    logger.debug("Playing arcade");

    if (taskInstanceService.getNumberOfDoneTaskInstancesInChallenge(challenge) % 2 == 0) {
      redirectAttributes.addFlashAttribute("challengeId", challenge.getId());
      redirectAttributes.addFlashAttribute("challenge", challenge);
      redirectAttributes.addFlashAttribute("minLength", Integer.MAX_VALUE);
      logger.debug("User has test turn. Redirecting to /newtaskpair.");
      return new RedirectView("/newtaskpair");

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
      // User has completed the practice challenge. TODO: Award points?
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

    User user = userService.getCurrentUser();
    TaskInstance newTaskInstance = taskInstanceService.createEmpty(user, task);
    if (task.getType() == TaskType.IMPLEMENTATION) {
      logger.debug("Task type is implementation");
      newTaskInstance.setTestTaskInstance(testTaskInstance);
      testTaskInstance.addImplementationTaskInstance(newTaskInstance);
      taskInstanceService.save(newTaskInstance);
      taskInstanceService.save(testTaskInstance);
    }
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

