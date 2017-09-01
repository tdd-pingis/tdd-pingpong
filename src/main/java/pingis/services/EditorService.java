package pingis.services;

import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.utils.CodeStub;
import pingis.utils.CodeStubBuilder;
import pingis.utils.EditorTabData;
import pingis.utils.TestStubBuilder;

@Service
public class EditorService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  TaskInstanceService taskInstanceService;
  @Autowired
  TaskService taskService;

  public EditorService() {
  }

  public Map<String, EditorTabData> generateEditorContents(TaskInstance taskInstance) {
    Map<String, EditorTabData> tabData = new LinkedHashMap<>();

    String implCode;
    String testCode;

    if (taskInstance.getTask().getType() == TaskType.IMPLEMENTATION) {
      TaskInstance testTaskInstance = taskInstance.getTestTaskinstance();

      if (testTaskInstance == null) {
        // DataImporter does not set testTaskInstances. If null, use the old
        // method that gets the test by modeluser
        testTaskInstance = taskInstanceService.getCorrespondingTestTaskInstance(
            taskInstance);
      }

      implCode = taskInstance.getTask().getCodeStub();
      testCode = testTaskInstance.getCode();
    } else {
      implCode = taskService.getCorrespondingTask(taskInstance.getTask()).getCodeStub();
      testCode = taskInstance.getCode();
    }

    CodeStubBuilder stubBuilder = CodeStubBuilder.fromCode(implCode);
    CodeStub implStub = stubBuilder.build();
    CodeStub testStub = new TestStubBuilder(stubBuilder).build();

    tabData.put("test", new EditorTabData(testStub.filename, testCode));
    tabData.put("impl", new EditorTabData(implStub.filename, implCode));

    return tabData;
  }
}
