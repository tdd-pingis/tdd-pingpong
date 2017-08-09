package pingis.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class TaskImplementation {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @NotNull
    private String code;

    // TODO: Decide scaling of rating, then set @DecimalMin and @DecimalMax constraints here
    private float rating;

    

    @NotNull
    private CodeStatus status;

    @NotNull
    private boolean isExample; // true if accepted as model solution

    @ManyToOne(fetch=FetchType.LAZY)
    private Task task;

    @NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    private User user;
    
    //@NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    private ChallengeImplementation challengeImplementation;

    protected TaskImplementation() {}

    public TaskImplementation(User user, String code, Task task) {
        this.user = user;
        this.code = code;
        this.task = task;
        this.status = CodeStatus.IN_PROGRESS;
        this.isExample = false; // by default
        this.rating = 0;    
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;   
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isExample() {
        return isExample;
    }

    public void setIsExample(boolean isExample) {
        this.isExample = isExample;
    }

    public CodeStatus getStatus() {
        return status;
    }

    public void setStatus(CodeStatus status) {
        this.status = status;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setChallengeImplementation(ChallengeImplementation challengeImplementation) {
        this.challengeImplementation = challengeImplementation;
    }

    public ChallengeImplementation getChallengeImplementation() {
        return challengeImplementation;
    }
}
