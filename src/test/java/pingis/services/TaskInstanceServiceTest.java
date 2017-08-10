package pingis.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.entities.*;
import pingis.repositories.TaskImplementationRepository;
import pingis.repositories.TaskRepository;
import pingis.repositories.UserRepository;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TaskImplementationService.class})
public class TaskInstanceServiceTest {

    @Autowired
    private TaskImplementationService taskImplementationService;
    @MockBean
    private TaskImplementationRepository taskImplementationRepositoryMock;
    @MockBean
    private UserRepository userRepositoryMock;
    @MockBean
    private TaskRepository taskRepositoryMock;

    private User testUser;
    private Task testTask;
    private Task implementationTask;
    private TaskInstance testTaskInstance;
    private TaskInstance implementationTaskInstance;
    private Challenge testChallenge;

    @Before
    public void setUp() {
        testUser = new User(1l, "testUser", 1);
        testTask = new Task(1, ImplementationType.TEST, testUser,
                "Desc", "Desc", "Code",
                1, 1);
        implementationTask = new Task(2,
                ImplementationType.IMPLEMENTATION, testUser,
                "Desc", "Desc", "Code",
                1, 1);
        testTaskInstance = new TaskInstance(testUser,
                "thisShouldBeEmpty", testTask);

        implementationTaskInstance = new TaskInstance(testUser,
                "return 'This is my implementation';", implementationTask);
        testChallenge = new Challenge("Name",
                testUser, "Simple calculator", ChallengeType.MIXED);
        testChallenge.addTask(testTask);
        testChallenge.addTask(implementationTask);
        testTask.setChallenge(testChallenge);
        implementationTask.setChallenge(testChallenge);
    }

    @Test
    public void getCorrespondingTestTaskImplementationTest() {
        when(userRepositoryMock.findOne(0l)).thenReturn(testUser);
        when(taskRepositoryMock.findByIndexAndChallenge(implementationTask
                .getIndex() - 1, testChallenge))
                .thenReturn(testTask);
        when(taskImplementationRepositoryMock.findByTaskAndUser(testTask, testUser))
                .thenReturn(testTaskInstance);

        TaskInstance result = taskImplementationService
                .getCorrespondingTestTaskInstance(implementationTaskInstance);

        verify(userRepositoryMock).findOne(0l);
        verify(taskRepositoryMock).findByIndexAndChallenge(implementationTask.getIndex() - 1, testChallenge);
        verify(taskImplementationRepositoryMock).findByTaskAndUser(testTask, testUser);

        verifyNoMoreInteractions(taskRepositoryMock);
        verifyNoMoreInteractions(userRepositoryMock);
        verifyNoMoreInteractions(taskImplementationRepositoryMock);

        assertEquals(result, testTaskInstance);
    }

    @Test
    public void getCorrespondingImplementationTaskImplementationTest() {
        when(userRepositoryMock.findOne(0l)).thenReturn(testUser);
        when(taskRepositoryMock.findByIndexAndChallenge(testTask.getIndex() + 1, testChallenge))
                .thenReturn(implementationTask);
        when(taskImplementationRepositoryMock.findByTaskAndUser(implementationTask, testUser))
                .thenReturn(implementationTaskInstance);

        TaskInstance result = taskImplementationService
                .getCorrespondingImplTaskInstance(testTaskInstance);

        verify(userRepositoryMock).findOne(0l);
        verify(taskRepositoryMock).findByIndexAndChallenge(testTask.getIndex() + 1, testChallenge);
        verify(taskImplementationRepositoryMock).findByTaskAndUser(implementationTask, testUser);

        verifyNoMoreInteractions(taskRepositoryMock);
        verifyNoMoreInteractions(userRepositoryMock);
        verifyNoMoreInteractions(taskImplementationRepositoryMock);

        assertEquals(result, implementationTaskInstance);
    }

    @Test
    public void testFindOne() {
        when(taskImplementationRepositoryMock.findOne(testTaskInstance.getId()))
                .thenReturn(testTaskInstance);

        TaskInstance result = taskImplementationService.findOne(testTaskInstance.getId());

        verify(taskImplementationRepositoryMock).findOne(testTaskInstance.getId());
        verifyNoMoreInteractions(taskImplementationRepositoryMock);

        assertEquals(result, testTaskInstance);
    }


    @Test
    public void testUpdateTaskImplementationCode() {
        assertEquals("thisShouldBeEmpty", testTaskInstance.getCode());

        when(taskImplementationRepositoryMock.findOne(testTaskInstance.getId()))
                .thenReturn(testTaskInstance);

        String testCode = "Return 1+1;";

        TaskInstance result = taskImplementationService
                .updateTaskImplementationCode(testTaskInstance.getId(), testCode);

        verify(taskImplementationRepositoryMock).findOne(testTaskInstance.getId());
        verifyNoMoreInteractions(taskImplementationRepositoryMock);

        assertEquals(testCode, result.getCode());
    }

}