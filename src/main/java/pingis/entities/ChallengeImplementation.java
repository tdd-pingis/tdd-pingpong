package pingis.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
public class ChallengeImplementation {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @NotNull
    private CodeStatus status;  
    
    
    @ManyToOne(fetch=FetchType  .EAGER)
    private Challenge challenge;
    
    @NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    private User testUser;

    @NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    private User implementationUser;
    
    @OneToMany(fetch=FetchType.LAZY, /* cascade = {CascadeType.ALL} ,*/ mappedBy = "challengeImplementation")
    private List<TaskImplementation> taskImplementations;

    protected ChallengeImplementation() {}

    public ChallengeImplementation(Challenge challenge, User testUser, User implementationUser) {
        this.challenge = challenge;
        this.testUser = testUser;
        this.implementationUser = implementationUser;
        this.status = CodeStatus.IN_PROGRESS; 
        this.taskImplementations = new ArrayList<>();
    }
    
    public long getId() {
        return id;
    }
    
    public void addTaskImplementation(TaskImplementation taskImplementation) {
        this.taskImplementations.add(taskImplementation);
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
        if (!testUser.equals(this.implementationUser)) {
            this.testUser = testUser;
        }
    }

    public User getImplementationUser() {
        return implementationUser;
    }

    public void setImplementationUser(User implementationUser) {
        if (!this.testUser.equals(implementationUser)) {
            this.implementationUser = implementationUser;
        }
    }

    public List<TaskImplementation> getTaskImplementations() {
        return taskImplementations;
    }

    public void setTaskImplementations(List<TaskImplementation> taskImplementations) {
        this.taskImplementations = taskImplementations;
    }

}
