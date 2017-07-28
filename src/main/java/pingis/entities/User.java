package pingis.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;

@Entity
public class User {
    @Id
    private long id;
    private String name;
    private int level;
    
    // private Set<Role> roles <-- TODO: think through the security roles and 
                                // how to differentiate between users and admins 
    
    @OneToMany(fetch=FetchType.LAZY)
    private List<TaskImplementation> taskImplementations;

    protected User() {}

    public User(long id, String name, int level) {
        this.name = name;
        this.level = level;
        this.taskImplementations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    
    public long getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setTaskImplementations(List<TaskImplementation> taskImplementations) {
        this.taskImplementations = taskImplementations;
    }
    
    public List<TaskImplementation> getTaskImplementations() {
        return this.taskImplementations;
    }
    
}
