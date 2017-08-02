package pingis.controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pingis.entities.Task;
import pingis.entities.Challenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pingis.repositories.ChallengeImplementationRepository;
import pingis.repositories.ChallengeRepository;
import pingis.repositories.TaskImplementationRepository;
import pingis.repositories.TaskRepository;
import pingis.repositories.UserRepository;
import pingis.services.DataLoader;
import pingis.utils.EditorTabData;
import pingis.utils.JavaClassGenerator;
import pingis.utils.JavaSyntaxChecker;

@Profile("dev")
@Controller
public class TaskViewController {
    
    @Autowired
    private DataLoader dataloader;
    
    @Autowired
    private ChallengeRepository challengeRepository;
    
    @Autowired
    private ChallengeImplementationRepository challengeImplRepository;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private TaskImplementationRepository taskImplementationRepository;
    
    @Autowired
    private UserRepository userRepository;
   

    @RequestMapping(value = "/task/{challengeId}/{taskId}/{taskType}", method = RequestMethod.GET)
    public String task(Model model, @PathVariable String taskType,
            @PathVariable Long challengeId, @PathVariable int taskId) {
            
       

        return "task";
    }

    

}
