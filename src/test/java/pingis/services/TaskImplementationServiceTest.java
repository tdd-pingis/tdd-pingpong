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
public class TaskImplementationServiceTest {

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
    private TaskImplementation testTaskImplementation;
    private TaskImplementation implementationTaskImplementation;
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
        testTaskImplementation = new TaskImplementation(testUser,
                "thisShouldBeEmpty", testTask);

        implementationTaskImplementation = new TaskImplementation(testUser,
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
                .thenReturn(testTaskImplementation);

        TaskImplementation result = taskImplementationService
                .getCorrespondingTestTaskImplementation(implementationTaskImplementation);

        verify(userRepositoryMock).findOne(0l);
        verify(taskRepositoryMock).findByIndexAndChallenge(implementationTask.getIndex() - 1, testChallenge);
        verify(taskImplementationRepositoryMock).findByTaskAndUser(testTask, testUser);

        verifyNoMoreInteractions(taskRepositoryMock);
        verifyNoMoreInteractions(userRepositoryMock);
        verifyNoMoreInteractions(taskImplementationRepositoryMock);

        assertEquals(result, testTaskImplementation);
    }

    @Test
    public void getCorrespondingImplementationTaskImplementationTest() {
        when(userRepositoryMock.findOne(0l)).thenReturn(testUser);
        when(taskRepositoryMock.findByIndexAndChallenge(testTask.getIndex() + 1, testChallenge))
                .thenReturn(implementationTask);
        when(taskImplementationRepositoryMock.findByTaskAndUser(implementationTask, testUser))
                .thenReturn(implementationTaskImplementation);

        TaskImplementation result = taskImplementationService
                .getCorrespondingImplTaskImplementation(testTaskImplementation);

        verify(userRepositoryMock).findOne(0l);
        verify(taskRepositoryMock).findByIndexAndChallenge(testTask.getIndex() + 1, testChallenge);
        verify(taskImplementationRepositoryMock).findByTaskAndUser(implementationTask, testUser);

        verifyNoMoreInteractions(taskRepositoryMock);
        verifyNoMoreInteractions(userRepositoryMock);
        verifyNoMoreInteractions(taskImplementationRepositoryMock);

        assertEquals(result, implementationTaskImplementation);
    }

    @Test
    public void testFindOne() {
        when(taskImplementationRepositoryMock.findOne(testTaskImplementation.getId()))
                .thenReturn(testTaskImplementation);

        TaskImplementation result = taskImplementationService.findOne(testTaskImplementation.getId());

        verify(taskImplementationRepositoryMock).findOne(testTaskImplementation.getId());
        verifyNoMoreInteractions(taskImplementationRepositoryMock);

        assertEquals(result, testTaskImplementation);
    }


    @Test
    public void testUpdateTaskImplementationCode() {
        assertEquals("thisShouldBeEmpty", testTaskImplementation.getCode());

        when(taskImplementationRepositoryMock.findOne(testTaskImplementation.getId()))
                .thenReturn(testTaskImplementation);

        String testCode = "Return 1+1;";

        TaskImplementation result = taskImplementationService
                .updateTaskImplementationCode(testTaskImplementation.getId(), testCode);

        verify(taskImplementationRepositoryMock).findOne(testTaskImplementation.getId());
        verifyNoMoreInteractions(taskImplementationRepositoryMock);

        assertEquals(testCode, result.getCode());
    }

}