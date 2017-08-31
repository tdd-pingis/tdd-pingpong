package pingis.services.logic;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.TaskInstance;
import pingis.services.entity.ChallengeService;
import pingis.services.entity.UserService;

@Service
public class DashboardService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private ChallengeService challengeService;

  @Autowired
  private UserService userService;

  public List<Challenge> getAvailableChallenges(MultiValueMap<Challenge, TaskInstance>
      myTasksInChallenges) {
    List<Challenge> availableChallenges = challengeService.findAll().stream()
        .filter(e -> !e.getIsOpen())
        .filter(e -> e.getLevel() <= userService.levelOfCurrentUser())
        .filter(e -> e.getType() != ChallengeType.ARCADE)
        .filter(e -> !myTasksInChallenges.containsKey(e))
        .collect(Collectors.toList());

    return availableChallenges;
  }

  public Challenge getParticipatingLiveChallenge() {
    Optional<Challenge> findFirst = challengeService.findAll().stream()
        .filter(e -> e.getIsOpen())
        .filter(e -> challengeService.isParticipating(e, userService.getCurrentUser()))
        .findFirst();

    if (findFirst.isPresent()) {
      return findFirst.get();
    } else {
      return null;
    }
  }
}
