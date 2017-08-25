package pingis.services;

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

  private final int numberOfLevels = 100;
  private final int pointsForFirstLevel = 1000;
  private final float base = 1.1f;

  private final UserRepository userRepository;
  private final Logger logger = Logger.getLogger(UserService.class);

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
    for (int i = 1; i <= numberOfLevels; i++) {
      if (score < (int)(pointsForFirstLevel * Math.pow(this.base, i - 1))) {
        return i - 1;
      }
    }
    return numberOfLevels;
  }

  /*public int getLevel(int points) {
    int log1 = (int)Math.floor(Math.log(points)/Math.log(base));
    int log2 = (int)Math.floor(Math.log(pointsForFirstLevel)/Math.log(base));
    return log1 - log2 + 1;
  }*/

  public int levelOfCurrentUser() {
    return getLevel(getCurrentUser().getPoints());
  }
}
