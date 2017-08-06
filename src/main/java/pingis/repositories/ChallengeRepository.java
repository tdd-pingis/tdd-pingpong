package pingis.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pingis.entities.Challenge;
import org.springframework.data.repository.CrudRepository;

public interface ChallengeRepository extends CrudRepository<Challenge, Long>, JpaSpecificationExecutor<Challenge> {

    Challenge findByName(String name);

}
