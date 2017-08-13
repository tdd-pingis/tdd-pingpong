package pingis.repositories;

import org.springframework.data.repository.CrudRepository;
import pingis.entities.Challenge;
import pingis.entities.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {

  Task findByIndexAndChallenge(int index, Challenge challenge);
}
