package pingis.entities;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ChallengeImplementation {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private CodeStatus status;
    
    @ManyToOne(fetch=FetchType.EAGER)
    private Challenge challenge;
    
    @ManyToOne(fetch=FetchType.EAGER)
    private User testUser;
    
    @ManyToOne(fetch=FetchType.EAGER)
    private User implementationUser;
    
    @OneToMany(fetch=FetchType.LAZY)
    private List<TaskImplementation> taskImplementations;

    protected ChallengeImplementation() {}

    public ChallengeImplementation(Challenge challenge, User testUser, User implementationUser) {
        this.challenge = challenge;
        this.testUser = testUser;
        this.implementationUser = implementationUser;
        this.status = CodeStatus.IN_PROGRESS;
    }

    public long getId() {
        return id;
    }
    
    public void setStatus(CodeStatus status) {
        this.status = status;
    }
    
    public CodeStatus getStatus() {
        return this.status;
    }
    
    public void setCompleted() {
        this.status = CodeStatus.DONE;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public User getTestUser() {
        return testUser;
    }

    public void setTestUser(User testUser) {
        this.testUser = testUser;
    }

    public User getImplementationUser() {
        return implementationUser;
    }

    public void setImplementationUser(User implementationUser) {
        this.implementationUser = implementationUser;
    }

    public List<TaskImplementation> getTaskImplementations() {
        return taskImplementations;
    }

    public void setTaskImplementations(List<TaskImplementation> taskImplementations) {
        this.taskImplementations = taskImplementations;
    }

}
