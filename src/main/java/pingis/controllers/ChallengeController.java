package pingis.controllers;

import java.util.ArrayList;
import java.util.List;
import pingis.entities.Task;
import pingis.entities.Challenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pingis.repositories.ChallengeRepository;

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
        model.addAttribute("basecode", t.getCode());
        model.addAttribute("taskname", t.getName());
        model.addAttribute("taskdesc", t.getDesc());
        return "task";
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public String task(String code) {
        return "redirect:/feedback";
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
