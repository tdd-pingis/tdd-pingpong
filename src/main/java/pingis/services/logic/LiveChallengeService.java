package pingis.services.logic;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.Challenge;
import pingis.entities.Task;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.services.entity.ChallengeService;
import pingis.services.entity.TaskInstanceService;
import pingis.services.entity.TaskService;
import pingis.services.entity.UserService;
import pingis.services.logic.GameplayService.TurnType;


@Service
public class LiveChallengeService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());


  @Autowired
  private UserService userService;

  @Autowired
  private TaskInstanceService taskInstanceService;

  @Autowired
  private TaskService taskService;

  @Autowired
  private ChallengeService challengeService;


  public TurnType getTurnType(Challenge challenge, User user) {

    // Author made the first task pair, so his "side" is 0.
    int side = user.getId() == challenge.getAuthor().getId() ? 0 : 1;
    int numberOfTasks = taskService.getNumberOfTasks(challenge);
    int highestIndex = numberOfTasks / 2;
    int numberOfDoneInstances =
        taskInstanceService.getNumberOfDoneTaskInstancesInChallenge(challenge);
    logger.debug("Number of done instances: " + numberOfDoneInstances);
    if (numberOfDoneInstances % 2 == 1 && highestIndex % 2 == side) {
      return TurnType.IMPLEMENTATION;
    }
    if (numberOfDoneInstances % 2 == 0 && highestIndex % 2 == side) {
      return TurnType.TEST;
    }
    return TurnType.NONE;
  }

  public Challenge createChallenge(Challenge newChallenge, User user) {
    newChallenge.setAuthor(user);
    newChallenge.setLevel(1);
    newChallenge.setOpen(true);
    newChallenge.setTasks(new ArrayList<>());
    return challengeService.save(newChallenge);
  }

  public boolean canParticipate(Challenge currentChallenge, User user) {
    if (!challengeService.isParticipating(currentChallenge, user)) {
      logger.debug("Not participating.");

      if (currentChallenge.getSecondPlayer() == null) {
        currentChallenge.setSecondPlayer(user);
        challengeService.save(currentChallenge);
        logger.debug("Current user saved as a participant"
            + " (second player) to current challenge.");
      } else {
        return false;
      }
    }
    return true;
  }

  public Task getTopmostImplementationTask(Challenge challenge) {
    List<Task> tasks = taskService.findAllByChallenge(challenge);
    int index = tasks.size() / 2;
    for (Task task : tasks) {
      if (task.getIndex() == index && task.getType() == TaskType.IMPLEMENTATION) {
        return task;
      }
    }
    return null;
  }

  public Task getTopmostTestTask(Challenge challenge) {
    List<Task> tasks = taskService.findAllByChallenge(challenge);
    int index = tasks.size() / 2;
    for (Task task : tasks) {
      if (task.getIndex() == index && task.getType() == TaskType.TEST) {
        return task;
      }
    }
    return null;
  }
}
