package pingis.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pingis.entities.Task;
import pingis.entities.Challenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pingis.repositories.ChallengeRepository;
import pingis.utils.JavaSyntaxChecker;

@EnableOAuth2Sso
@Controller
public class ChallengeController {
    @Autowired
    private ChallengeRepository cr;

    @RequestMapping("/")
    public String index() {
    
        return "index";
    }
    
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
    
    @RequestMapping(value = "/task/{challenge}/{task}", method = RequestMethod.GET)
    public String task(Model model, @PathVariable Long challenge, @PathVariable int task) {
        Challenge c = cr.findOne(challenge);
        model.addAttribute("challengename", c.getName());
        model.addAttribute("challengedesc", c.getDesc());
        model.addAttribute("difficulty", c.getLevel());        

        Task t = c.getTasks().get(task);

        // The 'code' attribute may already be present in case of a redirect, so add
        // it only if it doesn't exist.
        if (!model.containsAttribute("code")) {
            model.addAttribute("code", t.getCode());
        }

        model.addAttribute("taskname", t.getName());
        model.addAttribute("taskdesc", t.getDesc());

        model.addAttribute("challengeId", challenge);
        model.addAttribute("taskId", task);
        
        model.addAttribute("dummycode", "public void doSomething() {\n   System.out.println(\"Hello, tabs!\");\n}");

        return "task";
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public RedirectView task(String code, long challenge, int task, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("challenge", challenge);
        redirectAttributes.addAttribute("task", task);

        String[] syntaxErrors = JavaSyntaxChecker.parseCode(code);

        if (syntaxErrors == null) {
            return new RedirectView("/feedback");
        }

        redirectAttributes.addFlashAttribute("errors", syntaxErrors);
        redirectAttributes.addFlashAttribute("code", code);

        return new RedirectView("/task/{challenge}/{task}");
    }

    @RequestMapping("/sandbox")
    public String sandbox(Model model) {

        return "sandbox";
    }
    
    @RequestMapping("/feedback")
    public String feedback(Model model) {
        model.addAttribute("feedback", "Good work!");
        return "feedback";
    }


}
