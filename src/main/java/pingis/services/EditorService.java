package pingis.services;

import org.springframework.stereotype.Service;
import pingis.entities.Task;
import pingis.utils.EditorTabData;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.data.jpa.domain.Specifications.where;
import org.springframework.ui.Model;
import pingis.entities.Challenge;
import pingis.entities.ChallengeImplementation;
import pingis.entities.ImplementationType;
import pingis.entities.QuerySpecifications;
import static pingis.entities.QuerySpecifications.hasChallenge;
import static pingis.entities.QuerySpecifications.hasIndex;
import pingis.entities.TaskImplementation;

@Service
public class EditorService {
    @Autowired
    TaskService taskService;
    @Autowired
    TaskImplementationService taskImplementationService;
    
    private LinkedHashMap<String, EditorTabData> content;

    public EditorService() {
        content = new LinkedHashMap<>();
    }

    public Map<String, EditorTabData> generateEditorContents(TaskImplementation taskImplementation) {

        Map<String, EditorTabData> tabData = new LinkedHashMap();
        if (taskImplementation.getTask().getType().equals(ImplementationType.TEST)) {
            EditorTabData editorTabData = new EditorTabData(
                    "Write your test here",
                    taskImplementation.getTask().getCodeStub());
            tabData.put("editor1", editorTabData);
        } else {
            TaskImplementation testTaskImplementation =
                    taskImplementationService.getCorrespondingTestTaskImplementation(
                            taskImplementation);
            EditorTabData tab1 =
                    new EditorTabData(
                            "Implement code here",
                            taskImplementation.getTask().getCodeStub());
 
            EditorTabData tab2
                    = new EditorTabData("Test to fulfill",
                            testTaskImplementation
                                    .getCode());
            tabData.put("editor1", tab1);
            tabData.put("editor2", tab2);
        }
        return tabData;
    }

    public LinkedHashMap<String, EditorTabData> getContent() {
        return content;
    }
}
