package pingis.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.Challenge;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.User;
import pingis.repositories.ChallengeRepository;
import pingis.services.TaskInstanceService;
import pingis.services.TaskService;

@Service
public class ChallengeService {

  private final ChallengeRepository challengeRepository;

  @Autowired
  private TaskService taskService;

  @Autowired
  private TaskInstanceService taskInstanceService;

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

  public int getNumberOfTasks(Challenge challenge) {
    List<Task> tasks = taskService.findAllByChallenge(challenge);
    return tasks.size();
  }
  

}
