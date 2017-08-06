/*
 */
package pingis.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pingis.entities.Challenge;
import pingis.entities.ChallengeImplementation;
import pingis.entities.User;

public interface ChallengeImplementationRepository extends CrudRepository<ChallengeImplementation, Long>,
        JpaSpecificationExecutor<ChallengeImplementation> {
    
//    @Query("delete From Reference r Where r.name = ?1")
//    void deleteReferenceByName(String name);
    //@Query("select * from ChallengeImplementation i where i.testUser = ?1 or i.implementationUser = ?1")
    //List<ChallengeImplementation> findAllActiveChallengeImplementations(User user);
    

}
