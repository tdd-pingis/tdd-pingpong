package pingis.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MiscController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/sandbox")
    public String sandbox() {
        return "sandbox";
    }
}
