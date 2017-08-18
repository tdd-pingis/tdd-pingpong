package pingis.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import pingis.utils.EditorTabData;
import pingis.utils.JavaClassGenerator;
import pingis.utils.JavaSyntaxChecker;


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
    
    if (!taskInstanceService.canContinue(taskInstance, userService.getCurrentUser())) {
      return "redirect:/error";
    }
    Challenge currentChallenge = taskInstance.getTask().getChallenge();
    model.addAttribute("challenge", currentChallenge);
    model.addAttribute("task", taskInstance.getTask());
    model.addAttribute("taskInstanceId", taskInstanceId);
    Map<String, EditorTabData> editorContents = editorService.generateEditorContents(taskInstance);
    model.addAttribute("submissionCodeStub", editorContents.get("editor1").code);
    model.addAttribute("staticCode", editorContents.get("editor2").code);
    String implFileName = JavaClassGenerator.generateImplClassFilename(currentChallenge);
    String testFileName = JavaClassGenerator.generateTestClassFilename(currentChallenge);

    if (taskInstance.getTask().getType() == TaskType.TEST) {
      model.addAttribute("submissionTabFileName", testFileName);
      model.addAttribute("staticTabFileName", implFileName);
    } else {
      model.addAttribute("submissionTabFileName", implFileName);
      model.addAttribute("staticTabFileName", testFileName);
    }
    if (model.containsAttribute("code")) {
      model.addAttribute("submissionCodeStub", model.asMap().get("code"));
    }
    return "task";
  }

  @RequestMapping(value = "/task", method = RequestMethod.POST)
  public RedirectView task(String submissionCode,
      long taskInstanceId,
      RedirectAttributes redirectAttributes) throws IOException, ArchiveException {

    String[] errors = JavaSyntaxChecker.parseCode(submissionCode);
    if (errors != null) {
      redirectAttributes.addFlashAttribute("errors", errors);
      redirectAttributes.addFlashAttribute("code", submissionCode);
      return new RedirectView("/task/{taskInstanceId}");
    }

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

  @RequestMapping("/nextTask/{challengeId}")
  public String nextTask(@PathVariable long challengeId, Model model) {
    Challenge currentChallenge = challengeService.findOne(challengeId);
    List<Task> tasks = taskService.filterTasksByUser(
            currentChallenge.getTasks(), userService.getCurrentUser());
    List<Task> testTasks = taskService.filterTasksByUser(
            taskService.getAvailableTasksByType(currentChallenge, TaskType.TEST),
            userService.getCurrentUser());
    MultiValueMap<Task, TaskInstance> implementationTasks =
            taskService.getAvailableTestTaskInstances(currentChallenge,
                    userService.getCurrentUser());
    model.addAttribute("challenge", currentChallenge);
    model.addAttribute("testTasks", testTasks);
    model.addAttribute("implementationTaskInstances", implementationTasks);
    return "nexttask";
  }

  @RequestMapping("/newTaskInstance")
  public RedirectView newTaskInstance(@RequestParam long taskId,
      @RequestParam long testTaskInstanceId,
      RedirectAttributes redirectAttributes) {
    Task task = taskService.findOne(taskId);
    User user = userService.getCurrentUser();
    TaskInstance newTaskInstance = taskInstanceService.createEmpty(user, task);
    if (task.getType() == TaskType.IMPLEMENTATION) {
      TaskInstance testTaskInstance = taskInstanceService.findOne(testTaskInstanceId);
      newTaskInstance.setTestTaskInstance(testTaskInstance);
      testTaskInstance.addImplementionTaskInstance(newTaskInstance);
      taskInstanceService.save(newTaskInstance);
      taskInstanceService.save(testTaskInstance);
    }
    redirectAttributes.addAttribute("taskInstanceId", newTaskInstance.getId());
    return new RedirectView("/task/{taskInstanceId}");
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

    String implFileName = JavaClassGenerator.generateImplClassFilename(challenge);
    String testFileName = JavaClassGenerator.generateTestClassFilename(challenge);

    if (taskInstance.getTask().getType() == TaskType.TEST) {
      files.put(testFileName, submissionCode.getBytes());
      files.put(implFileName, staticCode.getBytes());
    } else {
      files.put(implFileName, submissionCode.getBytes());
      files.put(testFileName, staticCode.getBytes());
    }

    return sandboxService.submit(files, taskInstance);
  }

}
