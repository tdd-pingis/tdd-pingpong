
package pingis.services;

import java.util.List;
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
    
    public boolean authenticateOAuthUser(OAuthUser authUser) {
        return userRepository.exists(Long.parseLong(authUser.getId()));
    }
    
    public boolean authenticateUser(String userName) {
        // Only for development purposes (OAuth2-login disabled)
        return userRepository.findByName(userName) != null;
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

    public void verifyUserInitialization() throws IllegalStateException {
        if (!this.contains(DEV_USER_ID)) {
            throw new IllegalStateException("Database initialization error: "
                    + "'user' does not exist in the DB.");
            
        } else if (!this.contains(DEV_ADMIN_ID)) {
            throw new  IllegalStateException("Database initialization error: "
                    + "'admin' does not exist in the DB.");
        }
    }
    
}
