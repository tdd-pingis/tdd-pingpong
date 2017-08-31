package pingis.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.compress.archivers.ArchiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.CodeStatus;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.sandbox.Submission;
import pingis.services.entity.ChallengeService;
import pingis.services.entity.TaskInstanceService;
import pingis.services.entity.TaskService;
import pingis.services.entity.UserService;
import pingis.services.logic.EditorService;
import pingis.services.logic.GameplayService;
import pingis.services.sandbox.SandboxService;
import pingis.utils.CodeStub;
import pingis.utils.CodeStubBuilder;
import pingis.utils.EditorTabData;
import pingis.utils.TestStubBuilder;

@Controller
public class TaskController {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  ChallengeService challengeService;
  @Autowired
  TaskService taskService;
  @Autowired
  EditorService editorService;
  @Autowired
  TaskInstanceService taskInstanceService;
  @Autowired
  UserService userService;
  @Autowired
  GameplayService gameplayService;

  @Autowired
  private SandboxService sandboxService;

  @RequestMapping(value = "/task/{taskInstanceId}", method = RequestMethod.GET)
  public String task(Model model,
      @PathVariable Long taskInstanceId) {
    logger.debug("Get /task/{}", taskInstanceId);

    TaskInstance taskInstance =
        taskInstanceService.findOne(taskInstanceId);
    if (taskInstance == null) {
      model.addAttribute("errormessage", "no such task instance");
      return "error";
    }

    if (!gameplayService.canPlayOrSkip(taskInstance)) {
      logger.debug("Can't play or skip");
      return "redirect:/error";
    }

    Challenge currentChallenge = taskInstance.getTask().getChallenge();
    model.addAttribute("challenge", currentChallenge);
    model.addAttribute("task", taskInstance.getTask());
    model.addAttribute("taskInstanceId", taskInstanceId);

    Map<String, EditorTabData> editorContents = editorService.generateEditorContents(taskInstance);

    boolean isTest = taskInstance.getTask().getType() == TaskType.TEST;
    EditorTabData submissionTab = editorContents.get(isTest ? "test" : "impl");
    EditorTabData staticTab = editorContents.get(isTest ? "impl" : "test");

    model.addAttribute("submissionCodeStub", submissionTab.code);
    model.addAttribute("submissionTabFileName", submissionTab.title);

    model.addAttribute("staticCode", staticTab.code);
    model.addAttribute("staticTabFileName", staticTab.title);

    logger.debug("entering editor view");
    return "task";
  }

  @RequestMapping(value = "/task", method = RequestMethod.POST)
  public RedirectView task(String submissionCode,
      long taskInstanceId,
      RedirectAttributes redirectAttributes) throws IOException, ArchiveException {
    logger.debug("Submitting task");

    TaskInstance taskInstance = taskInstanceService.findOne(taskInstanceId);
    Challenge currentChallenge = taskInstance.getTask().getChallenge();

    Submission submission = submitToTmc(taskInstance, currentChallenge, submissionCode);

    redirectAttributes.addFlashAttribute("submissionId", submission.getId().toString());
    redirectAttributes.addFlashAttribute("taskInstance", taskInstance);
    redirectAttributes.addFlashAttribute("challenge", currentChallenge);
    redirectAttributes.addFlashAttribute("user", userService.getCurrentUser());

    // Save user's answer from left editor
    taskInstanceService.updateTaskInstanceCode(taskInstanceId, submissionCode);
    logger.debug("Redirecting to feedback");
    return new RedirectView("/feedback");
  }

  @RequestMapping("/feedback")
  public String feedback() {
    logger.debug("Request to /feedback");
    return "feedback";
  }

  @MessageMapping("/rate/{taskInstanceId}")
  public String rateTask(@DestinationVariable Long taskInstanceId, Integer rating) {
    logger.debug("Rating task");

    boolean rated = gameplayService
        .rate(rating, taskInstanceId);

    if (!rated) {
      logger.debug("Rating failed!");
      return "FAILED";
    }

    logger.debug("Rating ok!");
    return "OK";
  }

  private Submission submitToTmc(TaskInstance taskInstance, Challenge challenge,
      String submissionCode)
      throws IOException, ArchiveException {
    logger.debug("Submitting to TMC");
    Map<String, byte[]> files = new HashMap<>();

    String staticCode = taskService.getCorrespondingTask(taskInstance.getTask()).getCodeStub();

    CodeStubBuilder stubBuilder = new CodeStubBuilder(challenge.getName());
    CodeStub implStub = stubBuilder.build();
    CodeStub testStub = new TestStubBuilder(stubBuilder).build();

    boolean isTest = taskInstance.getTask().getType() == TaskType.TEST;
    files.put(isTest ? testStub.filename : implStub.filename, submissionCode.getBytes());
    files.put(isTest ? implStub.filename : testStub.filename, staticCode.getBytes());

    return sandboxService.submit(files, taskInstance);
  }

  @RequestMapping("/skip/{taskInstanceId}")
  public RedirectView skip(RedirectAttributes redirectAttributes,
      @PathVariable long taskInstanceId) {
    logger.debug("Request to /skip/{}", taskInstanceId);

    TaskInstance skippedTaskInstance = taskInstanceService.findOne(taskInstanceId);
    if (!gameplayService.canPlayOrSkip(skippedTaskInstance)) {
      logger.debug("Can't play or skip");
      return new RedirectView("/error");
    }

    Challenge currentChallenge = skippedTaskInstance.getChallenge();
    if (currentChallenge.getType() == ChallengeType.ARCADE || !currentChallenge.getIsOpen()) {
      logger.debug("Dropping arcade challenge");

      skippedTaskInstance.setStatus(CodeStatus.DROPPED);
      taskInstanceService.save(skippedTaskInstance);
      return new RedirectView("/playChallenge/" + currentChallenge.getId());
    }

    logger.debug("Redirecting to error");
    return new RedirectView("/error");
  }

}
