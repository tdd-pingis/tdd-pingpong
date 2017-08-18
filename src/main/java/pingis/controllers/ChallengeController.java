package pingis.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.services.ChallengeService;
import pingis.services.TaskInstanceService;
import pingis.services.TaskService;
import pingis.services.UserService;

@Controller
public class ChallengeController {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  ChallengeService challengeService;
  @Autowired
  TaskService taskService;
  @Autowired
  TaskInstanceService taskInstanceService;
  @Autowired
  UserService userService;

  @RequestMapping(value = "/newchallenge")
  public String newChallenge(Model model) {

    return "newchallenge";
  }

  @RequestMapping(value = "/createChallenge", method = RequestMethod.POST)
  public RedirectView createChallenge(String challengeName, String challengeDesc,
      String challengeType, String realm, RedirectAttributes redirectAttributes) {

    Challenge newChallenge = new Challenge(challengeName,
        userService.getCurrentUser(),
        challengeDesc, challengeType == "PROJECT" ? ChallengeType.PROJECT : ChallengeType.MIXED);
    logger.info(newChallenge.toString());

    return new RedirectView("/newtaskpair");
  }

  @RequestMapping(value = "/newtaskpair")
  public String newTaskPair(Model model) {
    return "newtaskpair";
  }

  @RequestMapping(value = "/createTaskPair", method = RequestMethod.POST)
  public RedirectView createTaskPair(String testTaskName, String implementationTaskName,
      String testTaskDesc, String implementationTaskDesc,
      String testCodeStub, String implementatinCodeStub,
      RedirectAttributes redirectAttributes) {
    // create and save new tasks, redirect to createtaskinstance

    return new RedirectView("/user");
  }
}
