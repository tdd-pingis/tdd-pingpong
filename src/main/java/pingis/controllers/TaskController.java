package pingis.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pingis.entities.ImplementationType;
import pingis.entities.Task;
import pingis.entities.Challenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    private SubmissionPackagingService packagingService;

    @Autowired
    private SubmissionSenderService senderService;

    @RequestMapping(value = "/task/{challengeId}/{taskId}", method = RequestMethod.GET)
    public String task(Model model,
                       @PathVariable Long challengeId,
                       @PathVariable int taskId) {

        Challenge currentChallenge = challengeService.findOne(challengeId);
        Task currentTask = taskService.findTaskInChallenge(challengeId, taskId);
        LinkedHashMap<String, EditorTabData> editorContents = editorService.generateContent(currentTask);

        model.addAttribute("challenge", currentChallenge);
        model.addAttribute("task", currentTask);
        model.addAttribute("editorContents", editorContents);

        String implFileName = JavaClassGenerator.generateImplClassFilename(currentChallenge);
        String testFileName = JavaClassGenerator.generateTestClassFilename(currentChallenge);

        model.addAttribute("implementationFileName", implFileName);
        model.addAttribute("testFileName", testFileName);

        String implCode = "TODO impl";
        String testCode = "TODO test";

        if (currentTask.getType() == ImplementationType.IMPLEMENTATION) {
            implCode = currentTask.getCodeStub();
        } else {
            testCode = currentTask.getCodeStub();
        }

        model.addAttribute("implementationCode", implCode);
        model.addAttribute("testCode", testCode);

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
                             Long challengeId,
                             Integer taskId,
                             RedirectAttributes redirectAttributes) throws IOException, ArchiveException {
        Challenge currentChallenge = challengeService.findOne(challengeId);
        Task currentTask = taskService.findTaskInChallenge(challengeId, taskId);

        redirectAttributes.addAttribute("challengeId", currentChallenge.getId());
        redirectAttributes.addAttribute("taskId", currentTask.getTaskId());

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

            return new RedirectView("/task/{challengeId}/{taskId}");
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
