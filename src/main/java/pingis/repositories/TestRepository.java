/*
 */
package pingis.repositories;

import java.util.List;
import pingis.entities.Test;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<Test, Long> {
    
//    @Query("delete From Reference r Where r.name = ?1")
//    void deleteReferenceByName(String name);
    

}
