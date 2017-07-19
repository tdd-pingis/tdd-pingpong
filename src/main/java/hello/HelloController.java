package hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HelloController {

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
    public String assignment() {
        return "assignment";
    }

    @RequestMapping(value = "/assignment", method = RequestMethod.POST)
    public String submit(String code) {
        System.out.println(code);
        return "assignment";
    }


}
