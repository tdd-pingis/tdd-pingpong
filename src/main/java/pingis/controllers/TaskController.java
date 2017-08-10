package pingis.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pingis.entities.Task;
import pingis.entities.Challenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pingis.entities.TaskType;
import pingis.entities.TaskInstance;

import pingis.entities.User;
import pingis.entities.tmc.TmcSubmission;

import pingis.services.*;
import pingis.utils.EditorTabData;
import pingis.utils.JavaClassGenerator;
import pingis.utils.JavaSyntaxChecker;
import pingis.services.UserService;


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
    private SubmissionPackagingService packagingService;

    @Autowired
    private SubmissionSenderService senderService;


    @RequestMapping(value = "/task/{taskInstanceId}", method = RequestMethod.GET)
    public String task(Model model,
            @PathVariable Long taskInstanceId) {

        TaskInstance taskInstance =
                taskInstanceService.findOne(taskInstanceId);
        if (taskInstance == null) {
            model.addAttribute("errormessage","no such task instance");
            return "error";
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
    
    private String[] checkErrors(String submissionCode, String staticCode) {
        String[] submissionSyntaxErrors = JavaSyntaxChecker.parseCode(submissionCode);
        String[] staticSyntaxErrors = JavaSyntaxChecker.parseCode(staticCode);
        if (submissionSyntaxErrors != null || staticSyntaxErrors != null) {
            String[] errors = ArrayUtils.addAll(submissionSyntaxErrors, staticSyntaxErrors);
            return errors;
        }
        return null;
    }

    // TODO: This should actually be a separate service...
    private TmcSubmission submitToTmc(TaskInstance taskInstance, Challenge challenge, String submissionCode,
                                      String staticCode)
            throws IOException, ArchiveException {
        logger.debug("Submitting to TMC");
        Map<String, byte[]> files = new HashMap<>();
        String implFileName = JavaClassGenerator.generateImplClassFilename(challenge);
        String testFileName = JavaClassGenerator.generateTestClassFilename(challenge);

        if (taskInstance.getTask().getType() == TaskType.TEST) {
            files.put(testFileName, submissionCode.getBytes());
            files.put(implFileName, staticCode.getBytes());
        } else {
            files.put(implFileName, submissionCode.getBytes());
            files.put(testFileName, staticCode.getBytes());
        }
        byte[] packaged = packagingService.packageSubmission(files);
        TmcSubmission submission = new TmcSubmission();
        logger.debug("Created the submission");
        submission.setTaskInstance(taskInstance);
        return senderService.sendSubmission(submission, packaged);
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public RedirectView task(String submissionCode,
                             String staticCode,
                             long taskInstanceId,
                             RedirectAttributes redirectAttributes) throws IOException, ArchiveException {
        TaskInstance taskInstance = taskInstanceService.findOne(taskInstanceId);
        Task currentTask = taskInstance.getTask();

        Challenge currentChallenge = currentTask.getChallenge();
        redirectAttributes.addAttribute("taskInstanceId", taskInstanceId);

        String[] errors = checkErrors(submissionCode, staticCode);
        if (errors != null) {
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("code", submissionCode);
            return new RedirectView("/task/{taskInstanceId}");
        }

        TmcSubmission submission = submitToTmc(taskInstance, currentChallenge, submissionCode, staticCode);
        redirectAttributes.addAttribute("submission", submission);

        // Save user's answer from left editor
        taskInstanceService.updateTaskInstanceCode(taskInstanceId, submissionCode);
        logger.debug("Redirecting to feedback");
        return new RedirectView("/feedback");
    }

    @RequestMapping("/feedback")
    public String feedback(Model model, @RequestParam long taskImplementationId) {
        Challenge currentChallenge = taskInstanceService
                .findOne(taskImplementationId)
                .getTask().getChallenge();
        model.addAttribute("challengeId", currentChallenge.getId());
        model.addAttribute("feedback", "Good work!");
        return "feedback";
    }
    
    @RequestMapping("/nextTask/{challengeId}")
    public String nextTask(@PathVariable long challengeId, Model model) {
        Challenge currentChallenge = challengeService.findOne(challengeId);
        List<Task> tasks = currentChallenge.getTasks();
        model.addAttribute("challenge", currentChallenge);
        model.addAttribute("tasks", tasks);
        return "nexttask";
    }

    @RequestMapping("/newTaskImplementation/{taskId}")
    public RedirectView newTaskImplementation(@PathVariable long taskId,
            RedirectAttributes redirectAttributes) {
        Task task = taskService.findOne(taskId);
        User user = userService.getCurrentUser();
        TaskInstance newTaskImplementation = taskInstanceService.createEmpty(user, task);
        redirectAttributes.addAttribute("taskImplementationId", newTaskImplementation.getId());
        return new RedirectView("/task/{taskImplementationId}");
    }

}
