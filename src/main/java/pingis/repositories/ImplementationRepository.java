/*
 */
package pingis.repositories;

import java.util.List;
import pingis.entities.Challenge;
import pingis.entities.Implementation;
import org.springframework.data.repository.CrudRepository;

public interface ImplementationRepository extends CrudRepository<Implementation, Long> {
    
//    @Query("delete From Reference r Where r.name = ?1")
//    void deleteReferenceByName(String name);
    

}
