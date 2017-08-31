package pingis.services.logic;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.Challenge;
import pingis.entities.CodeStatus;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.services.entity.TaskInstanceService;
import pingis.services.entity.TaskService;
import pingis.services.entity.UserService;

@Service
public class PracticeChallengeService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private TaskService taskService;

  @Autowired
  private UserService userService;

  @Autowired
  private TaskInstanceService taskInstanceService;

  public Task nextPracticeTask(Challenge challenge) {
    int numberOfTasks = taskService.findAllByChallenge(challenge).size();
    logger.debug("Number of tasks: " + numberOfTasks);
    User player = userService.getCurrentUser();
    List<TaskInstance> instances = taskInstanceService.getByUserAndChallenge(player, challenge);
    logger.debug("Task instances found: " + instances.size());
    if (instances.size() == numberOfTasks) {
      return null;
    }
    int currentIndex = instances.size() / 2 + 1;
    logger.debug("Current index: " + currentIndex);
    if (instances.size() % 2 == 0) {
      logger.debug("Returning test task of index " + currentIndex);
      return taskService.findByChallengeAndTypeAndIndex(challenge, TaskType.TEST, currentIndex);
    }
    logger.debug("Returning implementation task of index " + currentIndex);
    return taskService.findByChallengeAndTypeAndIndex(challenge,
        TaskType.IMPLEMENTATION,
        currentIndex);
  }

  // Hope Jussi's DataImporter-fix renders this obsolete. This is fucking nasty.
  public TaskInstance getCorrespondingTestTaskInstance(
      TaskInstance implTaskInstance) {
    return taskInstanceService
        .findByTaskAndUser(
            taskService.findByChallengeAndTypeAndIndex(implTaskInstance.getChallenge(),
                TaskType.TEST,
                implTaskInstance.getTask().getIndex()),
            userService.findOne(0L));
  }

  public TaskInstance getRandomTaskInstance(Task task) {
    List<TaskInstance> viableInstances = taskInstanceService.findByTask(task).stream()
        .filter(i -> !i.getUser().equals(userService.getCurrentUser()))
        .filter(i -> i.getStatus() == CodeStatus.DONE)
        .collect(Collectors.toList());
    return viableInstances.get(new Random().nextInt(viableInstances.size()));
  }

  public boolean isOwnChallenge(Challenge challenge, User player) {
    return challenge.getAuthor().equals(player)
        || (challenge.getSecondPlayer() != null
        && challenge.getSecondPlayer().equals(player));
  }
}
