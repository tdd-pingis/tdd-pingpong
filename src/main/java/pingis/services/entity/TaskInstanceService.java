package pingis.services.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pingis.entities.Challenge;
import pingis.entities.CodeStatus;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.User;
import pingis.repositories.TaskInstanceRepository;

@Service
public class TaskInstanceService {

  @Autowired
  private TaskInstanceRepository taskInstanceRepository;

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

  public TaskInstance findByTaskAndUser(Task task, User user) {
    return taskInstanceRepository.findByTaskAndUser(task, user);
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

  public TaskInstance getUnfinishedTaskInstance(Challenge challenge) {
    List<TaskInstance> taskInstances = getAllByChallenge(challenge);
    for (TaskInstance current : taskInstances) {
      if (current.getStatus() == CodeStatus.IN_PROGRESS) {
        return current;
      }
    }
    return null;
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

  public List<TaskInstance> findByTask(Task task) {
    return taskInstanceRepository.findByTask(task);
  }

  public Optional<TaskInstance> findById(Long id) {
    return taskInstanceRepository.findById(id);
  }
}
