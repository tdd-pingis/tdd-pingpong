package pingis.services;

import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import pingis.entities.*;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.Application;
import pingis.entities.TaskInstance;
import pingis.utils.EditorTabData;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
public class EditorServiceTest {

    @Autowired
    private EditorService editorService;
    @MockBean
    private TaskInstanceService taskInstanceServiceMock;

    @MockBean
    private TaskService taskServiceMock;
    
    private User testUser;
    private Task testTask;
    private Task implementationTask;
    private Challenge challenge;
    private TaskInstance passedTaskInstance;
    private TaskInstance returnedTaskInstance;

    @Before
    public void setUp() {
        this.challenge = new Challenge("testchallenge", testUser, "testing");
        this.testTask = new Task(
                1,
                TaskType.TEST,
                testUser,
                "test",
                "testing",
                "public void test",
                1, 1);
        this.implementationTask = new Task(
                2,
                TaskType.IMPLEMENTATION,
                testUser,
                "implementing",
                "testing",
                "public void implementation",
                2, 2);
        testTask.setChallenge(challenge);
        implementationTask.setChallenge(challenge);
        this.passedTaskInstance = new TaskInstance(testUser, "", implementationTask);
        this.returnedTaskInstance = new TaskInstance(testUser, "public void test", testTask);
                
    }
    
    @Test
    public void testEditorServiceWithTestTask() {
        when(taskServiceMock.getCorrespondingImplementationTask(returnedTaskInstance, challenge))
                .thenReturn(implementationTask);
        Map<String, EditorTabData> result = editorService.generateEditorContents(returnedTaskInstance);
        assertEquals(result.get("editor1").code, "public void test");
        verify(taskServiceMock).getCorrespondingImplementationTask(returnedTaskInstance, challenge);
        verifyNoMoreInteractions(taskServiceMock);
    }
    
    @Test
    public void testEditorServiceWithImplementationTask() {
        when(taskInstanceServiceMock.getCorrespondingTestTaskInstance(this.passedTaskInstance)).
                thenReturn(this.returnedTaskInstance);
        Map<String, EditorTabData> result = this.editorService.generateEditorContents(passedTaskInstance);
        assertEquals(result.get("editor2").code, "public void test");
        assertEquals(result.get("editor1").code, "public void implementation");
        verify(taskInstanceServiceMock, times(1)).
                getCorrespondingTestTaskInstance(passedTaskInstance);
        verifyNoMoreInteractions(taskInstanceServiceMock);
    }

}
