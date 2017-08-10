package pingis.services;

import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import pingis.entities.Task;
import pingis.entities.User;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.Application;
import pingis.entities.Challenge;
import pingis.entities.ChallengeImplementation;
import pingis.entities.ImplementationType;
import pingis.entities.TaskImplementation;
import pingis.utils.EditorTabData;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
public class EditorServiceTest {

    @Autowired
    private EditorService editorService;
    @MockBean
    private TaskImplementationService taskImplementationServiceMock;
    
    private User testUser;
    private Task testTask;
    private Task implementationTask;
    private Challenge challenge;
    private ChallengeImplementation challengeImplementation;
    private TaskImplementation passedTaskImplementation;
    private TaskImplementation returnedTaskImplementation;

    @Before
    public void setUp() {
        this.challenge = new Challenge("testchallenge", testUser, "testing");
        this.challengeImplementation = new ChallengeImplementation(challenge, testUser, testUser);
        this.testTask = new Task(
                1,
                ImplementationType.TEST,
                testUser,
                "test",
                "testing",
                "public void test",
                1, 1);
        this.implementationTask = new Task(
                2,
                ImplementationType.IMPLEMENTATION,
                testUser,
                "implementing",
                "testing",
                "public void implementation",
                2, 2);
        this.passedTaskImplementation = new TaskImplementation(testUser, "", implementationTask);
        this.returnedTaskImplementation = new TaskImplementation(testUser, "public void test", testTask);
        this.passedTaskImplementation.setChallengeImplementation(challengeImplementation);
        this.returnedTaskImplementation.setChallengeImplementation(challengeImplementation);
                
    }
    
    @Test
    public void testEditorServiceWithTestTask() {
        when(taskImplementationServiceMock.getCorrespondingImplTaskImplementation(returnedTaskImplementation))
                .thenReturn(this.passedTaskImplementation);
        Map<String, EditorTabData> result = editorService.generateEditorContents(returnedTaskImplementation);
        assertEquals(result.get("editor1").code, "public void test");
        verify(taskImplementationServiceMock, times(1)).
                getCorrespondingImplTaskImplementation(returnedTaskImplementation);
        verifyNoMoreInteractions(taskImplementationServiceMock);
    }
    
    @Test
    public void testEditorServiceWithImplementationTask() {
        when(taskImplementationServiceMock.getCorrespondingTestTaskImplementation(this.passedTaskImplementation)).
                thenReturn(this.returnedTaskImplementation);
        Map<String, EditorTabData> result = this.editorService.generateEditorContents(passedTaskImplementation);
        assertEquals(result.get("editor2").code, "public void test");
        assertEquals(result.get("editor1").code, "public void implementation");
        verify(taskImplementationServiceMock, times(1)).
                getCorrespondingTestTaskImplementation(passedTaskImplementation);
        verifyNoMoreInteractions(taskImplementationServiceMock);
    }

}
