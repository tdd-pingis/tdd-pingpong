package pingis.services.entity;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.Realm;
import pingis.entities.User;
import pingis.repositories.ChallengeRepository;

@Service
public class ChallengeService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final ChallengeRepository challengeRepository;


  @Autowired
  public ChallengeService(ChallengeRepository challengeRepo) {
    this.challengeRepository = challengeRepo;
  }

  public Challenge findOne(Long challengeId) {
    // TODO: Implement validation here
    return challengeRepository.findById(challengeId).get();
  }

  public Challenge save(Challenge newChallenge) {
    // TODO: Implement validation here
    return challengeRepository.save(newChallenge);
  }

  public List<Challenge> findAll() {
    return (List<Challenge>) challengeRepository.findAll();
  }

  public Challenge delete(Long challengeId) {
    // TODO: Implement validation here
    Challenge c = findOne(challengeId);
    challengeRepository.deleteById(challengeId);
    return c;
  }

  public boolean contains(Long challengeId) {
    return challengeRepository.existsById(challengeId);
  }

  public Challenge findByName(String name) {
    return challengeRepository.findByName(name);
  }


  public Challenge getRandomLiveChallenge(User user) {
    List<Challenge> liveChallenges = findAll().stream()
        .filter(e -> e.getIsOpen())
        .filter(e -> e.getSecondPlayer() == null)
        .filter(e -> e.getAuthor() != user)
        .collect(Collectors.toList());
    if (liveChallenges.size() > 0) {
      return liveChallenges.get(new Random().nextInt(liveChallenges.size()));
    } else {
      return null;
    }
  }

  public void closeChallenge(Challenge currentChallenge) {
    currentChallenge.setOpen(false);
    save(currentChallenge);
    logger.debug("Closed challenge {}", currentChallenge.getId());
  }

  public Challenge findByTypeAndRealm(ChallengeType type, Realm realm) {
    return challengeRepository.findByTypeAndRealm(type, realm);
  }

  public boolean isParticipating(Challenge challenge, User user) {
    if (user.getId() == challenge.getAuthor().getId()) {
      return true;
    } else if (challenge.getSecondPlayer() == null) {
      return false;
    }

    return (user.getId() == challenge.getSecondPlayer().getId());
  }
}
