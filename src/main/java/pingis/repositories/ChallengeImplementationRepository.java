/*
 */
package pingis.repositories;


import org.springframework.data.repository.CrudRepository;
import pingis.entities.ChallengeImplementation;

public interface ChallengeImplementationRepository extends CrudRepository<ChallengeImplementation, Long> {
    
//    @Query("delete From Reference r Where r.name = ?1")
//    void deleteReferenceByName(String name);
    

}
