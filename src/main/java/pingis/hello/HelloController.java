package pingis.hello;

import java.util.ArrayList;
import java.util.List;
import pingis.entities.Task;
import pingis.entities.Challenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pingis.repositories.ChallengeRepository;

@Controller
public class HelloController {
    @Autowired
    private ChallengeRepository cr;

    @RequestMapping("/")
    public String index() {
        String out = "<link rel='stylesheet' href='/webjars/bootstrap/3.3.0/css/bootstrap.min.css'></link>\n" +
            "    <link rel='stylesheet' href='/css/site.css'></link>";
        out+="<p class=\"lead\">Hello bootstrap</p>";
        return out;
    }
    
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
    
    @RequestMapping("/assignment")
    public String assignment(Model model) {
        Challenge c = cr.findOne(1l);
        model.addAttribute("challengename", c.getName());
        model.addAttribute("challengedesc", c.getDesc());
        model.addAttribute("difficulty", c.getLevel());
        List<Task> tasks = c.getTasks();
        System.out.println("size of task list: "+tasks.size());
        Task t = c.getTasks().get(0);
        model.addAttribute("taskname", t.getName());
        model.addAttribute("taskdesc", t.getDesc());
        return "assignment";
    }

    @RequestMapping(value = "/assignment", method = RequestMethod.POST)
    public String submit(String code) {
        System.out.println(code);
        return "assignment";
    }
    
    @RequestMapping("/sandbox")
    public String sandbox(Model model) {
        System.out.println("moooooooi");
        System.out.println(cr.count());
        Challenge c = cr.findOne(1l);
        System.out.println(c);
        
        return "sandbox";
    }


}
