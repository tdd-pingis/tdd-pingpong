package pingis.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.util.List;

import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pingis.entities.ImplementationType;
import pingis.entities.Task;
import pingis.entities.Challenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import static org.springframework.data.jpa.domain.Specifications.where;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pingis.entities.ChallengeImplementation;
import pingis.entities.ImplementationType;
import pingis.entities.QuerySpecifications;
import pingis.entities.TaskImplementation;
import pingis.entities.User;
import pingis.repositories.ChallengeImplementationRepository;
import pingis.repositories.TaskImplementationRepository;
import pingis.repositories.TaskRepository;
import pingis.repositories.UserRepository;

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
    private SubmissionPackagingService packagingService;

    @Autowired
    private SubmissionSenderService senderService;


    @RequestMapping(value = "/task/{taskImplementationId}", method = RequestMethod.GET)
    public String task(Model model,
            @PathVariable Long taskImplementationId, @AuthenticationPrincipal User user) {

        TaskImplementation taskImplementation =
                taskImplementationService.findOne(taskImplementationId);
        if (taskImplementation == null) {
            model.addAttribute("errormessage","no such task implementation");
            return "error";
        }
        Challenge currentChallenge = taskImplementation.getChallengeImplementation().getChallenge();
        model.addAttribute("challenge",
                currentChallenge);
        model.addAttribute("task",
                taskImplementation.getTask());
        model.addAttribute("taskImplementationId",
                taskImplementationId);
        Map<String, EditorTabData> editorContents = editorService.generateEditorContents(taskImplementation);
        model.addAttribute("testCode", editorContents.get("editor2").code);
        model.addAttribute("implementationCode", editorContents.get("editor1").code);
        String implFileName = JavaClassGenerator.generateImplClassFilename(currentChallenge);
        String testFileName = JavaClassGenerator.generateTestClassFilename(currentChallenge);

        model.addAttribute("implementationFileName", implFileName);
        model.addAttribute("testFileName", testFileName);

        return "task";
    }

    // TODO: This should actually be a separate service...
    private TmcSubmission submitToTmc(Challenge challenge, String implementationCode, String testCode)
            throws IOException, ArchiveException {
        Map<String, byte[]> files = new HashMap<>();

        String implFileName = JavaClassGenerator.generateImplClassFilename(challenge);
        String testFileName = JavaClassGenerator.generateTestClassFilename(challenge);

        files.put(implFileName, implementationCode.getBytes());
        files.put(testFileName, testCode.getBytes());

        byte[] packaged = packagingService.packageSubmission(files);
        return senderService.sendSubmission(packaged);
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public RedirectView task(String implementationCode,
                             String testCode,
                             long taskImplementationId,
                             RedirectAttributes redirectAttributes) throws IOException, ArchiveException {
        TaskImplementation taskImplementation = taskImplementationService.findOne(taskImplementationId);
        Task currentTask = taskImplementation.getTask();

        Challenge currentChallenge = currentTask.getChallenge();

        redirectAttributes.addAttribute("taskImplementationId", taskImplementationId);
        String checkedCode;
        if (currentTask.getType() == ImplementationType.IMPLEMENTATION) {
            checkedCode = implementationCode;
        } else {
            checkedCode = testCode;
        }
        String[] syntaxErrors = JavaSyntaxChecker.parseCode(checkedCode);

        if (syntaxErrors != null) {
            redirectAttributes.addFlashAttribute("errors", syntaxErrors);
            redirectAttributes.addFlashAttribute("code", checkedCode);

            return new RedirectView("/task/{taskImplementationId}");
        }
        
        TmcSubmission submission = submitToTmc(currentChallenge, implementationCode, testCode);
        redirectAttributes.addAttribute("submission", submission);

        return new RedirectView("/feedback");
    }

    @RequestMapping("/feedback")
    public String feedback(Model model) {
        model.addAttribute("feedback", "Good work!");
        return "feedback";
    }


}
