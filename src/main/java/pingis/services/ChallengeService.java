package pingis.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.Challenge;
import pingis.entities.CodeStatus;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.repositories.ChallengeRepository;

@Service
public class ChallengeService {

  private final ChallengeRepository challengeRepository;

  @Autowired
  private TaskService taskService;

  @Autowired
  private UserService userService;

  @Autowired
  private TaskInstanceService taskInstanceService;

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
    return (List<Challenge>) challengeRepository.findAll();
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


  public TaskInstance getUnfinishedTaskInstance(Challenge challenge) {
    List<TaskInstance> taskInstances = taskInstanceService.getAllByChallenge(challenge);
    for (TaskInstance current : taskInstances) {
      if (current.getStatus() == CodeStatus.IN_PROGRESS) {
        return current;
      }
    }
    return null;
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





}
