package pingis.services.logic;

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
import pingis.entities.Task;
import pingis.entities.TaskType;
import pingis.services.entity.ChallengeService;
import pingis.services.entity.TaskService;

@Service
public class ArcadeChallengeService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  @Autowired
  private ChallengeService challengeService;

  @Autowired
  private TaskService taskService;

  public Challenge getArcadeChallenge(Realm realm) {
    return challengeService.findByTypeAndRealm(ChallengeType.ARCADE, realm);
  }

  public Task getRandomImplementationTask(Challenge challenge) {
    List<Task> tasks = taskService.findAllByChallenge(challenge);
    List<Task> implTasks = tasks.stream()
        .filter(e -> e.getType() == TaskType.IMPLEMENTATION).collect(Collectors.toList());
    return implTasks.get(new Random().nextInt(implTasks.size()));
  }
}
