package pingis.repositories;

import org.springframework.data.repository.CrudRepository;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.Realm;

public interface ChallengeRepository extends CrudRepository<Challenge, Long> {

  Challenge findByName(String name);

  Challenge findByTypeAndRealm(ChallengeType type, Realm realm);
}
