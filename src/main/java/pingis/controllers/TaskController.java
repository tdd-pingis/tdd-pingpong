package pingis.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
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
import pingis.services.ChallengeService;
import pingis.services.DataLoader;
import pingis.services.EditorService;
import pingis.services.TaskImplementationService;
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
    @Autowired
    TaskImplementationService taskImplementationService;


    @RequestMapping(value = "/task/{taskImplementationId}", method = RequestMethod.GET)
    public String task(Model model,
            @PathVariable Long taskImplementationId, @AuthenticationPrincipal User user) {

        TaskImplementation taskImplementation =
                taskImplementationService.findOne(taskImplementationId);

        if (taskImplementation == null) {
            model.addAttribute("errormessage","no such task implementation");
            return "error";
        }
        
        // TODO: make this work
        /*if (!taskImplementation.getUser().equals(user)) {
            return "/index";
        }*/
        model.addAttribute("challenge",
                taskImplementation.getChallengeImplementation().getChallenge());
        model.addAttribute("task",
                taskImplementation.getTask());
        model.addAttribute("taskImplementationId",
                taskImplementationId);
        model.addAttribute("editorContents",
                editorService.generateEditorContents(taskImplementation));
        return "task";
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public RedirectView task(String code,
                             long challenge,
                             int task,
                             long taskImplementationId,
                             RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("challenge", challenge);
        redirectAttributes.addAttribute("task", task);
        redirectAttributes.addAttribute("taskImplementationId", taskImplementationId);

        String[] syntaxErrors = JavaSyntaxChecker.parseCode(code);

        if (syntaxErrors == null) {
            return new RedirectView("/feedback");
        }
        redirectAttributes.addFlashAttribute("errors", syntaxErrors);
        redirectAttributes.addFlashAttribute("code", code);
        return new RedirectView("/task/{taskImplementationId}");
    }


    @RequestMapping("/feedback")
    public String feedback(Model model) {
        model.addAttribute("feedback", "Good work!");
        return "feedback";
    }


}
