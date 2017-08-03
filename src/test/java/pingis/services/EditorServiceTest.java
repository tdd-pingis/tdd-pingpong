package pingis.services;

import org.junit.Before;
import org.junit.Test;
import pingis.entities.Task;
import pingis.entities.User;

import static org.junit.Assert.assertEquals;
import pingis.entities.ImplementationType;

public class EditorServiceTest {

    private EditorService editorService;
    private User testUser;
    private Task testTask;

    @Before
    public void setUp() {
        editorService = new EditorService();
        testUser = new User(1, "Matti", 1);
        testTask = new Task(1, ImplementationType.TEST, testUser, "FirstTask", "SimpleCalcluator", "return 0;", 1, 1);
    }

    @Test
    public void simpleGenerateContentTest() {
        editorService.generateContent(testTask);
        String codeInTask = editorService.getContent().get("editor1").getCode();
        assertEquals(codeInTask, testTask.getCodeStub());
    }
}
