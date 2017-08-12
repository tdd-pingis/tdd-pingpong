package pingis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.*;
import pingis.repositories.ChallengeRepository;
import pingis.repositories.TaskRepository;
import java.util.List;
import java.util.ArrayList;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import pingis.repositories.TaskInstanceRepository;
import pingis.services.TaskInstanceService;

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

    public Task getCorrespondingTestTask(Task task) {
        return taskRepository.findByIndexAndChallenge(task.getIndex()-1, task.getChallenge());
    }

    public List<Task> getAvailableTestTasks(Challenge challenge) {
        List<Task> availableTestTasks = new ArrayList<Task>();
        List<Task> testTasks = challenge.getTasks();
        for (Task task: testTasks) {
            if (task.getType() == TaskType.TEST) {
                availableTestTasks.add(task);
            }
        }
        return availableTestTasks;
    }

    public MultiValueMap<Task, TaskInstance> getAvailableTestTaskInstances(Challenge challenge) {
        MultiValueMap<Task, TaskInstance> availableTestTaskInstances = new LinkedMultiValueMap();
        List<Task> tasks = challenge.getTasks();
        for (Task task: tasks) {
            if (task.getType() == TaskType.TEST) {
                continue;
            }
            Task testTask = getCorrespondingTestTask(task);
            for (TaskInstance testTaskInstance : testTask.getTaskInstances()) {
                if (testTaskInstance.getStatus() == CodeStatus.DONE) {
                    availableTestTaskInstances.add(task, testTaskInstance);

                }
            }
        }
        return availableTestTaskInstances;
    }

}
