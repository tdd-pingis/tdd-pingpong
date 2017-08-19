package pingis.services;

import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.Challenge;
import pingis.repositories.ChallengeRepository;

@Service
public class ChallengeService {

  private final ChallengeRepository challengeRepository;

  @Autowired
  public ChallengeService(ChallengeRepository challengeRepo) {
    this.challengeRepository = challengeRepo;
  }

  public Challenge findOne(Long challengeId) {
    // Implement validation here
    return challengeRepository.findById(challengeId).get();
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
    challengeRepository.deleteById(challengeId);
    return c;
  }

  public Challenge getRandomChallenge() {
    List<Challenge> challenges = findAll();
    return challenges.get(new Random().nextInt(challenges.size()));
  }

  public boolean contains(Long challengeId) {
    return challengeRepository.existsById(challengeId);
  }

  public Challenge findByName(String name) {
    return challengeRepository.findByName(name);
  }

}
