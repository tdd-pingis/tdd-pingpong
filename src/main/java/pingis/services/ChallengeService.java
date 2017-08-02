package pingis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.Challenge;
import pingis.repositories.ChallengeRepository;
import java.util.List;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    @Autowired
    public ChallengeService(ChallengeRepository challengeRepo) {
        this.challengeRepository = challengeRepo;
    }

    public Challenge findOne(Long challengeId) {
        // Implement validation here
        return challengeRepository.findOne(challengeId);
    }

    public Challenge save(Challenge newChallenge) {
        // Implement validation here
        return challengeRepository.save(newChallenge);
    }

    public List<Challenge> findAll() {
        return (List) challengeRepository.findAll();
    }

    public Challenge delete(Long challengeId) {
        //Implement validation here
        Challenge c = findOne(challengeId);
        challengeRepository.delete(challengeId);
        return c;
    }

    public boolean contains(Long challengeId) {
        return challengeRepository.exists(challengeId);
    }

    public Challenge findByName(String name) {
        return challengeRepository.findByName(name);
    }

}
