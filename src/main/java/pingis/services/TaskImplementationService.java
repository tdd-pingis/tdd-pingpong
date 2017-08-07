
package pingis.services;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.data.jpa.domain.Specifications.where;
import org.springframework.stereotype.Service;
import pingis.entities.Challenge;
import pingis.entities.QuerySpecifications;
import static pingis.entities.QuerySpecifications.hasChallenge;
import static pingis.entities.QuerySpecifications.hasIndex;
import pingis.entities.TaskImplementation;
import pingis.repositories.ChallengeRepository;
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
                findOne(where(QuerySpecifications.hasTask(taskRepository
                        .findOne(where(hasIndex(implTaskImplementation.getTask()
                                .getIndex() - 1))
                                .and(hasChallenge(implTaskImplementation.getTask()
                                        .getChallenge()))))) // ugly hack to find modeluser, please fix
                        .and(QuerySpecifications.hasUser(userRepository.findOne(0l))));
    }
    
    public TaskImplementation getCorrespondingImplTaskImplementation(TaskImplementation testTaskImplementation) {
        return taskImplementationRepository.
                findOne(where(QuerySpecifications.hasTask(taskRepository
                        .findOne(where(hasIndex(testTaskImplementation.getTask().getIndex() + 1))
                                .and(hasChallenge(testTaskImplementation.getTask().getChallenge())))))
                .and(QuerySpecifications.hasUser(userRepository.findOne(0l))));
    }

    public TaskImplementation findOne(long taskImplementationId) {
        return taskImplementationRepository.findOne(taskImplementationId);

    }
}
