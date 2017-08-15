package pingis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pingis.entities.Challenge;
import pingis.entities.CodeStatus;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.repositories.ChallengeRepository;
import pingis.repositories.TaskInstanceRepository;
import pingis.repositories.TaskRepository;

@Service
public class TaskService {

  @Autowired
  private TaskRepository taskRepository;
  @Autowired
  private ChallengeRepository challengeRepository;
  @Autowired
  private TaskInstanceRepository taskInstanceRepository;

  public Task findTaskInChallenge(Long challengeId, int taskId) {
    // Implement validation here
    return challengeRepository.findOne(challengeId).getTasks().get(taskId);
  }

  public Task findOne(Long taskId) {
    // Implement validation here
    return taskRepository.findOne(taskId);
  }

  public Task save(Task newTask) {
    // Implement validation here
    return taskRepository.save(newTask);
  }

  public List<Task> findAll() {
    return (List) taskRepository.findAll();
  }

  public Task delete(Long taskId) {
    //Implement validation here
    Task t = findOne(taskId);
    taskRepository.delete(taskId);
    return t;
  }

  public boolean contains(Long taskId) {
    return taskRepository.exists(taskId);
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
    MultiValueMap<Task, TaskInstance> availableTestTaskInstances = new LinkedMultiValueMap();
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

}
