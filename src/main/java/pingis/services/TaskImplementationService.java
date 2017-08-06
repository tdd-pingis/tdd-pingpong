
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

@Service
public class TaskImplementationService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private TaskImplementationRepository taskImplementationRepository;

    public TaskImplementation getCorrespondingTestTaskImplementation(
            TaskImplementation implTaskImplementation) {
        return taskImplementationRepository.
                findOne(where(QuerySpecifications.hasTask(taskRepository
                        .findOne(where(hasIndex(implTaskImplementation.getTask().getIndex() - 1))
                                .and(hasChallenge(implTaskImplementation.getTask().getChallenge()))))));
    }

    public TaskImplementation findOne(long taskImplementationId) {
        return taskImplementationRepository.findOne(taskImplementationId);

    }
}
