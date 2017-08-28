package pingis.services;

import static pingis.entities.Task.LEVEL_MAX_VALUE;

import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pingis.entities.TmcUserDto;
import pingis.entities.User;
import pingis.repositories.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final Logger logger = Logger.getLogger(UserService.class);

  @Autowired
  private UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  private User findOne(Long userId) {
    // Implement validation here
    Optional<User> opt = userRepository.findById(userId);
    return opt.orElse(null);
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

  public User deleteById(Long userId) {
    //Implement validation here
    User c = findOne(userId);
    userRepository.deleteById(userId);
    return c;
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
}
