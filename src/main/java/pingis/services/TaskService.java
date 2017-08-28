package pingis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.CodeStatus;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.repositories.ChallengeRepository;
import pingis.repositories.TaskRepository;

@Service
public class TaskService {

  @Autowired
  private TaskRepository taskRepository;
  @Autowired
  private ChallengeRepository challengeRepository;
  @Autowired
  private TaskInstanceService taskInstanceService;
  @Autowired
  private UserService userService;

  public Task findTaskInChallenge(Long challengeId, int taskId) {
    // Implement validation here
    return challengeRepository.findById(challengeId).get().getTasks().get(taskId);
  }

  public Task findOne(Long taskId) {
    // Implement validation here
    return taskRepository.findById(taskId).get();
  }

  public Task save(Task newTask) {
    // Implement validation here
    return taskRepository.save(newTask);
  }

  public List<Task> findAll() {
    return (List<Task>) taskRepository.findAll();
  }

  public Task delete(Long taskId) {
    //Implement validation here
    Task t = findOne(taskId);
    taskRepository.deleteById(taskId);
    return t;
  }

  public boolean contains(Long taskId) {
    return taskRepository.existsById(taskId);
  }

  //Relies on the ordering of task types, may not always be the case...
  public Task getCorrespondingTask(Task task) {
    if (task.getType() == TaskType.TEST) {
      return taskRepository.findByIndexAndChallengeAndType(task.getIndex(),
          task.getChallenge(),
          TaskType.IMPLEMENTATION);
    } else {
      return taskRepository.findByIndexAndChallengeAndType(task.getIndex(),
          task.getChallenge(),
          TaskType.TEST);
    }
  }

  public List<Task> filterTasksByUser(List<Task> tasks, User user) {
    tasks = tasks.stream().filter((t) -> {
      for (TaskInstance ti : user.getTaskInstances()) {
        Task doneTask = ti.getTask();
        if (doneTask.getId() == t.getId()
                || getCorrespondingTask(doneTask).getId() == t.getId()) {
          return false;
        }
      }
      return true;
    }).collect(Collectors.toList());
    return tasks;
  }

  public List<Task> getAvailableTasksByType(Challenge challenge, TaskType taskType) {
    List<Task> availableTestTasks = new ArrayList<Task>();
    List<Task> testTasks = challenge.getTasks();
    for (Task task : testTasks) {
      if (task.getType() == taskType) {
        availableTestTasks.add(task);
      }
    }
    return availableTestTasks;
  }

  //TODOL refactoring
  public MultiValueMap<Task, TaskInstance> getAvailableTestTaskInstances(
          Challenge challenge, User user) {
    MultiValueMap<Task, TaskInstance> availableTestTaskInstances = new LinkedMultiValueMap<>();
    List<Task> implTasks = getAvailableTasksByType(challenge, TaskType.IMPLEMENTATION);
    for (Task task : implTasks) {
      Task testTask = getCorrespondingTask(task);
      for (TaskInstance testTaskInstance : testTask.getTaskInstances()) {
        if (testTaskInstance.getStatus() == CodeStatus.DONE
                && user.getId() != testTaskInstance.getUser().getId()) {
          availableTestTaskInstances.add(task, testTaskInstance);
        }
      }
    }
    return availableTestTaskInstances;
  }

  public List<Task> findAllByChallenge(Challenge challenge) {
    return taskRepository.findAllByChallenge(challenge);
  }

  public Task getRandomTestTask(Challenge currentChallenge, User currentUser) {
    List<Task> testTasks = filterTasksByUser(
        getAvailableTasksByType(currentChallenge, TaskType.TEST),
        currentUser);
    return getRandomElementFromList(testTasks);
  }

  public Task getRandomImplTask(Challenge currentChallenge, User currentUser) {
    MultiValueMap<Task, TaskInstance> implementationTasks = getAvailableTestTaskInstances(
        currentChallenge,
        currentUser);
    List<Task> keys = new ArrayList<>();
    keys.addAll(implementationTasks.keySet());
    return getRandomElementFromList(keys);
  }

  public TaskInstance getRandomTaskInstance(Challenge currentChallenge, User currentUser,
      Task whichTask) {
    MultiValueMap<Task, TaskInstance> allTaskInstances = getAvailableTestTaskInstances(
        currentChallenge, currentUser);
    return getRandomElementFromList(allTaskInstances.get(whichTask));
  }

  public TaskType getRandomTaskType() {
    List<TaskType> taskTypes = new ArrayList<>();
    taskTypes.add(TaskType.IMPLEMENTATION);
    taskTypes.add(TaskType.TEST);
    return getRandomElementFromList(taskTypes);
  }

  public boolean noNextTaskAvailable(Challenge currentChallenge, User currentUser) {
    return !hasNextImplTaskAvailable(currentChallenge, currentUser)
        && !hasNextTestTaskAvailable(currentChallenge, currentUser);
  }

  public boolean hasNextTestTaskAvailable(Challenge currentChallenge, User currentUser) {
    return getRandomTestTask(currentChallenge, currentUser) != null;
  }

  public boolean hasNextImplTaskAvailable(Challenge currentChallenge, User currentUser) {
    return getRandomImplTask(currentChallenge, currentUser) != null;
  }

  private <T> T getRandomElementFromList(List<T> list) {
    if (list.isEmpty()) {
      return null;
    }
    return list.get(new Random().nextInt(list.size()));
  }

  public List<Task> findAllByChallengeAndIndex(Challenge challenge, int index) {
    return taskRepository.findAllByChallengeAndIndex(challenge, index);
  }

  public Task findByChallengeAndTypeAndIndex(Challenge challenge, TaskType type, int index) {
    return taskRepository.findByChallengeAndTypeAndIndex(challenge, type, index);
  }

  public Task nextPracticeTask(Challenge challenge) {
    int highestIndex = findAllByChallenge(challenge).size() / 2;
    User player = userService.getCurrentUser();
    List<TaskInstance> instances = taskInstanceService.getByUserAndChallenge(player, challenge);
    if (instances.size() == highestIndex) {
      return null;
    }
    if (instances.size() == 0) {
      return new Random().nextInt(1) == 0
          ? findByChallengeAndTypeAndIndex(challenge, TaskType.TEST, 1)
          : findByChallengeAndTypeAndIndex(challenge, TaskType.IMPLEMENTATION, 1);
    }
    Task testTask = findByChallengeAndTypeAndIndex(challenge, TaskType.TEST, instances.size());
    if (taskInstanceService.getByTaskAndUser(testTask, player) != null) {
      return findByChallengeAndTypeAndIndex(challenge, TaskType.IMPLEMENTATION,
          instances.size() + 1);
    } else {
      return findByChallengeAndTypeAndIndex(challenge, TaskType.TEST, instances.size() + 1);
    }
  }
}
