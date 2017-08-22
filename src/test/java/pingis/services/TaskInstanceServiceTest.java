package pingis.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.CodeStatus;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.repositories.TaskInstanceRepository;
import pingis.repositories.TaskRepository;
import pingis.repositories.UserRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TaskInstanceService.class})
public class TaskInstanceServiceTest {

  @Autowired
  private TaskInstanceService taskInstanceService;
  @MockBean
  private TaskInstanceRepository taskInstanceRepositoryMock;
  @MockBean
  private UserRepository userRepositoryMock;
  @MockBean
  private TaskRepository taskRepositoryMock;

  @MockBean
  private UserService userServiceMock;

  private User testUser;
  private Task testTask;
  private Task implementationTask;
  private TaskInstance testTaskInstance;
  private TaskInstance implementationTaskInstance;
  private Challenge testChallenge;

  @Before
  public void setUp() {
    testUser = new User(1L, "testUser", 1);
    testTask = new Task(1, TaskType.TEST, testUser,
        "Desc", "Desc", "Code",
        1, 1);
    implementationTask = new Task(1,
        TaskType.IMPLEMENTATION, testUser,
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
  public void getCorrespondingTestTaskInstanceTest() {
    when(userRepositoryMock.findById(0L)).thenReturn(Optional.of(testUser));
    when(taskRepositoryMock.findByIndexAndChallengeAndType(implementationTask
        .getIndex(), testChallenge, TaskType.TEST))
        .thenReturn(testTask);
    when(taskInstanceRepositoryMock.findByTaskAndUser(testTask, testUser))
        .thenReturn(testTaskInstance);

    TaskInstance result = taskInstanceService
        .getCorrespondingTestTaskInstance(implementationTaskInstance);

    assertEquals(result, testTaskInstance);

    verify(userRepositoryMock).findById(0L);
    verify(taskRepositoryMock)
        .findByIndexAndChallengeAndType(
            implementationTask.getIndex(),
            testChallenge,
            TaskType.TEST);
    verify(taskInstanceRepositoryMock).findByTaskAndUser(testTask, testUser);

    verifyNoMoreInteractions(taskRepositoryMock);
    verifyNoMoreInteractions(userRepositoryMock);
    verifyNoMoreInteractions(taskInstanceRepositoryMock);
  }

  @Test
  public void getCorrespondingImplementationTaskInstanceTest() {
    when(userRepositoryMock.findById(0L)).thenReturn(Optional.of(testUser));
    when(taskRepositoryMock.findByIndexAndChallengeAndType(
        testTask.getIndex(),
        testChallenge,
        TaskType.IMPLEMENTATION))
        .thenReturn(implementationTask);
    when(taskInstanceRepositoryMock.findByTaskAndUser(implementationTask, testUser))
        .thenReturn(implementationTaskInstance);

    TaskInstance result = taskInstanceService
        .getCorrespondingImplTaskInstance(testTaskInstance);

    assertEquals(result, implementationTaskInstance);

    verify(userRepositoryMock).findById(0L);
    verify(taskRepositoryMock).findByIndexAndChallengeAndType(
        testTask.getIndex(),
        testChallenge,
        TaskType.IMPLEMENTATION);
    verify(taskInstanceRepositoryMock).findByTaskAndUser(implementationTask, testUser);

    verifyNoMoreInteractions(taskRepositoryMock);
    verifyNoMoreInteractions(userRepositoryMock);
    verifyNoMoreInteractions(taskInstanceRepositoryMock);


  }

  @Test
  public void testFindOne() {
    when(taskInstanceRepositoryMock.findById(testTaskInstance.getId()))
        .thenReturn(Optional.of(testTaskInstance));

    TaskInstance result = taskInstanceService.findOne(testTaskInstance.getId());

    verify(taskInstanceRepositoryMock).findById(testTaskInstance.getId());
    verifyNoMoreInteractions(taskInstanceRepositoryMock);

    assertEquals(result, testTaskInstance);
  }


  @Test
  public void testUpdateTaskInstanceCode() {
    assertEquals("thisShouldBeEmpty", testTaskInstance.getCode());

    when(taskInstanceRepositoryMock.findById(testTaskInstance.getId()))
        .thenReturn(Optional.of(testTaskInstance));

    String testCode = "Return 1+1;";

    TaskInstance result = taskInstanceService
        .updateTaskInstanceCode(testTaskInstance.getId(), testCode);

    verify(taskInstanceRepositoryMock).findById(testTaskInstance.getId());
    verifyNoMoreInteractions(taskInstanceRepositoryMock);

    assertEquals(testCode, result.getCode());
  }

  @Test
  public void testMarkAsDone() {
    TaskInstance result = taskInstanceService.markAsDone(testTaskInstance);
    assertEquals(CodeStatus.DONE, result.getStatus());
  }

  @Test
  public void testRating() {
    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    when(taskInstanceRepositoryMock.findById(testTaskInstance.getId()))
        .thenReturn(Optional.of(testTaskInstance));

    boolean rated = taskInstanceService.rate(5, testTaskInstance.getId());

    assertTrue(rated);
    assertEquals(5.0f, testTaskInstance.getTask().getAverageRating(), Float.MIN_VALUE);
  }

  @Test
  public void testDoubleRating() {
    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    when(taskInstanceRepositoryMock.findById(testTaskInstance.getId()))
        .thenReturn(Optional.of(testTaskInstance));

    boolean rated = taskInstanceService.rate(3, testTaskInstance.getId());
    boolean rated2 = taskInstanceService.rate(5, testTaskInstance.getId());

    assertTrue(rated);
    assertFalse(rated2);
    assertEquals(3.0f, testTaskInstance.getTask().getAverageRating(), Float.MIN_VALUE);
  }
}
