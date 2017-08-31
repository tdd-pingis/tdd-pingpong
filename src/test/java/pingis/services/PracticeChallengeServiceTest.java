package pingis.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.services.entity.TaskInstanceService;
import pingis.services.entity.TaskService;
import pingis.services.entity.UserService;
import pingis.services.logic.PracticeChallengeService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PracticeChallengeService.class})
public class PracticeChallengeServiceTest {


  @Autowired
  private PracticeChallengeService practiceChallengeService;

  @MockBean
  UserService userServiceMock;

  @MockBean
  TaskService taskServiceMock;
  @MockBean
  TaskInstanceService taskInstanceServiceMock;

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
    when(userServiceMock.findOne(0L)).thenReturn(testUser);
    when(taskServiceMock.findByChallengeAndTypeAndIndex(testChallenge,
        TaskType.TEST,
        implementationTask.getIndex()))
        .thenReturn(testTask);
    when(taskInstanceServiceMock.findByTaskAndUser(testTask, testUser))
        .thenReturn(testTaskInstance);

    TaskInstance result = practiceChallengeService
        .getCorrespondingTestTaskInstance(implementationTaskInstance);

    assertEquals(result, testTaskInstance);

    verify(userServiceMock).findOne(0L);
    verify(taskServiceMock)
        .findByChallengeAndTypeAndIndex(
            testChallenge,
            TaskType.TEST, implementationTask
                .getIndex());
    verify(taskInstanceServiceMock).findByTaskAndUser(testTask, testUser);

    verifyNoMoreInteractions(taskServiceMock);
    verifyNoMoreInteractions(userServiceMock);
    verifyNoMoreInteractions(taskInstanceServiceMock);
  }
}
