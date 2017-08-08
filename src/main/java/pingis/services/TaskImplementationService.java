
package pingis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pingis.entities.TaskImplementation;
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


    
    public TaskImplementation getCorrespondingTestTaskImplementation(
            TaskImplementation implTaskImplementation) {
        return taskImplementationRepository.
                findByTaskAndUser(
                        taskRepository.
                                findByIndexAndChallenge(implTaskImplementation.getTask().getIndex()-1,
                                        implTaskImplementation.getTask().getChallenge()),
                        userRepository.findOne(0l));
    }
    

    
    public TaskImplementation getCorrespondingImplTaskImplementation(TaskImplementation testTaskImplementation) {
        return taskImplementationRepository.
                findByTaskAndUser(
                        taskRepository.findByIndexAndChallenge(testTaskImplementation.getTask().getIndex()+1,
                                testTaskImplementation.getTask().getChallenge()),
                        userRepository.findOne(0l));
    }

    public TaskImplementation findOne(long taskImplementationId) {
        return taskImplementationRepository.findOne(taskImplementationId);

    }

    @Transactional
    public TaskImplementation updateTaskImplementationCode(Long taskImplementationId, String taskImplementationCode) {
        TaskImplementation taskImplementationToUpdate = taskImplementationRepository.findOne(taskImplementationId);
        taskImplementationToUpdate.setCode(taskImplementationCode);
        return taskImplementationToUpdate;
    }
}
