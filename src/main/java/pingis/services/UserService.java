
package pingis.services;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pingis.entities.OAuthUser;
import pingis.entities.User;
import pingis.repositories.UserRepository;
import static pingis.services.DataImporter.UserType.TEST_USER;
import static pingis.services.DataImporter.UserType.TMC_MODEL_USER;
        
@Service
public class UserService {
    
    private final static long DEV_USER_ID  = TEST_USER.getId();
    private final static long DEV_ADMIN_ID = TMC_MODEL_USER.getId();
    private final UserRepository userRepository;
    private final Logger logger = Logger.getLogger("UserService");
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    public User findOne(Long userId) {
        // Implement validation here
        return userRepository.findOne(userId);
    }

    public User save(User user) {
        // Implement validation here
        return userRepository.save(user);
    }
    
    public User handleOAuthUserAuthentication(OAuthUser authUser) {
        User user = findOne(Long.parseLong(authUser.getId()));
        if(user == null) {
            user = initializeUser(authUser);
            logger.info("New OauthUser detected and initialized.");
        }
        
        logger.info("OauthUser successfully authenticated.");
        return user;
    }
    
    public User handleDevUserAuthentication(String userName) {
        User newUser = userRepository.findByName(userName);
        if(newUser == null) {
            newUser = new User(userName);
            userRepository.save(newUser);
            logger.info("New TMCuser detected and initialized.");
        }
        
        logger.info("TMCuser successfully authenticated.");
        return newUser;
    }
    
    public User initializeUser(OAuthUser newUser) {
        // Implement validation here
        return userRepository.save(new User(Long.parseLong(newUser.getId()), 
                             newUser.getName(), 
                             1, 
                             newUser.isAdministrator()));
    }
    
    public List<User> findAll() {
        return (List) userRepository.findAll();
    }

    public void delete(User user) {
        //Implement validation here
        userRepository.delete(user);
    }
    
    public User deleteById(Long userId) {
        //Implement validation here
        User c = findOne(userId);
        userRepository.delete(userId);
        return c;
    }
    
    public void deleteMultiple(List<User> users) {
        //Implement validation here 
        for (User user : users) {
            userRepository.delete(user);
        }
    }

    public boolean contains(Long userId) {
        return userRepository.exists(userId);
    }
    
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

}
