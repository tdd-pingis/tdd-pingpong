package pingis.repositories;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import pingis.entities.Challenge;
import pingis.entities.Task;
import pingis.entities.TaskType;

public interface TaskRepository extends CrudRepository<Task, Long> {

  Task findByIndexAndChallenge(int index, Challenge challenge);

  List<Task> findAllByChallenge(Challenge challenge);

  Task findByIndexAndChallengeAndType(int index, Challenge challenge, TaskType type);

}
