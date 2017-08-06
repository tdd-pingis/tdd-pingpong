/*
 */
package pingis.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pingis.entities.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    
//    @Query("delete From Reference r Where r.name = ?1")
//    void deleteReferenceByName(String name);
    

}
