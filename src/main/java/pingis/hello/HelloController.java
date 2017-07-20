package pingis.hello;

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
        model.addAttribute("name", "Eka tehtava");
        model.addAttribute("desc", "Tehtavana on kirjoittaa metodi, joka ratkaisee pysahtymisongelman.");
        model.addAttribute("difficulty", "1");
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
        Challenge c = new Challenge("name", "description");
        cr.save(c);
        
        return "sandbox";
    }


}