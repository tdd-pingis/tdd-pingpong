package pingis.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.compress.archivers.ArchiveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.CodeStatus;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.entities.sandbox.Submission;
import pingis.services.ChallengeService;
import pingis.services.EditorService;
import pingis.services.TaskInstanceService;
import pingis.services.TaskService;
import pingis.services.UserService;
import pingis.services.sandbox.SandboxService;
import pingis.utils.CodeStub;
import pingis.utils.CodeStubBuilder;
import pingis.utils.EditorTabData;
import pingis.utils.JavaSyntaxChecker;
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
  private SandboxService sandboxService;

  @RequestMapping(value = "/task/{taskInstanceId}", method = RequestMethod.GET)
  public String task(Model model,
      @PathVariable Long taskInstanceId) {

    TaskInstance taskInstance =
        taskInstanceService.findOne(taskInstanceId);
    if (taskInstance == null) {
      model.addAttribute("errormessage", "no such task instance");
      return "error";
    }

    if (!taskInstanceService.canPlayOrSkip(taskInstance)) {
      return "redirect:/error";
    }

    Challenge currentChallenge = taskInstance.getTask().getChallenge();
    model.addAttribute("challenge", currentChallenge);
    model.addAttribute("task", taskInstance.getTask());
    model.addAttribute("taskInstanceId", taskInstanceId);

    Map<String, EditorTabData> editorContents = editorService.generateEditorContents(taskInstance);
    EditorTabData tab1 = editorContents.get("editor1");
    EditorTabData tab2 = editorContents.get("editor2");

    model.addAttribute("submissionCodeStub", tab1.code);
    model.addAttribute("staticCode", tab2.code);

    boolean isTest = taskInstance.getTask().getType() == TaskType.TEST;

    model.addAttribute("submissionTabFileName", isTest ? tab1.title : tab2.title);
    model.addAttribute("staticTabFileName", isTest ? tab2.title : tab1.title);

    logger.info("entering editor view");
    return "task";
  }

  @RequestMapping(value = "/task", method = RequestMethod.POST)
  public RedirectView task(String submissionCode,
      long taskInstanceId,
      RedirectAttributes redirectAttributes) throws IOException, ArchiveException {
    logger.info("submitting");

    TaskInstance taskInstance = taskInstanceService.findOne(taskInstanceId);
    Challenge currentChallenge = taskInstance.getTask().getChallenge();

    Submission submission = submitToTmc(taskInstance, currentChallenge, submissionCode);
    
    redirectAttributes.addFlashAttribute("submissionId", submission.getId().toString());
    redirectAttributes.addFlashAttribute("taskInstance", taskInstance);
    redirectAttributes.addFlashAttribute("challenge", currentChallenge);

    // Save user's answer from left editor
    taskInstanceService.updateTaskInstanceCode(taskInstanceId, submissionCode);
    logger.debug("Redirecting to feedback");
    return new RedirectView("/feedback");
  }

  @RequestMapping("/feedback")
  public String feedback() {
    return "feedback";
  }

  @MessageMapping("/rate/{taskInstanceId}")
  public String rateTask(@DestinationVariable Long taskInstanceId, Integer rating) {
    boolean rated = taskInstanceService.rate(rating, taskInstanceId);

    if (!rated) {
      return "FAILED";
    }

    return "OK";
  }

  @RequestMapping("/randomTask")
  public RedirectView randomTask(RedirectAttributes redirectAttributes) {
    Challenge randomChallenge = challengeService.getRandomChallenge();
    redirectAttributes.addAttribute("challengeId", randomChallenge.getId());
    return new RedirectView("/randomTask/{challengeId}");
  }

  @RequestMapping("/randomTask/{challengeId}")
  public RedirectView randomTaskInChallenge(@PathVariable long challengeId,
      RedirectAttributes redirectAttributes) {

    Challenge currentChallenge = challengeService.findOne(challengeId);
    User currentUser = userService.getCurrentUser();

    if (taskService.noNextTaskAvailable(currentChallenge, currentUser)) {
      return new RedirectView("/user");
    }

    Long nextTaskInstanceId;
    Task nextTask;

    // If the next tasktype is test AND there are test-tasks available
    // OR
    // (the next random tasktype is impl, but) there are no impl-tasks left
    if ((taskService.getRandomTaskType().equals(TaskType.TEST)
        && taskService.hasNextTestTaskAvailable(currentChallenge, currentUser))
        || !taskService.hasNextImplTaskAvailable(currentChallenge, currentUser)) {
      nextTask = taskService.getRandomTestTask(currentChallenge, currentUser);
      nextTaskInstanceId = 0L;
    } else {
      nextTask = taskService.getRandomImplTask(currentChallenge, currentUser);
      nextTaskInstanceId = taskService.getRandomTaskInstance(currentChallenge,
          currentUser, nextTask).getId();
    }

    redirectAttributes.addAttribute("taskId", nextTask.getId());
    redirectAttributes.addAttribute("testTaskInstanceId", nextTaskInstanceId);

    return new RedirectView("/newTaskInstance");
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
    TaskInstance skippedTaskInstance = taskInstanceService.findOne(taskInstanceId);
    if (!taskInstanceService.canPlayOrSkip(skippedTaskInstance)) {
      return new RedirectView("/error");
    }
    Challenge currentChallenge = skippedTaskInstance.getChallenge();
    if (currentChallenge.getType() == ChallengeType.ARCADE || !currentChallenge.getIsOpen()) {
      skippedTaskInstance.setStatus(CodeStatus.DROPPED);
      taskInstanceService.save(skippedTaskInstance);
      return new RedirectView("/playChallenge/" + currentChallenge.getId());
    }
    return new RedirectView("/error");
  }

}
