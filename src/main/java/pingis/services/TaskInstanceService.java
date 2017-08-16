
package pingis.services;

import java.util.ArrayList;
import java.util.List;
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


  public TaskInstance getCorrespondingTestTaskInstance(
      TaskInstance implTaskInstance) {
    return taskInstanceRepository
        .findByTaskAndUser(
            taskRepository
                .findByIndexAndChallengeAndType(implTaskInstance.getTask().getIndex(),
                    implTaskInstance.getTask().getChallenge(), TaskType.TEST),
            userRepository.findOne(0L));
  }


  public TaskInstance getCorrespondingImplTaskInstance(TaskInstance testTaskInstance) {
    return taskInstanceRepository
        .findByTaskAndUser(
            taskRepository.findByIndexAndChallengeAndType(testTaskInstance.getTask().getIndex(),
                testTaskInstance.getTask().getChallenge(), TaskType.IMPLEMENTATION),
            userRepository.findOne(0L));
  }

  public TaskInstance findOne(long taskInstanceId) {
    return taskInstanceRepository.findOne(taskInstanceId);

  }

  @Transactional
  public TaskInstance updateTaskInstanceCode(Long taskInstanceId, String taskInstanceCode) {
    TaskInstance taskInstanceToUpdate = taskInstanceRepository.findOne(taskInstanceId);
    taskInstanceToUpdate.setCode(taskInstanceCode);
    return taskInstanceToUpdate;
  }

  public TaskInstance createEmpty(User user, Task task) {
    TaskInstance newTaskInstance = new TaskInstance(user, "", task);
    newTaskInstance.setCode(task.getCodeStub());
    return taskInstanceRepository.save(newTaskInstance);
  }
  
  public List<TaskInstance> getByUserAndTask(User user, Challenge challenge) {
    List<TaskInstance> taskInstances = new ArrayList<>();
    for (Task task : challenge.getTasks()) {
      taskInstances.add(taskInstanceRepository.findByTaskAndUser(task, user));
    }
    return taskInstances;
  }


  @Transactional
  public TaskInstance markAsDone(TaskInstance taskInstance) {
    taskInstance.setStatus(CodeStatus.DONE);
    return taskInstance;
  }

  public TaskInstance save(TaskInstance taskInstance) {
    return taskInstanceRepository.save(taskInstance);
  }

  public List<TaskInstance> findAll() {
    return (List<TaskInstance>) taskInstanceRepository.findAll();
  }
}
