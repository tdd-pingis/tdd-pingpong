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
import pingis.repositories.ChallengeRepository;
import pingis.utils.EditorTabData;
import pingis.utils.JavaClassGenerator;
import pingis.utils.JavaSyntaxChecker;

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

    //CHECKSTYLE:OFF
    @RequestMapping(value = "/task/{challengeId}/{taskId}/{taskType}", method = RequestMethod.GET)
    public String task(Model model, @PathVariable String taskType,
            @PathVariable Long challengeId, @PathVariable int taskId) {
        Challenge c = cr.findOne(challengeId);
        model.addAttribute("challengename", c.getName());
        model.addAttribute("challengedesc", c.getDesc());
        model.addAttribute("difficulty", c.getLevel());

        Task t = c.getTasks().get(taskId);
        List<Task> viewTasks = new ArrayList<Task>();
        viewTasks.add(t); // add all tasks that are needed in the page

        // The 'editorContents' attribute may already be present in case of a redirect, so add
        // it only if it doesn't exist.
        LinkedHashMap<String, EditorTabData> editorContents = new LinkedHashMap();
        if (!model.containsAttribute("editorContents")) {
            model.addAttribute("editorContents", editorContents);
        }

        editorContents.put("editor1", new EditorTabData("CalculatorTest.java", t.getCodeStub()));
        editorContents.put("editor2", new EditorTabData("Calculator.java",
                "public class Calculator {\n"
                + "    \n"
                + "    public Calculator() {}\n"
                + "\n"
                + "    public int multiply(int a, int b) {\n"
                + "        return a*b;\n"
                + "    }\n"
                + "}"));
        editorContents.put("editor3", new EditorTabData("Model solution", "public void testAddition() {\n"
                + "    Calculator c = new Calculator();\n"
                + "    assertEquals(8, c.multiply(2, 4));\n"
                + "}"));

        model.addAttribute("ntabs", editorContents.size());
        model.addAttribute("taskname", t.getName());
        model.addAttribute("taskdesc", t.getDesc());

        model.addAttribute("challengeId", challengeId);
        model.addAttribute("taskId", taskId);
        return "task";
    }
    //CHECKSTYLE:ON

    @RequestMapping(value = "/taskId", method = RequestMethod.POST)
    public RedirectView task(String code, long challengeId, int taskId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("challengeId", challengeId);
        redirectAttributes.addAttribute("task", taskId);

        String[] syntaxErrors = JavaSyntaxChecker.parseCode(code);

        if (syntaxErrors == null) {
            return new RedirectView("/feedback");
        }

        redirectAttributes.addFlashAttribute("errors", syntaxErrors);
        redirectAttributes.addFlashAttribute("code", code);

        return new RedirectView("/task/{challengeId}/{taskId}");
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
