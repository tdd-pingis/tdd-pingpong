package pingis.services.entity;

import static pingis.entities.Task.LEVEL_MAX_VALUE;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pingis.entities.Challenge;
import pingis.entities.CodeStatus;
import pingis.entities.TaskInstance;
import pingis.entities.TmcUserDto;
import pingis.entities.User;
import pingis.repositories.UserRepository;

@Service
public class UserService {

  private final Logger logger = Logger.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  public User findOne(Long userId) {
    // Implement validation here
    Optional<User> opt = userRepository.findById(userId);
    if (opt.isPresent()) {
      return opt.get();
    }
    return null;
  }

  public User save(User user) {
    // Implement validation here
    return userRepository.save(user);
  }

  public User handleOAuthUserAuthentication(TmcUserDto authUser) {
    User user = findOne(Long.parseLong(authUser.getId()));
    if (user == null) {
      user = initializeUser(authUser);
      logger.info("New OauthUser detected and initialized.");
    }

    logger.info("OauthUser successfully authenticated.");
    return user;
  }

  public User handleUserAuthenticationByName(String userName) {
    User user = userRepository.findByName(userName);
    if (user == null) {
      user = new User(userName);
      userRepository.save(user);
      logger.info("New TMCuser detected and initialized.");
    }

    logger.info("TMCuser successfully authenticated.");
    return user;
  }

  public User initializeUser(TmcUserDto newUser) {
    // Implement validation here
    return userRepository.save(new User(Long.parseLong(newUser.getId()),
        newUser.getName(),
        1,
        newUser.isAdministrator()));
  }

  public List<User> findAll() {
    return (List<User>) userRepository.findAll();
  }

  public void delete(User user) {
    //Implement validation here
    userRepository.delete(user);
  }

  public void deleteMultiple(List<User> users) {
    //Implement validation here
    for (User user : users) {
      userRepository.delete(user);
    }
  }

  public boolean contains(Long userId) {
    return userRepository.existsById(userId);
  }

  public User findByName(String name) {
    return userRepository.findByName(name);
  }

  public User getCurrentUser() {
    return findByName(SecurityContextHolder.getContext().getAuthentication().getName());
  }

  public int getLevel(int score) {
    return Math.min(score / 1000, LEVEL_MAX_VALUE);
  }

  public int levelOfCurrentUser() {
    return getLevel(getCurrentUser().getPoints());
  }


  public List<TaskInstance> getHistory(TaskInstanceService taskInstanceService) {
    return getCurrentUser().getTaskInstances().stream()
            .sorted(taskInstanceService.new TaskInstanceTimestampComparator())
            .collect(Collectors.toList());
  }

  public TaskInstance getLastUnfinishedInstance(TaskInstanceService taskInstanceService) {
    Optional<TaskInstance> unfinished = getHistory(taskInstanceService).stream()
            .filter(e -> e.getStatus() == CodeStatus.IN_PROGRESS)
            .findFirst();

    if (unfinished.isPresent()) {
      return unfinished.get();
    } else {
      return null;
    }
  }

  public MultiValueMap<Challenge, TaskInstance> getCompletedTaskInstancesInUnfinishedChallenges(
      User user) {
    MultiValueMap<Challenge, TaskInstance> myTasksInChallenges = new LinkedMultiValueMap<>();
    user.getTaskInstances().stream()
        .filter(e -> !e.getChallenge().getIsOpen())
        .filter(e -> e.getStatus().equals(CodeStatus.DONE))
        .forEach(e -> myTasksInChallenges.add(e.getChallenge(), e));

    myTasksInChallenges.keySet().stream()
        .filter(e -> user.getCompletedChallenges().contains(e))
        .forEach(e -> myTasksInChallenges.remove(e));

    return myTasksInChallenges;
  }
}
