package pingis.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.entities.Challenge;
import pingis.entities.Task;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.services.entity.ChallengeService;
import pingis.services.entity.TaskInstanceService;
import pingis.services.entity.TaskService;
import pingis.services.entity.UserService;
import pingis.services.logic.GameplayService.TurnType;
import pingis.services.logic.LiveChallengeService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {LiveChallengeService.class})
public class LiveChallengeServiceTest {

  @Autowired
  private LiveChallengeService liveChallengeService;

  @MockBean
  TaskService taskServiceMock;

  @MockBean
  UserService userServiceMock;

  @MockBean
  TaskInstanceService taskInstanceServiceMock;

  @MockBean
  ChallengeService challengeServiceMock;

  private User testUser;
  private Challenge testChallenge;
  private Task firstTestTask;
  private Task firstImplTask;
  private Task secondTestTask;
  private Task secondImplTask;

  @Before
  public void setUp() {
    createUserChallengeAndTAsks();
  }

  public void createUserChallengeAndTAsks() {
    testUser = new User(1, "pekka", 1);
    testUser.setId(123);
    testChallenge = new Challenge("testihaaste", testUser, "testihaaste");
    testChallenge.setOpen(true);
    firstTestTask = new Task(1,
        TaskType.TEST,
        testUser,
        "testitesti",
        "testaa jotai",
        "public void test1",
        1, 1);
    firstImplTask = new Task(1,
        TaskType.IMPLEMENTATION,
        testUser,
        "testitoteutus",
        "toteuta jotai",
        "public void toteutus1",
        1, 1);
    secondTestTask = new Task(2,
        TaskType.TEST,
        testUser,
        "testitesti2",
        "testaa jotai2",
        "public void test2",
        1, 1);
    secondImplTask = new Task(2,
        TaskType.IMPLEMENTATION,
        testUser,
        "testitoteutus2",
        "toteuta jotai2",
        "public void toteutus2",
        1, 1);
  }

  @Test
  public void testGetTurnTypeWhenTesting() {
    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    when(taskServiceMock.getNumberOfTasks(testChallenge)).thenReturn(4);
    when(taskInstanceServiceMock.getNumberOfDoneTaskInstancesInChallenge(testChallenge))
        .thenReturn(4);
    assertEquals(TurnType.TEST, liveChallengeService
        .getTurnType(testChallenge, testUser));
    verify(taskServiceMock).getNumberOfTasks(testChallenge);
    verify(taskInstanceServiceMock).getNumberOfDoneTaskInstancesInChallenge(testChallenge);
  }

  @Test
  public void testGetTurnTypeWhenImplementing() {
    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    when(taskServiceMock.getNumberOfTasks(testChallenge)).thenReturn(4);
    when(taskInstanceServiceMock.getNumberOfDoneTaskInstancesInChallenge(testChallenge))
        .thenReturn(3);
    assertEquals(TurnType.IMPLEMENTATION, liveChallengeService
        .getTurnType(testChallenge, testUser));
    verify(taskServiceMock).getNumberOfTasks(testChallenge);
    verify(taskInstanceServiceMock).getNumberOfDoneTaskInstancesInChallenge(testChallenge);
  }

  @Test
  public void testGetTurnTypeWhenIsNotTurn() {

    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    when(taskInstanceServiceMock.getNumberOfDoneTaskInstancesInChallenge(testChallenge))
        .thenReturn(3);
    when(taskServiceMock.getNumberOfTasks(testChallenge)).thenReturn(3);
    assertEquals(TurnType.NONE, liveChallengeService
        .getTurnType(testChallenge, testUser));
    verify(taskServiceMock).getNumberOfTasks(testChallenge);
    verify(taskInstanceServiceMock).getNumberOfDoneTaskInstancesInChallenge(testChallenge);
  }





  @Test
  public void testGetTopmostImplementationTask() {
    List<Task> tasks = new ArrayList<>();
    tasks.add(firstTestTask);
    tasks.add(firstImplTask);
    tasks.add(secondTestTask);
    tasks.add(secondImplTask);
    when(taskServiceMock.findAllByChallenge(testChallenge)).thenReturn(tasks);
    assertEquals(secondImplTask.getDesc(),
        liveChallengeService
            .getTopmostImplementationTask(testChallenge)
            .getDesc());
    verify(taskServiceMock).findAllByChallenge(testChallenge);
    verifyNoMoreInteractions(taskServiceMock);
  }

  @Test
  public void testGetTopmostTestTask() {
    List<Task> tasks = new ArrayList<>();
    tasks.add(firstTestTask);
    tasks.add(firstImplTask);
    tasks.add(secondTestTask);
    tasks.add(secondImplTask);
    when(taskServiceMock.findAllByChallenge(testChallenge)).thenReturn(tasks);
    assertEquals(secondTestTask.getDesc(),
        liveChallengeService.getTopmostTestTask(testChallenge).getDesc());
    verify(taskServiceMock).findAllByChallenge(testChallenge);
  }


}
