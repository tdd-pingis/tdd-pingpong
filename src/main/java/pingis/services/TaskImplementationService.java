
package pingis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pingis.entities.TaskInstance;
import pingis.repositories.TaskImplementationRepository;
import pingis.repositories.TaskRepository;
import pingis.repositories.UserRepository;

@Service
public class TaskImplementationService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskImplementationRepository taskImplementationRepository;
    @Autowired
    private UserRepository userRepository;


    
    public TaskInstance getCorrespondingTestTaskInstance(
            TaskInstance implTaskInstance) {
        return taskImplementationRepository.
                findByTaskAndUser(
                        taskRepository.
                                findByIndexAndChallenge(implTaskInstance.getTask().getIndex()-1,
                                        implTaskInstance.getTask().getChallenge()),
                        userRepository.findOne(0l));
    }
    

    
    public TaskInstance getCorrespondingImplTaskInstance(TaskInstance testTaskInstance) {
        return taskImplementationRepository.
                findByTaskAndUser(
                        taskRepository.findByIndexAndChallenge(testTaskInstance.getTask().getIndex()+1,
                                testTaskInstance.getTask().getChallenge()),
                        userRepository.findOne(0l));
    }

    public TaskInstance findOne(long taskInstanceId) {
        return taskImplementationRepository.findOne(taskInstanceId);

    }

    @Transactional
    public TaskInstance updateTaskImplementationCode(Long taskInstanceId, String taskInstanceCode) {
        TaskInstance taskInstanceToUpdate = taskImplementationRepository.findOne(taskInstanceId);
        taskInstanceToUpdate.setCode(taskInstanceCode);
        return taskInstanceToUpdate;
    }
}
