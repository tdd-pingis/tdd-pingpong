package pingis.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pingis.entities.OAuthUser;
import pingis.entities.User;
import pingis.services.UserService;

@Profile(value = {"prod", "oauth"})
@Controller
public class UserController {
    
    @Autowired
    UserService userService;
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "redirect:/oauth2/authorization/code/tmc";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String user(Model model, @AuthenticationPrincipal OAuthUser user) {
        userService.handleOAuthUserAuthentication(user);
        return "user";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model) {
        return "admin";
    }
}
