package pingis.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.CodeStatus;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskPair;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.repositories.ChallengeRepository;
import pingis.utils.CodeStubBuilder;
import pingis.utils.TestStubBuilder;

@Service
public class ChallengeService {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());


  private final ChallengeRepository challengeRepository;

  @Autowired
  private UserService userService;
  @Autowired
  private TaskInstanceService taskInstanceService;

  @Autowired
  private GameplayService gameplayService;



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

  public boolean isOwnChallenge(Challenge challenge, User player) {
    return challenge.getAuthor().equals(player)
            || (challenge.getSecondPlayer() != null
            && challenge.getSecondPlayer().equals(player));
  }

  public List<Challenge> getAvailableChallenges(MultiValueMap<Challenge, TaskInstance>
                                              myTasksInChallenges) {
    List<Challenge> availableChallenges = findAll().stream()
            .filter(e -> !e.getIsOpen())
            .filter(e -> e.getLevel() <= userService.levelOfCurrentUser())
            .filter(e -> e.getType() != ChallengeType.ARCADE)
            .filter(e -> !myTasksInChallenges.containsKey(e))
            .collect(Collectors.toList());

    return availableChallenges;
  }

  public MultiValueMap<Challenge, TaskInstance> getCompletedTaskInstancesInUnfinishedChallenges() {
    MultiValueMap<Challenge, TaskInstance> myTasksInChallenges = new LinkedMultiValueMap<>();
    userService.getCurrentUser().getTaskInstances().stream()
            .filter(e -> !e.getChallenge().getIsOpen())
            .filter(e -> e.getStatus().equals(CodeStatus.DONE))
            .forEach(e -> myTasksInChallenges.add(e.getChallenge(), e));

    myTasksInChallenges.keySet().stream()
            .filter(e -> userService.getCurrentUser().getCompletedChallenges().contains(e))
            .forEach(e -> myTasksInChallenges.remove(e));

    return myTasksInChallenges;
  }

  public void createTaskPair(Challenge currentChallenge, TaskPair taskPair) {
    logger.debug("Creating new task pair");
    logger.debug("Generating new task pair and instance");

    int highestIndex = taskService.findAllByChallenge(currentChallenge).size() / 2;
    logger.info("Challenge ID: " + currentChallenge.getId());
    logger.info("Challenge type: " + currentChallenge.getType());
    logger.info("Highest index: " + highestIndex);
    String testStub = "";
    String implStub = "";

    // Autogenerate code stubs
    if (currentChallenge.getType() == ChallengeType.MIXED
        || currentChallenge.getType() == ChallengeType.ARCADE
        || (currentChallenge.getType() == ChallengeType.PROJECT
        && highestIndex == 0)) {
      logger.info("generating code stubs");
      implStub = new CodeStubBuilder(taskPair.getClassName()).build().code;
      testStub = new TestStubBuilder(implStub).withTestImports().build().code;
    } else {
      User player = userService.getCurrentUser();
      User otherPlayer = currentChallenge.getAuthor().equals(player)
          ? currentChallenge.getSecondPlayer() : currentChallenge.getAuthor();
      // Challenge is a project with at least one existing task instance pair.
      // Inheriting code from previous instance pair.
      logger.info("inheriting code stubs from previous task pair");
      testStub = taskInstanceService.getByTaskAndUser(
          taskService.findByChallengeAndTypeAndIndex(currentChallenge, TaskType.TEST, highestIndex),
          otherPlayer)
          .getCode();
      implStub = taskInstanceService.getByTaskAndUser(
          taskService.findByChallengeAndTypeAndIndex(currentChallenge,
              TaskType.IMPLEMENTATION,
              highestIndex),
          player)
          .getCode();
    }

    taskPair.setImplementationCodeStub(implStub);
    taskPair.setTestCodeStub(testStub);

    logger.debug("Generating new task pair and instance");

    // NotLikeThis
    gameplayService.generateTaskPairAndTaskInstance(taskPair.getTestTaskName(),
        taskPair.getTestTaskName(),
        taskPair.getTestTaskDesc(),
        taskPair.getTestTaskDesc(),
        taskPair.getTestCodeStub(),
        taskPair.getImplementationCodeStub(),
        currentChallenge);
  }

  public void closeChallenge(Challenge currentChallenge) {
    currentChallenge.setOpen(false);
    save(currentChallenge);
    logger.debug("Closed challenge {}", currentChallenge.getId());
  }

  public Challenge createChallenge(Challenge newChallenge) {
    newChallenge.setAuthor(userService.getCurrentUser());
    newChallenge.setLevel(1);
    newChallenge.setOpen(true);
    newChallenge.setTasks(new ArrayList<>());
    return save(newChallenge);
  }

  public TaskInstance newTaskInstance(Task task, TaskInstance testTaskInstance) {
    User user = userService.getCurrentUser();
    TaskInstance newTaskInstance = taskInstanceService.createEmpty(user, task);
    if (task.getType() == TaskType.IMPLEMENTATION) {
      logger.debug("Task type is implementation");
      newTaskInstance.setTestTaskInstance(testTaskInstance);
      testTaskInstance.addImplementationTaskInstance(newTaskInstance);
      taskInstanceService.save(newTaskInstance);
      taskInstanceService.save(testTaskInstance);
    }
    return newTaskInstance;
  }
}
