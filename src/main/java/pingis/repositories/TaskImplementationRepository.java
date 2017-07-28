/*
 */
package pingis.repositories;

import java.util.List;
import pingis.entities.Challenge;
import pingis.entities.TaskImplementation;
import org.springframework.data.repository.CrudRepository;

public interface TaskImplementationRepository extends CrudRepository<TaskImplementation, Long> {
    
//    @Query("delete From Reference r Where r.name = ?1")
//    void deleteReferenceByName(String name);
    

}
