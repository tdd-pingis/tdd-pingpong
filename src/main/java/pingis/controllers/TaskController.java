package pingis.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pingis.entities.Task;
import pingis.entities.Challenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pingis.entities.ChallengeImplementation;

import pingis.entities.ImplementationType;
import pingis.entities.TaskImplementation;
import pingis.repositories.UserRepository;
import pingis.repositories.ChallengeImplementationRepository;

import pingis.entities.User;
import pingis.entities.TmcSubmission;
import pingis.services.*;
import pingis.utils.EditorTabData;
import pingis.utils.JavaClassGenerator;
import pingis.utils.JavaSyntaxChecker;


@Controller
public class TaskController {

    @Autowired
    ChallengeService challengeService;
    @Autowired
    TaskService taskService;
    @Autowired
    EditorService editorService;
    @Autowired
    TaskImplementationService taskImplementationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChallengeImplementationRepository challengeImplementationRepository;


    @Autowired
    private SubmissionPackagingService packagingService;

    @Autowired
    private SubmissionSenderService senderService;


    @RequestMapping(value = "/task/{taskImplementationId}", method = RequestMethod.GET)
    public String task(Model model,
            @PathVariable Long taskImplementationId) {

        TaskImplementation taskImplementation =
                taskImplementationService.findOne(taskImplementationId);
        if (taskImplementation == null) {
            model.addAttribute("errormessage","no such task implementation");
            return "error";
        }
        Challenge currentChallenge = taskImplementation.getTask().getChallenge();
        model.addAttribute("challenge",
                currentChallenge);
        model.addAttribute("task",
                taskImplementation.getTask());
        model.addAttribute("taskImplementationId",
                taskImplementationId);
        Map<String, EditorTabData> editorContents = editorService.generateEditorContents(taskImplementation);
        model.addAttribute("submissionCodeStub", editorContents.get("editor1").code);
        model.addAttribute("staticCode", editorContents.get("editor2").code);
        String implFileName = JavaClassGenerator.generateImplClassFilename(currentChallenge);
        String testFileName = JavaClassGenerator.generateTestClassFilename(currentChallenge);
        
        if (taskImplementation.getTask().getType() == ImplementationType.TEST) {
            model.addAttribute("submissionTabFileName", testFileName);
            model.addAttribute("staticTabFileName", implFileName);
        } else {
            model.addAttribute("submissionTabFileName", implFileName);
            model.addAttribute("staticTabFileName", testFileName);
        }
        return "task";
    }

    // TODO: This should actually be a separate service...
    private TmcSubmission submitToTmc(Challenge challenge, String submissionCode,
            String staticCode, ImplementationType taskType)
            throws IOException, ArchiveException {
        Map<String, byte[]> files = new HashMap<>();
        String implFileName = JavaClassGenerator.generateImplClassFilename(challenge);
        String testFileName = JavaClassGenerator.generateTestClassFilename(challenge);

        if (taskType == ImplementationType.TEST) {
            files.put(testFileName, submissionCode.getBytes());
            files.put(implFileName, staticCode.getBytes());
        } else {
            files.put(implFileName, submissionCode.getBytes());
            files.put(testFileName, staticCode.getBytes());
        }
        byte[] packaged = packagingService.packageSubmission(files);
        return senderService.sendSubmission(packaged);
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public RedirectView task(String submissionCode,
                             String staticCode,
                             long taskImplementationId,
                             RedirectAttributes redirectAttributes) throws IOException, ArchiveException {
        TaskImplementation taskImplementation = taskImplementationService.findOne(taskImplementationId);
        Task currentTask = taskImplementation.getTask();

        Challenge currentChallenge = currentTask.getChallenge();

        redirectAttributes.addAttribute("taskImplementationId", taskImplementationId);

        String[] syntaxErrors = JavaSyntaxChecker.parseCode(submissionCode);

        if (syntaxErrors != null) {
            redirectAttributes.addFlashAttribute("errors", syntaxErrors);
            redirectAttributes.addFlashAttribute("code", submissionCode);
            return new RedirectView("/task/{taskImplementationId}");
        }
        
        TmcSubmission submission = submitToTmc(currentChallenge, submissionCode, staticCode, currentTask.getType());
        redirectAttributes.addAttribute("submission", submission);

        // Save user's answer from 2nd editor
        taskImplementationService.updateTaskImplementationCode(taskImplementationId, submissionCode);

        return new RedirectView("/feedback");
    }

    @RequestMapping("/feedback")
    public String feedback(Model model) {
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
        User user = userRepository.findOne(0l); // TODO: FIX THIS
        ChallengeImplementation ci = challengeImplementationRepository.findOne(1l);
        TaskImplementation newTaskImplementation = taskImplementationService.createEmpty(user, task);
        newTaskImplementation.setChallengeImplementation(ci);
        ci.addTaskImplementation(newTaskImplementation);
        ci.setChallenge(task.getChallenge());
        redirectAttributes.addAttribute("taskImplementationId", newTaskImplementation.getId());
        return new RedirectView("/task/{taskImplementationId}");
    }

}
