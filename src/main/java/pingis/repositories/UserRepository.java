/*
 */
package pingis.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pingis.entities.User;
import org.springframework.data.repository.CrudRepository;
import pingis.entities.TaskImplementation;

public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {
    
//    @Query("delete From Reference r Where r.name = ?1")
//    void deleteReferenceByName(String name);
    

}
