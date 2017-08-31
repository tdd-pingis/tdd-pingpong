package pingis.services.logic;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.services.entity.TaskInstanceService;
import pingis.services.entity.TaskService;
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
  @Autowired
  GameplayService gameplayService;
  @Autowired
  PracticeChallengeService practiceChallengeService;

  public EditorService() {
  }

  public Map<String, EditorTabData> generateEditorContents(TaskInstance taskInstance) {
    Map<String, EditorTabData> tabData = new LinkedHashMap<>();

    CodeStubBuilder stubBuilder = new CodeStubBuilder(taskInstance.getChallenge().getName());
    CodeStub implStub = stubBuilder.build();
    CodeStub testStub = new TestStubBuilder(stubBuilder).build();

    String implCode;
    String testCode;

    if (taskInstance.getTask().getType() == TaskType.IMPLEMENTATION) {
      TaskInstance testTaskInstance = taskInstance.getTestTaskinstance();

      if (testTaskInstance == null) {
        // DataImporter does not set testTaskInstances. If null, use the old
        // method that gets the test by modeluser
        testTaskInstance = practiceChallengeService
            .getCorrespondingTestTaskInstance(
            taskInstance);
      }

      implCode = taskInstance.getTask().getCodeStub();
      testCode = testTaskInstance.getCode();
    } else {
      implCode = taskService.getCorrespondingTask(taskInstance.getTask()).getCodeStub();
      testCode = taskInstance.getCode();
    }

    tabData.put("test", new EditorTabData(testStub.filename, testCode));
    tabData.put("impl", new EditorTabData(implStub.filename, implCode));

    return tabData;
  }
}
