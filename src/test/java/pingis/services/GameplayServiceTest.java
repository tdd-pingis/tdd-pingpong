package pingis.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.entities.Challenge;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.repositories.ChallengeRepository;
import pingis.services.GameplayService.TurnType;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {GameplayService.class})
public class GameplayServiceTest {

  @Autowired
  private GameplayService gameplayService;

  @MockBean
  private TaskService taskServiceMock;

  @MockBean
  private UserService userServiceMock;

  @MockBean
  private TaskInstanceService taskInstanceServiceMock;

  @MockBean
  private ChallengeService challengeServiceMock;

  @MockBean
  private ChallengeRepository challengeRepositoryMock;

  private User testUser;
  private Challenge testChallenge;
  private Task firstTestTask;
  private Task firstImplTask;
  private Task secondTestTask;
  private Task secondImplTask;
  private ArgumentCaptor<Task> testTaskCaptor;

  @Before
  public void setUp() {
    createUserChallengeAndTAsks();
    testTaskCaptor = ArgumentCaptor.forClass(Task.class);
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
  public void testGetNumberOfTasks() {
    testChallenge.addTask(firstTestTask);
    testChallenge.addTask(firstImplTask);
    List<Task> tasks = new ArrayList<>();
    tasks.add(firstTestTask);
    tasks.add(firstImplTask);
    when(taskServiceMock.findAllByChallenge(testChallenge)).thenReturn(tasks);
    assertEquals(2, gameplayService.getNumberOfTasks(testChallenge));
    verify(taskServiceMock).findAllByChallenge(testChallenge);
    verifyNoMoreInteractions(taskServiceMock);
  }

  @Test
  public void testGetTurnTypeWhenTesting() {
    testChallenge.addTask(firstTestTask);
    testChallenge.addTask(firstImplTask);
    testChallenge.addTask(secondTestTask);
    testChallenge.addTask(secondImplTask);
    List<Task> tasks = new ArrayList<>();
    tasks.add(firstTestTask);
    tasks.add(firstImplTask);
    tasks.add(secondTestTask);
    tasks.add(secondImplTask);
    when(taskServiceMock.findAllByChallenge(testChallenge)).thenReturn(tasks);
    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    when(taskInstanceServiceMock.getNumberOfDoneTaskInstancesInChallenge(testChallenge))
        .thenReturn(4);
    assertEquals(TurnType.TEST, gameplayService.getTurnType(testChallenge));
    verify(taskServiceMock).findAllByChallenge(testChallenge);
    verify(userServiceMock).getCurrentUser();
    verify(taskInstanceServiceMock).getNumberOfDoneTaskInstancesInChallenge(testChallenge);
    verifyNoMoreInteractions(taskServiceMock);
    verifyNoMoreInteractions(userServiceMock);
    verifyNoMoreInteractions(taskInstanceServiceMock);
  }

  @Test
  public void testGetTurnTypeWhenImplementing() {
    testChallenge.addTask(firstTestTask);
    testChallenge.addTask(firstImplTask);
    List<Task> tasks = new ArrayList<>();
    tasks.add(firstTestTask);
    tasks.add(firstImplTask);
    tasks.add(secondTestTask);
    tasks.add(secondImplTask);
    when(taskServiceMock.findAllByChallenge(testChallenge)).thenReturn(tasks);
    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    when(taskInstanceServiceMock.getNumberOfDoneTaskInstancesInChallenge(testChallenge))
        .thenReturn(3);
    assertEquals(TurnType.IMPLEMENTATION, gameplayService.getTurnType(testChallenge));
    verify(taskServiceMock).findAllByChallenge(testChallenge);
    verify(userServiceMock).getCurrentUser();
    verify(taskInstanceServiceMock).getNumberOfDoneTaskInstancesInChallenge(testChallenge);
    verifyNoMoreInteractions(taskServiceMock);
    verifyNoMoreInteractions(userServiceMock);
    verifyNoMoreInteractions(taskInstanceServiceMock);
  }

  @Test
  public void testGetTurnTypeWhenIsNotTurn() {
    testChallenge.addTask(firstTestTask);
    testChallenge.addTask(firstImplTask);
    List<Task> tasks = new ArrayList<>();
    tasks.add(firstTestTask);
    tasks.add(firstImplTask);
    when(taskServiceMock.findAllByChallenge(testChallenge)).thenReturn(tasks);
    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    when(taskInstanceServiceMock.getNumberOfDoneTaskInstancesInChallenge(testChallenge))
        .thenReturn(3);
    assertEquals(TurnType.NONE, gameplayService.getTurnType(testChallenge));
    verify(taskServiceMock).findAllByChallenge(testChallenge);
    verify(userServiceMock).getCurrentUser();
    verify(taskInstanceServiceMock).getNumberOfDoneTaskInstancesInChallenge(testChallenge);
    verifyNoMoreInteractions(taskServiceMock);
    verifyNoMoreInteractions(userServiceMock);
    verifyNoMoreInteractions(taskInstanceServiceMock);
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
        gameplayService.getTopmostImplementationTask(testChallenge)
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
        gameplayService.getTopmostTestTask(testChallenge).getDesc());
    verify(taskServiceMock).findAllByChallenge(testChallenge);
  }

  @Test
  public void testGetParticipatingLiveChallenge() {
    List<Challenge> challenges = new ArrayList<>();
    challenges.add(testChallenge);
    when(challengeServiceMock.findAll()).thenReturn(challenges);
    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    assertEquals(testChallenge.getDesc(), gameplayService.getParticipatingLiveChallenge()
        .getDesc());
    verify(challengeServiceMock).findAll();
    verify(userServiceMock).getCurrentUser();
    verifyNoMoreInteractions(challengeServiceMock);
    verifyNoMoreInteractions(userServiceMock);
  }

  @Test
  public void testGenerateTasksAndInstance() {
    List<Task> tasks = new ArrayList<>();
    tasks.add(firstTestTask);
    tasks.add(firstImplTask);
    tasks.add(secondTestTask);
    tasks.add(secondImplTask);
    when(taskServiceMock.findAllByChallenge(testChallenge)).thenReturn(tasks);
    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    TaskInstance testTaskInstance = new TaskInstance(testUser, "koodii", firstTestTask);
    when(taskInstanceServiceMock.createEmpty(any(), any())).thenReturn(testTaskInstance);
    Task testTask = gameplayService.generateTaskPairAndTaskInstance("testitaski",
        "toteutustaski", "testikuvaus",
        "toteutuskuvaus", "testitynkä",
        "toteutustynkä", testChallenge);
    verify(taskServiceMock, times(2)).save(testTaskCaptor.capture());
    assertEquals("testikuvaus", testTask.getDesc());
    verify(taskServiceMock).findAllByChallenge(testChallenge);
    verify(userServiceMock).getCurrentUser();
  }
}
