package pingis.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
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
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.repositories.ChallengeRepository;
import pingis.repositories.TaskInstanceRepository;
import pingis.repositories.TaskRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TaskService.class})
public class TaskServiceTest {

  @MockBean
  private TaskInstanceRepository taskInstanceRepository;

  @MockBean
  private TaskRepository taskRepositoryMock;

  @Autowired
  private TaskService taskService;

  @MockBean
  private ChallengeRepository challengeRepositoryMock;

  private User testUser;
  private Challenge testChallenge;
  private List<Task> testTasks;
  private List<Task> implTasks;
  private ArgumentCaptor<Task> taskCaptor;
  private Random random;

  @Before
  public void setUp() {
    random = new Random();
    testTasks = new ArrayList<>();
    implTasks = new ArrayList<>();
    testUser = new User(1, "Matti", 1);
    testChallenge = new Challenge(UUID.randomUUID().toString(),
        testUser,
        UUID.randomUUID().toString());
    generateTestTasks(testTasks, TaskType.TEST);
    generateTestTasks(implTasks, TaskType.IMPLEMENTATION);
    taskCaptor = ArgumentCaptor.forClass(Task.class);
  }


  @Test
  public void simpleSaveAndFindOneTaskTest() {
    Task randomTask = getRandomTask(testTasks);
    taskService.save(randomTask);
    when(taskRepositoryMock.findById(randomTask.getId())).thenReturn(Optional.of(randomTask));

    taskService.findOne(randomTask.getId());

    verify(taskRepositoryMock).save(taskCaptor.capture());
    verify(taskRepositoryMock).findById(taskCaptor.getValue().getId());
    verifyNoMoreInteractions(taskRepositoryMock);

    Task oneTask = taskCaptor.getValue();

    assertEquals(randomTask.getName(), oneTask.getName());
  }

  @Test
  public void simpleFindAllTaskTest() {
    when(taskRepositoryMock.findAll()).thenReturn(implTasks);

    List<Task> result = taskService.findAll();
    int randomIndex = random.nextInt(result.size());

    assertEquals(implTasks.get(randomIndex), result.get(randomIndex));

    verify(taskRepositoryMock).findAll();
    verifyNoMoreInteractions(taskRepositoryMock);
  }

  @Test
  public void simpleDeleteOneTaskTest() {
    Task randomTask = getRandomTask(testTasks);
    taskService.save(randomTask);
    when(taskRepositoryMock.findById(randomTask.getId())).thenReturn(Optional.of(randomTask));

    verify(taskRepositoryMock).save(taskCaptor.capture());

    taskService.delete(taskCaptor.getValue().getId());

    verify(taskRepositoryMock).deleteById(taskCaptor.getValue().getId());

    boolean deleted = taskService.contains(taskCaptor.getValue().getId());

    assertFalse(deleted);
  }

  @Test
  public void getCorrespondingTaskForImplementation() {
    Task implTask = getRandomTask(implTasks);

    taskService.getCorrespondingTask(implTask);

    verify(taskRepositoryMock).findByIndexAndChallengeAndType(implTask.getIndex(),
        implTask.getChallenge(), TaskType.TEST);

    verifyNoMoreInteractions(taskRepositoryMock);
  }

  @Test
  public void getCorrespondingTaskForTest() {
    Task testTask = getRandomTask(testTasks);

    taskService.getCorrespondingTask(testTask);

    verify(taskRepositoryMock).findByIndexAndChallengeAndType(testTask.getIndex(),
        testTask.getChallenge(), TaskType.IMPLEMENTATION);

    verifyNoMoreInteractions(taskRepositoryMock);
  }

  @Test
  public void getAvailableTasksByType() {
    List<Task> tasks = taskService.getAvailableTasksByType(testChallenge, TaskType.TEST);
    assertEquals(testTasks, tasks);
  }

  @Test
  public void findTaskInChallenge() {
    when(challengeRepositoryMock.findById(testChallenge.getId()))
        .thenReturn(Optional.of(testChallenge));

    Task task = taskService.findTaskInChallenge(testChallenge.getId(), 3);
    assertEquals(testTasks.get(3), task);
  }

  @Test
  public void ratingTest() {
    Task randomTask = implTasks.get(0);

    when(taskRepositoryMock.findOne(randomTask.getId())).thenReturn(randomTask);

    taskService.addRatingToTask(5, randomTask.getId());

    assertEquals(5, randomTask.getAverageRating(), 0.001);
    assertEquals(1, randomTask.getNumRatings());

    verify(taskRepositoryMock).findOne(randomTask.getId());
    verifyNoMoreInteractions(taskRepositoryMock);
  }

  //TODO: Make more tests for this service.

  private Task getRandomTask(List<Task> list) {
    return list.get(random.nextInt(list.size()));
  }

  private void generateTestTasks(List<Task> list, TaskType taskType) {
    for (int i = 0; i < 25; i++) {
      Task newTask = new Task(
          random.nextInt(),
          taskType,
          testUser,
          UUID.randomUUID().toString(),
          UUID.randomUUID().toString(),
          UUID.randomUUID().toString(),
          random.nextInt(200),
          random.nextInt(200));
      newTask.setChallenge(testChallenge);
      testChallenge.getTasks().add(newTask);
      list.add(newTask);
    }
  }

}
