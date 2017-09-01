package pingis.services.entity;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.Challenge;
import pingis.entities.Task;
import pingis.entities.TaskType;
import pingis.repositories.TaskRepository;

@Service
public class TaskService {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private TaskRepository taskRepository;

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

  public List<Task> findAllByChallenge(Challenge challenge) {
    return taskRepository.findAllByChallenge(challenge);
  }

  public Task findByChallengeAndTypeAndIndex(Challenge challenge, TaskType type, int index) {
    return taskRepository.findByChallengeAndTypeAndIndex(challenge, type, index);
  }


  public int getNumberOfTasks(Challenge challenge) {
    List<Task> tasks = findAllByChallenge(challenge);
    return tasks.size();
  }
}
