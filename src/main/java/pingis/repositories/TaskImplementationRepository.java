/*
 */
package pingis.repositories;

import pingis.entities.TaskInstance;
import org.springframework.data.repository.CrudRepository;
import pingis.entities.Task;
import pingis.entities.User;

public interface TaskImplementationRepository extends CrudRepository<TaskInstance, Long> {
    
//    @Query("delete From Reference r Where r.name = ?1")
//    void deleteReferenceByName(String name);
    TaskInstance findByTaskAndUser(Task task, User user);

}
