package pingis.services;

import org.springframework.stereotype.Service;
import pingis.utils.EditorTabData;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import pingis.entities.Challenge;
import pingis.entities.ImplementationType;
import pingis.entities.TaskImplementation;
import pingis.utils.JavaClassGenerator;

@Service
public class EditorService {
    
    @Autowired
    TaskImplementationService taskImplementationService;
    
    public EditorService() {
    }

    public Map<String, EditorTabData> generateEditorContents(TaskImplementation taskImplementation) {
        Map<String, EditorTabData> tabData;
        Challenge currentChallenge = taskImplementation.getTask().getChallenge();
        if (taskImplementation.getTask().getType().equals(ImplementationType.TEST)) {
            tabData = this.generateTestTaskTabs(taskImplementation, currentChallenge);
        } else {
            tabData = this.generateImplTaskTabs(taskImplementation, currentChallenge);
        }
        return tabData;
    }

    private Map<String, EditorTabData> generateTestTaskTabs(TaskImplementation taskImplementation,
            Challenge currentChallenge) {
        Map<String, EditorTabData> tabData = new LinkedHashMap();
        TaskImplementation implTaskImplementation
                = taskImplementationService.getCorrespondingImplTaskImplementation(taskImplementation);
        EditorTabData tab1 = new EditorTabData(
                JavaClassGenerator.generateTestClassFilename(currentChallenge),
                taskImplementation.getTask().getCodeStub());
        EditorTabData tab2 = new EditorTabData(
                JavaClassGenerator.generateImplClassFilename(currentChallenge),
                implTaskImplementation.getTask().getCodeStub());
        tabData.put("editor1", tab1);
        tabData.put("editor2", tab2);
        return tabData;
    }

    private Map<String, EditorTabData> generateImplTaskTabs(TaskImplementation taskImplementation,
            Challenge currentChallenge) {
        Map<String, EditorTabData> tabData = new LinkedHashMap();
        TaskImplementation testTaskImplementation
                = taskImplementationService.getCorrespondingTestTaskImplementation(
                        taskImplementation);
        EditorTabData tab2 = new EditorTabData(
                "Implement code here",
                taskImplementation.getTask().getCodeStub());
        EditorTabData tab1 = new EditorTabData("Test to fulfill",
                testTaskImplementation
                        .getCode());
        tabData.put("editor2", tab1);
        tabData.put("editor1", tab2);
        return tabData;
    }

}
