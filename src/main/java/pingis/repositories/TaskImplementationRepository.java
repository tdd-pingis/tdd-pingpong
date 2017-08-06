/*
 */
package pingis.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pingis.entities.Challenge;
import pingis.entities.TaskImplementation;
import org.springframework.data.repository.CrudRepository;
import pingis.entities.ChallengeImplementation;

public interface TaskImplementationRepository extends CrudRepository<TaskImplementation, Long>,
        JpaSpecificationExecutor<TaskImplementation> {
    
//    @Query("delete From Reference r Where r.name = ?1")
//    void deleteReferenceByName(String name);
    

}
