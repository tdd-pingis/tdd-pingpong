package pingis.repositories;

import java.util.List;
import pingis.entities.Challenge;
import org.springframework.data.repository.CrudRepository;

public interface ChallengeRepository extends CrudRepository<Challenge, Long> {
}
