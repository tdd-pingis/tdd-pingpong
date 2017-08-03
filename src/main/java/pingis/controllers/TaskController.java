package pingis.controllers;

import java.util.LinkedHashMap;

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
import pingis.services.ChallengeService;
import pingis.services.EditorService;
import pingis.services.TaskService;
import pingis.utils.EditorTabData;
import pingis.utils.JavaSyntaxChecker;

@Controller
public class TaskController {

    @Autowired
    ChallengeService challengeService;
    @Autowired
    TaskService taskService;
    @Autowired
    EditorService editorService;

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

        return "task";
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public RedirectView task(String code,
                             long challengeId,
                             int taskId,
                             RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("challengeId", challengeId);
        redirectAttributes.addAttribute("task", taskId);

        String[] syntaxErrors = JavaSyntaxChecker.parseCode(code);

        if (syntaxErrors == null) {
            return new RedirectView("/feedback");
        }

        redirectAttributes.addFlashAttribute("errors", syntaxErrors);
        redirectAttributes.addFlashAttribute("code", code);

        return new RedirectView("/task/{challengeId}/{taskId}");
    }


    @RequestMapping("/feedback")
    public String feedback(Model model) {
        model.addAttribute("feedback", "Good work!");
        return "feedback";
    }


}
