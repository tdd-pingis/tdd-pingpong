package pingis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pingis.entities.User;
import pingis.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;

@Profile("dev")
@Controller
public class UserDevController {
    
    @Autowired
    UserService userService;
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String user(Model model) {
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        if(!userService.authenticateUser(username)) {
            userService.save(new User(username));
            
            // TODO: return "redirect:/welcome-new-user";
            return "user";
        }
        
        // Add necessary attributes...
        // model.addAttribute(key, value);
        return "user";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model) {
        return "admin";
    }
}
