package pingis.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import pingis.entities.Challenge;
import pingis.entities.Task;
import pingis.entities.TaskType;

public interface TaskRepository extends CrudRepository<Task, Long> {

  Task findByIndexAndChallenge(int index, Challenge challenge);

  List<Task> findAllByChallenge(Challenge challenge);

  Task findByIndexAndChallengeAndType(int index, Challenge challenge, TaskType type);

  List<Task> findAllByChallengeAndIndex(Challenge challenge, int index);

  Task findByChallengeAndTypeAndIndex(Challenge challenge, TaskType type, int index);

}
