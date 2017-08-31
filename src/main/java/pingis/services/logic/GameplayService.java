package pingis.services.logic;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.CodeStatus;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskPair;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.services.entity.TaskInstanceService;
import pingis.services.entity.TaskService;
import pingis.services.entity.UserService;
import pingis.utils.CodeStubBuilder;
import pingis.utils.TestStubBuilder;

@Service
public class GameplayService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public enum TurnType {
    TEST, IMPLEMENTATION, NONE;
  }

  public static final int CHALLENGE_MIN_LENGTH = 2;

  @Autowired
  public TaskService taskService;

  @Autowired
  private UserService userService;

  @Autowired
  private TaskInstanceService taskInstanceService;

  @Autowired
  public GameplayService() {
  }


  public Task generateTaskPairAndTaskInstance(String testTaskName, String implementationTaskName,
      String testTaskDesc, String implementationTaskDesc, String testCodeStub,
      String implementationCodeStub, Challenge currentChallenge) {
    int nextIndex = taskService.getNumberOfTasks(currentChallenge) / 2 + 1;
    User currentUser = userService.getCurrentUser();
    Task testTask = new Task(nextIndex,
        TaskType.TEST,
        currentUser,
        testTaskName,
        testTaskDesc,
        testCodeStub,
        1, 1);
    Task implTask = new Task(nextIndex,
        TaskType.IMPLEMENTATION,
        currentUser,
        implementationTaskName,
        implementationTaskDesc,
        implementationCodeStub,
        1, 1);
    currentChallenge.addTask(testTask);
    currentChallenge.addTask(implTask);
    testTask.setChallenge(currentChallenge);
    implTask.setChallenge(currentChallenge);
    taskService.save(testTask);
    taskService.save(implTask);
    taskInstanceService.createEmpty(currentUser, testTask);
    return testTask;
  }

  public boolean canPlayOrSkip(TaskInstance taskInstance) {
    return taskInstance.getUser().equals(userService.getCurrentUser())
        && taskInstance.getStatus() == CodeStatus.IN_PROGRESS;
  }

  public TaskInstance newTaskInstance(Task task, TaskInstance testTaskInstance) {
    User user = userService.getCurrentUser();
    TaskInstance newTaskInstance = taskInstanceService.createEmpty(user, task);
    if (task.getType() == TaskType.IMPLEMENTATION) {
      logger.debug("Task type is implementation");
      newTaskInstance.setTestTaskInstance(testTaskInstance);
      testTaskInstance.addImplementationTaskInstance(newTaskInstance);
      taskInstanceService.save(newTaskInstance);
      taskInstanceService.save(testTaskInstance);
    }
    return newTaskInstance;
  }

  public void createTaskPair(Challenge currentChallenge, TaskPair taskPair) {
    logger.debug("Creating new task pair");
    logger.debug("Generating new task pair and instance");

    int highestIndex = taskService.findAllByChallenge(currentChallenge).size() / 2;
    logger.info("Challenge ID: " + currentChallenge.getId());
    logger.info("Challenge type: " + currentChallenge.getType());
    logger.info("Highest index: " + highestIndex);
    String testStub = "";
    String implStub = "";

    // Autogenerate code stubs
    if (currentChallenge.getType() == ChallengeType.MIXED
        || currentChallenge.getType() == ChallengeType.ARCADE
        || (currentChallenge.getType() == ChallengeType.PROJECT
        && highestIndex == 0)) {
      logger.info("generating code stubs");
      implStub = new CodeStubBuilder(taskPair.getClassName()).build().code;
      testStub = new TestStubBuilder(implStub).withTestImports().build().code;
    } else {
      User player = userService.getCurrentUser();
      User otherPlayer = currentChallenge.getAuthor().equals(player)
          ? currentChallenge.getSecondPlayer() : currentChallenge.getAuthor();
      // Challenge is a project with at least one existing task instance pair.
      // Inheriting code from previous instance pair.
      logger.info("inheriting code stubs from previous task pair");
      testStub = taskInstanceService.getByTaskAndUser(
          taskService.findByChallengeAndTypeAndIndex(currentChallenge, TaskType.TEST, highestIndex),
          otherPlayer)
          .getCode();
      implStub = taskInstanceService.getByTaskAndUser(
          taskService.findByChallengeAndTypeAndIndex(currentChallenge,
              TaskType.IMPLEMENTATION,
              highestIndex),
          player)
          .getCode();
    }

    taskPair.setImplementationCodeStub(implStub);
    taskPair.setTestCodeStub(testStub);

    logger.debug("Generating new task pair and instance");

    // NotLikeThis
    generateTaskPairAndTaskInstance(taskPair.getTestTaskName(),
        taskPair.getTestTaskName(),
        taskPair.getTestTaskDesc(),
        taskPair.getTestTaskDesc(),
        taskPair.getTestCodeStub(),
        taskPair.getImplementationCodeStub(),
        currentChallenge);
  }

  @Transactional
  public boolean rate(int rating, long taskInstanceId) {
    Optional<TaskInstance> taskInstanceOpt = taskInstanceService.findById(taskInstanceId);

    if (!taskInstanceOpt.isPresent()) {
      return false;
    }

    TaskInstance taskInstance = taskInstanceOpt.get();

    if (taskInstance.isRated()
        || taskInstance.getUser().getId() != userService.getCurrentUser().getId()) {
      // Task has already been rated or the instance doesn't belong to the current user, ignore.
      return false;
    }

    Task task = taskInstance.getTask();

    taskInstance.setRating(rating);
    task.addRating(rating);

    return true;
  }

}
