package pingis.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.services.ChallengeService;
import pingis.services.GameplayService;
import pingis.services.TaskInstanceService;
import pingis.services.TaskService;
import pingis.services.UserService;

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

    return "newchallenge";
  }

  @RequestMapping(value = "/createChallenge", method = RequestMethod.POST)
  public RedirectView createChallenge(String challengeName, String challengeDesc,
      String challengeType, String realm, RedirectAttributes redirectAttributes) {

    Challenge newChallenge = new Challenge(challengeName,
        userService.getCurrentUser(),
        challengeDesc, challengeType == "PROJECT" ? ChallengeType.PROJECT : ChallengeType.MIXED);
    newChallenge.setLevel(1);
    newChallenge.setOpen(true);
    newChallenge = challengeService.save(newChallenge);
    logger.info(newChallenge.toString());
    return new RedirectView("/playTurn/" + newChallenge.getId());
  }

  @RequestMapping(value = "/newtaskpair")
  public String newTaskPair(Model model) {
    return "newtaskpair";
  }

  @RequestMapping(value = "/createTaskPair", method = RequestMethod.POST)
  public RedirectView createTaskPair(String testTaskName, String implementationTaskName,
      String testTaskDesc, String implementationTaskDesc,
      String testCodeStub, String implementationCodeStub,
      long challengeId,
      RedirectAttributes redirectAttributes) {
    
//    String[] errors = JavaSyntaxChecker.checkTaskPairErrors(implementationCodeStub, testCodeStub);
//    if (errors != null) {
//      redirectAttributes.addFlashAttribute("errors", errors);
//      redirectAttributes.addAttribute("challengeId", challengeId);
//      
//      return new RedirectView("/createTaskPair");
//    }
    
    // create and save new tasks, redirect to createtaskinstance
    Challenge currentChallenge = challengeService.findOne(challengeId);
    int numberOfTasks = gameplayService.getNumberOfTasks(currentChallenge);
    int nextIndex = numberOfTasks / 2 + 1;
    User currentUser = userService.getCurrentUser();
    Task testTask = new Task(nextIndex,
        TaskType.TEST,
        currentUser,
        testTaskName,
        testTaskDesc,
        testCodeStub,
        1, 1);
    Task implTask = new Task(nextIndex,
        TaskType.IMPLEMENTATION,
        currentUser,
        implementationTaskName,
        implementationTaskDesc,
        implementationCodeStub,
        1, 1);
    currentChallenge.addTask(testTask);
    currentChallenge.addTask(implTask);
    testTask.setChallenge(currentChallenge);
    implTask.setChallenge(currentChallenge);
    taskService.save(testTask);
    taskService.save(implTask);
    TaskInstance newTestTaskInstance = taskInstanceService.createEmpty(currentUser, testTask);
    redirectAttributes.addAttribute("taskId", testTask.getId());
    redirectAttributes.addAttribute("testTaskInstanceId", 0L);
    return new RedirectView("/playTurn/" + currentChallenge.getId());
  }

  @RequestMapping(value = "/playTurn/{challengeId}")
  public RedirectView playTurn(Model model, @PathVariable Long challengeId,
      RedirectAttributes redirectAttributes) {
    
    Challenge currentChallenge = challengeService.findOne(challengeId);
    logger.info("Current Challenge fetched: " + currentChallenge);
    
    int index = gameplayService.getNumberOfTasks(currentChallenge) / 2;
    logger.info("Highest index of tasks in current challenge: " + index);
    
    if (!gameplayService.isParticipating(currentChallenge)) {
      logger.info("Not participating.");
      
      if (currentChallenge.getSecondPlayer() == null) {
        currentChallenge.setSecondPlayer(userService.getCurrentUser());
        challengeService.save(currentChallenge);
        redirectAttributes.addAttribute("Current user saved as a participant"
            + " (second player) to current challenge.");
      } else {
        redirectAttributes.addAttribute("message", "naaaaaughty!");
        return new RedirectView("/error");
      }
    }
    
    TaskInstance unfinished = challengeService.getUnfinishedTaskInstance(currentChallenge);
    logger.info("Unfinished task inside current challenge fetched: " + unfinished);
    if (unfinished != null && unfinished.getUser().equals(userService.getCurrentUser())) {
      //redirectAttributes.addAttribute("taskInstanceId", unfinished.getId());
      logger.info("Found unfinished taskinstance owned by current user, redirecting to \"/task\"");
      
      return new RedirectView("/task/" + unfinished.getId());
    } else if (unfinished != null
        && !unfinished.getUser().equals(userService.getCurrentUser())) {
      logger.info("Unfinished taskinstance found, but not owned by the"
          + " current user, redirecting to \"/user\"");
      return new RedirectView("/user");
    }

    if (gameplayService.isTestTurnInLiveChallenge(currentChallenge)) {
      redirectAttributes.addFlashAttribute("challengeId", currentChallenge.getId());
      redirectAttributes.addFlashAttribute("challenge", currentChallenge);
      redirectAttributes.addFlashAttribute("minLength", GameplayService.CHALLENGE_MIN_LENGTH);
      logger.info("Found even number of completed taskinstances, redirecting to \"/newtaskpair\"");
      
      return new RedirectView("/newtaskpair");
    }

    if (gameplayService.isImplementationTurnInLiveChallenge(currentChallenge)) {
      Task implTask = gameplayService.getTopmostImplementationTask(currentChallenge, index);
      logger.info("implementation task: " + implTask.toString());
      Task testTask = gameplayService.getTopmostTestTask(currentChallenge, index);
      logger.info("test task: " + testTask.toString());
      redirectAttributes.addAttribute("taskId", implTask.getId());
      redirectAttributes.addAttribute("testTaskInstanceId",
          taskInstanceService.getByTaskAndUser(testTask, testTask.getAuthor()).getId());
      logger.info("Found uneven number of completed taskinstances, "
              + "redirecting to \"/newtaskpair\"");
      return new RedirectView("/newTaskInstance");
    }
    
    logger.info("Not user's turn, redirecting to \"/user\"");
    return new RedirectView("/user");

  }

  @RequestMapping("/closeChallenge/{challengeId}")
  public RedirectView closeChallenge(@PathVariable Long challengeId,
      RedirectAttributes redirectAttributes) {
    Challenge currentChallenge = challengeService.findOne(challengeId);
    currentChallenge.setOpen(false);
    challengeService.save(currentChallenge);
    redirectAttributes.addFlashAttribute("message", "Challenge closed.");
    return new RedirectView("/user");
  }

}
