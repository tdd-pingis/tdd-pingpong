package pingis.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pingis.entities.Challenge;
import pingis.entities.CodeStatus;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.repositories.TaskInstanceRepository;
import pingis.repositories.TaskRepository;
import pingis.repositories.UserRepository;

@Service
public class TaskInstanceService {

  @Autowired
  private TaskRepository taskRepository;
  @Autowired
  private TaskInstanceRepository taskInstanceRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserService userService;

  public TaskInstance getCorrespondingTestTaskInstance(
      TaskInstance implTaskInstance) {
    return taskInstanceRepository
        .findByTaskAndUser(
            taskRepository
                .findByIndexAndChallengeAndType(implTaskInstance.getTask().getIndex(),
                    implTaskInstance.getTask().getChallenge(), TaskType.TEST),
            userRepository.findById(0L).get());
  }


  public TaskInstance getCorrespondingImplTaskInstance(TaskInstance testTaskInstance) {
    return taskInstanceRepository
        .findByTaskAndUser(
            taskRepository.findByIndexAndChallengeAndType(testTaskInstance.getTask()
                    .getIndex(),
                testTaskInstance.getTask().getChallenge(), TaskType.IMPLEMENTATION),
            userRepository.findById(0L).get());
  }

  public TaskInstance findOne(long taskInstanceId) {
    Optional<TaskInstance> opt = taskInstanceRepository.findById(taskInstanceId);
    if (opt.isPresent()) {
      return opt.get();
    } else {
      return null;
    }
  }

  @Transactional
  public TaskInstance updateTaskInstanceCode(Long taskInstanceId, String taskInstanceCode) {
    TaskInstance taskInstanceToUpdate = taskInstanceRepository.findById(taskInstanceId).get();
    taskInstanceToUpdate.setCode(taskInstanceCode);
    return taskInstanceToUpdate;
  }

  public TaskInstance createEmpty(User user, Task task) {
    TaskInstance newTaskInstance = new TaskInstance(user, "", task);
    newTaskInstance.setCode(task.getCodeStub());
    return taskInstanceRepository.save(newTaskInstance);
  }

  public List<TaskInstance> getByUserAndChallenge(User user, Challenge challenge) {
    List<TaskInstance> taskInstances = new ArrayList<>();
    for (Task task : challenge.getTasks()) {
      TaskInstance current = taskInstanceRepository.findByTaskAndUser(task, user);
      if (current != null) {
        taskInstances.add(current);
      }
    }
    return taskInstances;
  }

  @Transactional
  public boolean rate(int rating, long taskInstanceId) {
    Optional<TaskInstance> taskInstanceOpt = taskInstanceRepository.findById(taskInstanceId);

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

  @Transactional
  public TaskInstance markAsDone(TaskInstance taskInstance) {
    taskInstance.setStatus(CodeStatus.DONE);
    taskInstance.getUser().addPoints(taskInstance.getTask().getPoints());
    return taskInstance;
  }

  public TaskInstance save(TaskInstance taskInstance) {
    return taskInstanceRepository.save(taskInstance);
  }

  public List<TaskInstance> findAll() {
    return (List<TaskInstance>) taskInstanceRepository.findAll();
  }

  public List<TaskInstance> getAllByChallenge(Challenge challenge) {
    List<TaskInstance> taskInstances = new ArrayList<>();
    List<TaskInstance> allTaskInstances = findAll();
    for (TaskInstance current : allTaskInstances) {
      if (current.getTask().getChallenge().getId() == challenge.getId()) {
        taskInstances.add(current);
      }
    }
    return taskInstances;
  }


  public int getNumberOfDoneTaskInstancesInChallenge(Challenge challenge) {
    int count = 0;
    List<TaskInstance> allTaskInstances = findAll();
    for (TaskInstance current : allTaskInstances) {
      if (current.getChallenge().getId() == challenge.getId()
          && current.getStatus() == CodeStatus.DONE) {
        count++;
      }
    }
    return count;
  }

  public TaskInstance getByTaskAndUser(Task task, User user) {
    return taskInstanceRepository.findByTaskAndUser(task, user);
  }

  public TaskInstance getRandomTaskInstance(Task task) {
    List<TaskInstance> viableInstances = taskInstanceRepository.findByTask(task).stream()
        .filter(i -> !i.getUser().equals(userService.getCurrentUser()))
        .filter(i -> i.getStatus() == CodeStatus.DONE)
        .collect(Collectors.toList());
    return viableInstances.get(new Random().nextInt(viableInstances.size()));
  }

  public TaskInstance getUnfinishedInstanceInChallenge(Challenge challenge, User player) {
    Optional<TaskInstance> unfinished = getAllByChallenge(challenge).stream()
        .filter(i -> i.getUser().equals(player))
        .filter(i -> i.getStatus() == CodeStatus.IN_PROGRESS)
        .findFirst();
    if (unfinished.isPresent()) {
      return unfinished.get();
    }
    return null;
  }

  public List<TaskInstance> getHistory() {
    return userService.getCurrentUser().getTaskInstances().stream()
            .sorted(new TaskInstanceTimestampComparator())
            .collect(Collectors.toList());
  }

  public TaskInstance getLastUnfinishedInstance() {
    Optional<TaskInstance> unfinished = getHistory().stream()
            .filter(e -> e.getStatus() == CodeStatus.IN_PROGRESS)
            .findFirst();

    if (unfinished.isPresent()) {
      return unfinished.get();
    } else {
      return null;
    }
  }

  public boolean canPlayOrSkip(TaskInstance taskInstance) {
    return taskInstance.getUser().equals(userService.getCurrentUser())
        && taskInstance.getStatus() == CodeStatus.IN_PROGRESS;
  }

  public class TaskInstanceTimestampComparator implements Comparator<TaskInstance> {
    @Override
    public int compare(TaskInstance t1, TaskInstance t2) {
      if (t1.getCreationTime().after(t2.getCreationTime())) {
        return -1;
      } else {
        return 1;
      }
    }
  }
}
