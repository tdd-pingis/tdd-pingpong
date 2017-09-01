package pingis.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import pingis.entities.Challenge;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.services.entity.TaskInstanceService;
import pingis.services.entity.TaskService;
import pingis.services.entity.UserService;
import pingis.services.logic.GameplayService;


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



  private User testUser;
  private Challenge testChallenge;
  private Task firstTestTask;
  private Task firstImplTask;
  private Task secondTestTask;
  private Task secondImplTask;
  private ArgumentCaptor<Task> testTaskCaptor;
  private TaskInstance testTaskInstance;


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
    testTaskInstance = new TaskInstance(testUser,
        "thisShouldBeEmpty", firstTestTask);
  }






  @Test
  public void testGenerateTasksAndInstance() {
    List<Task> tasks = new ArrayList<>();
    tasks.add(firstTestTask);
    tasks.add(firstImplTask);
    tasks.add(secondTestTask);
    tasks.add(secondImplTask);
    when(taskServiceMock.getNumberOfTasks(testChallenge)).thenReturn(4);
    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    TaskInstance testTaskInstance = new TaskInstance(testUser, "koodii", firstTestTask);
    when(taskInstanceServiceMock.createEmpty(any(), any())).thenReturn(testTaskInstance);
    Task testTask = gameplayService.generateTaskPairAndTaskInstance("testitaski",
        "toteutustaski", "testikuvaus",
        "toteutuskuvaus", "testitynkä",
        "toteutustynkä", testChallenge);
    verify(taskServiceMock, times(2)).save(testTaskCaptor.capture());
    assertEquals("testikuvaus", testTask.getDesc());
    verify(userServiceMock).getCurrentUser();
  }

  @Test
  public void testRating() {
    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    when(taskInstanceServiceMock.findById(testTaskInstance.getId()))
        .thenReturn(Optional.of(testTaskInstance));

    boolean rated = gameplayService
        .rate(5, testTaskInstance.getId());

    assertTrue(rated);
    assertEquals(5.0f, testTaskInstance.getTask().getAverageRating(), Float.MIN_VALUE);
  }

  @Test
  public void testDoubleRating() {
    when(userServiceMock.getCurrentUser()).thenReturn(testUser);
    when(taskInstanceServiceMock.findById(testTaskInstance.getId()))
        .thenReturn(Optional.of(testTaskInstance));

    boolean rated = gameplayService
        .rate(3, testTaskInstance.getId());
    boolean rated2 = gameplayService
        .rate(5, testTaskInstance.getId());

    assertTrue(rated);
    assertFalse(rated2);
    assertEquals(3.0f, testTaskInstance.getTask().getAverageRating(), Float.MIN_VALUE);
  }
}
