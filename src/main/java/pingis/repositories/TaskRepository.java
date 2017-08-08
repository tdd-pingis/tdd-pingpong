/*
 */
package pingis.repositories;

import pingis.entities.Task;
import org.springframework.data.repository.CrudRepository;
import pingis.entities.Challenge;

public interface TaskRepository extends CrudRepository<Task, Long> {
    
//    @Query("delete From Reference r Where r.name = ?1")
//    void deleteReferenceByName(String name);
    Task findByIndexAndChallenge(int index, Challenge challenge);

}
