package pingis.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.Challenge;
import pingis.entities.Task;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.repositories.ChallengeRepository;

@Service
public class GameplayService {


  private final ChallengeRepository challengeRepository;

  public static final int CHALLENGE_MIN_LENGTH = 2;

  @Autowired
  private TaskService taskService;

  @Autowired
  private UserService userService;

  @Autowired
  private TaskInstanceService taskInstanceService;

  @Autowired
  private ChallengeService challengeService;

  @Autowired
  public GameplayService(ChallengeRepository challengeRepo) {
    this.challengeRepository = challengeRepo;
  }


  public int getNumberOfTasks(Challenge challenge) {
    List<Task> tasks = taskService.findAllByChallenge(challenge);
    return tasks.size();
  }

  public boolean isTestTurnInLiveChallenge(Challenge challenge) {
    User user = userService.getCurrentUser();
    int side = user.getId() == challenge.getAuthor().getId() ? 0 : 1;
    int numberOfTasks = getNumberOfTasks(challenge);
    int highestIndex = numberOfTasks / 2;
    int numberOfDoneInstances =
        taskInstanceService.getNumberOfDoneTaskInstancesInChallenge(challenge);
    if (numberOfDoneInstances % 2 == 0 && highestIndex % 2 == side) {
      return true;
    }
    return false;
  }

  public boolean isImplementationTurnInLiveChallenge(Challenge challenge) {
    User user = userService.getCurrentUser();
    int side = user.getId() == challenge.getAuthor().getId() ? 0 : 1;
    int numberOfTasks = getNumberOfTasks(challenge);
    int highestIndex = numberOfTasks / 2;
    int numberOfDoneInstances =
        taskInstanceService.getNumberOfDoneTaskInstancesInChallenge(challenge);
    if (numberOfDoneInstances % 2 == 1 && highestIndex % 2 == side) {
      return true;
    }
    return false;
  }

  public boolean isParticipating(Challenge challenge) {
    User currentUser = userService.getCurrentUser();
    if (currentUser.getId() == challenge.getAuthor().getId()) {
      return true;
    }

    if (challenge.getSecondPlayer() == null) {
      return false;
    }

    return (currentUser.getId() == challenge.getSecondPlayer().getId());
  }

  public Task getTopmostImplementationTask(Challenge challenge, int index) {
    List<Task> tasks = taskService.findAllByChallenge(challenge);
    for (Task task : tasks) {
      if (task.getIndex() == index && task.getType() == TaskType.IMPLEMENTATION) {
        return task;
      }
    }
    return null;
  }

  public Task getTopmostTestTask(Challenge challenge, int index) {
    List<Task> tasks = taskService.findAllByChallenge(challenge);
    for (Task task : tasks) {
      if (task.getIndex() == index && task.getType() == TaskType.TEST) {
        return task;
      }
    }
    return null;
  }

  public Challenge getParticipatingLiveChallenge() {
    Optional<Challenge> findFirst = challengeService.findAll().stream()
        .filter(e -> e.getIsOpen())
        .filter(e -> isParticipating(e))
        .findFirst();

    if (findFirst.isPresent()) {
      return findFirst.get();
    } else {
      return null;
    }
  }

}
