package pingis.services;

import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.Task;
import pingis.repositories.ChallengeRepository;
import pingis.repositories.TaskRepository;
import java.util.List;
import static org.springframework.data.jpa.domain.Specifications.where;
import org.springframework.ui.Model;
import pingis.entities.Challenge;
import pingis.entities.ChallengeImplementation;
import pingis.entities.ImplementationType;
import pingis.entities.QuerySpecifications;
import static pingis.entities.QuerySpecifications.*;
import pingis.entities.TaskImplementation;
import pingis.repositories.TaskImplementationRepository;
import pingis.utils.EditorTabData;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private TaskImplementationRepository taskImplementationRepository;

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

    public TaskImplementation getCorrespondingTestTaskImplementation(
            TaskImplementation implTaskImplementation,
            Challenge challenge) {
        return taskImplementationRepository.
                findOne(where(QuerySpecifications.hasTask(taskRepository
                .findOne(where(hasIndex(implTaskImplementation.getTask().getIndex() - 1))
                        .and(hasChallenge(challenge))))));
    }

}
