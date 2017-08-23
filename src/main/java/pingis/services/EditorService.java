package pingis.services;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.Challenge;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.utils.CodeStub;
import pingis.utils.CodeStubBuilder;
import pingis.utils.EditorTabData;
import pingis.utils.TestStubBuilder;

@Service
public class EditorService {

  @Autowired
  TaskInstanceService taskInstanceService;
  @Autowired
  TaskService taskService;

  public EditorService() {
  }

  public Map<String, EditorTabData> generateEditorContents(TaskInstance taskInstance) {
    Map<String, EditorTabData> tabData;
    Challenge currentChallenge = taskInstance.getTask().getChallenge();
    if (taskInstance.getTask().getType().equals(TaskType.TEST)) {
      tabData = this.generateTestTaskTabs(taskInstance, currentChallenge);
    } else {
      tabData = this.generateImplTaskTabs(taskInstance, currentChallenge);
    }
    return tabData;
  }

  private Map<String, EditorTabData> generateTestTaskTabs(TaskInstance taskInstance,
          Challenge currentChallenge) {
    Map<String, EditorTabData> tabData = new LinkedHashMap();

    CodeStubBuilder stubBuilder = new CodeStubBuilder(currentChallenge.getName());
    CodeStub implStub = stubBuilder.build();
    CodeStub testStub = new TestStubBuilder(stubBuilder).build();

    EditorTabData testTab = new EditorTabData(testStub.filename, taskInstance.getCode());
    EditorTabData implTab = new EditorTabData(implStub.filename,
            taskService.getCorrespondingTask(taskInstance.getTask()).getCodeStub());
    tabData.put("editor1", testTab);
    tabData.put("editor2", implTab);
    return tabData;
  }

  private Map<String, EditorTabData> generateImplTaskTabs(TaskInstance taskInstance,
      Challenge currentChallenge) {
    Map<String, EditorTabData> tabData = new LinkedHashMap();
    TaskInstance testTaskInstance = taskInstance.getTestTaskinstance();
    if (testTaskInstance == null) {
      // DataImporter does not set testTaskInstances. If null, use the old
      // method that gets the test by modeluser
      testTaskInstance
          = taskInstanceService.getCorrespondingTestTaskInstance(
          taskInstance);
    }

    CodeStubBuilder stubBuilder = new CodeStubBuilder(currentChallenge.getName());
    CodeStub implStub = stubBuilder.build();
    CodeStub testStub = new TestStubBuilder(stubBuilder).build();

    EditorTabData implTab = new EditorTabData(implStub.filename,
        taskInstance.getTask().getCodeStub());
    EditorTabData testTab = new EditorTabData(testStub.filename, testTaskInstance.getCode());
    tabData.put("editor2", testTab);
    tabData.put("editor1", implTab);
    return tabData;
  }

}
