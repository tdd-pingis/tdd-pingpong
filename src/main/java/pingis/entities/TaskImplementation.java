package pingis.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

@Entity
public class TaskImplementation {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String code;
    private float rating;
    private ImplementationType type;
    private CodeStatus status;
    private boolean isExample; // true if model solution
    private boolean isPublic; // turns public (true) when challenge is completed
    
    @ManyToOne(fetch=FetchType.LAZY)
    private Task task;
    
    @ManyToOne(fetch=FetchType.EAGER)
    private User user;

    protected TaskImplementation() {}

    public TaskImplementation(String code, ImplementationType type) {
        this.type = type;
        this.code = code;
        this.isExample = false; // default value
        this.isPublic = false;
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
    
    public ImplementationType getType() {
        return type;
    }
    
    public void setType(ImplementationType type) {
        this.type = type;
    }
    
    public void setTypeTest() {
        this.type = ImplementationType.TEST;
    }
    
    public void setTypeImplementation() {
        this.type = ImplementationType.IMPLEMENTATION;
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
    
    public boolean isPublic() {
        return isPublic;
    }
    
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
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

}