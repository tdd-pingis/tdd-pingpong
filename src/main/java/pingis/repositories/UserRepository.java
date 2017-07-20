/*
 */
package pingis.repositories;

import java.util.List;
import pingis.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    
//    @Query("delete From Reference r Where r.name = ?1")
//    void deleteReferenceByName(String name);
    

}
