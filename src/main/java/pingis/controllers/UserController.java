package pingis.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import static org.springframework.data.jpa.domain.Specifications.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pingis.entities.ChallengeImplementation;
import pingis.entities.ChallengeImplementationSpecifications;
import pingis.entities.CodeStatus;
import pingis.entities.QuerySpecifications;
import pingis.entities.User;
import pingis.repositories.ChallengeImplementationRepository;

@Profile(value = {"prod", "oauth"})
@Controller
public class UserController {
    
    @Autowired
    ChallengeImplementationRepository cir;
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "redirect:/oauth2/authorize/code/tmc";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String user(Model model, @AuthenticationPrincipal User user) {

        // TODO: update dashboard template
        return "user";
    }
    
    @RequestMapping(value="/error", method = RequestMethod.GET)
    public String error(Model model) {
        return "error";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model) {
        return "admin";
    }
}
