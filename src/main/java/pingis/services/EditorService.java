package pingis.services;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    String className = getClassNameFromCode(implCode);

    CodeStubBuilder stubBuilder = new CodeStubBuilder(className);
    CodeStub implStub = stubBuilder.build();
    CodeStub testStub = new TestStubBuilder(stubBuilder).build();

    tabData.put("test", new EditorTabData(testStub.filename, testCode));
    tabData.put("impl", new EditorTabData(implStub.filename, implCode));

    return tabData;
  }

  private String getClassNameFromCode(String code) {
    CompilationUnit cu;
    try {
      cu = JavaParser.parse(code);
    } catch (ParseProblemException ex) {
      // For development
      logger.debug("Invalid code in database, parsing with regex");
      logger.debug("Code: {}", code);
      Matcher m = Pattern.compile("public class ([a-zA-Z\\d]+) ?\\{").matcher(code);
      m.find();
      return m.group(1);
    }

    for (TypeDeclaration<?> type : cu.getTypes()) {

      if (type instanceof ClassOrInterfaceDeclaration
              && type.getModifiers().contains(Modifier.PUBLIC)) {

        return type.getNameAsString();
      }
    }

    throw new IllegalArgumentException("Task instance's code did not contain any public class");
  }
}
