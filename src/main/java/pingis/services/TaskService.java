package pingis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.Task;
import pingis.repositories.ChallengeRepository;
import pingis.repositories.TaskRepository;
import java.util.List;
import pingis.repositories.TaskInstanceRepository;
import pingis.entities.TaskInstance;
import pingis.entities.Challenge;

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


    public Task getCorrespondingImplementationTask(TaskInstance taskInstance, Challenge challenge) {
        return taskRepository.findByIndexAndChallenge(taskInstance.getTask().getIndex()+1, challenge);
    }
}
