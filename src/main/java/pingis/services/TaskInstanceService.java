
package pingis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pingis.entities.CodeStatus;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
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
        return taskInstanceRepository.
                findByTaskAndUser(
                        taskRepository.
                                findByIndexAndChallenge(implTaskInstance.getTask().getIndex()-1,
                                        implTaskInstance.getTask().getChallenge()),
                        userRepository.findOne(0l));
    }
    

    
    public TaskInstance getCorrespondingImplTaskInstance(TaskInstance testTaskInstance) {
        return taskInstanceRepository.
                findByTaskAndUser(
                        taskRepository.findByIndexAndChallenge(testTaskInstance.getTask().getIndex()+1,
                                testTaskInstance.getTask().getChallenge()),
                        userRepository.findOne(0l));
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
        return taskInstanceRepository.save(newTaskInstance);
    }

    @Transactional
    public TaskInstance markAsDone(TaskInstance taskInstance) {
        taskInstance.setStatus(CodeStatus.DONE);
        return taskInstance;
    }

    public TaskInstance save(TaskInstance taskInstance) {
        return taskInstanceRepository.save(taskInstance);
    }
}
