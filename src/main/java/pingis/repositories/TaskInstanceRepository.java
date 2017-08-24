package pingis.repositories;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.User;

public interface TaskInstanceRepository extends CrudRepository<TaskInstance, Long> {

  TaskInstance findByTaskAndUser(Task task, User user);

  List<TaskInstance> findByTask(Task task);
}
