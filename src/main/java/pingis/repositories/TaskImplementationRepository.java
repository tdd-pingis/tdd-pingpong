/*
 */
package pingis.repositories;

import pingis.entities.TaskImplementation;
import org.springframework.data.repository.CrudRepository;
import pingis.entities.Task;
import pingis.entities.User;

public interface TaskImplementationRepository extends CrudRepository<TaskImplementation, Long> {
    
//    @Query("delete From Reference r Where r.name = ?1")
//    void deleteReferenceByName(String name);
    TaskImplementation findByTaskAndUser(Task task, User user);

}
