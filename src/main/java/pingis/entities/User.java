package pingis.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import static pingis.entities.Task.LEVEL_MAX_VALUE;
import static pingis.entities.Task.LEVEL_MIN_VALUE;

@Entity
public class User {
    @Id
    @NotNull
    private long id;
    
    @NotNull
    private String name;
    
    @NotNull
    @Min(LEVEL_MIN_VALUE)
    @Max(LEVEL_MAX_VALUE)
    private int level;
    
    // private Set<Role> roles <-- TODO: think through the security roles and 
                                // how to differentiate between users and admins 
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "user")
    private List<TaskImplementation> taskImplementations;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "testUser")
    private List<ChallengeImplementation> testedChallenges;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "implementationUser")
    private List<ChallengeImplementation> implementedChallenges;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "author")
    private List<Challenge> authoredChallenges;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy = "author")
    private List<Task> authoredTasks;

    protected User() {}

    public User(long id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.taskImplementations = new ArrayList<>();
        this.implementedChallenges = new ArrayList<>();
        this.testedChallenges = new ArrayList<>();
        this.authoredChallenges = new ArrayList<>();
        this.authoredTasks = new ArrayList<>();
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

    public List<ChallengeImplementation> getTestedChallenges() {
        return testedChallenges;
    }

    public void setTestedChallenges(List<ChallengeImplementation> testedChallenges) {
        this.testedChallenges = testedChallenges;
    }

    public List<ChallengeImplementation> getImplementedChallenges() {
        return implementedChallenges;
    }

    public void setImplementedChallenges(List<ChallengeImplementation> implementedChallenges) {
        this.implementedChallenges = implementedChallenges;
    }

    public List<Challenge> getAuthoredChallenges() {
        return authoredChallenges;
    }

    public void setAuthoredChallenges(List<Challenge> authoredChallenges) {
        this.authoredChallenges = authoredChallenges;
    }

    public List<Task> getAuthoredTasks() {
        return authoredTasks;
    }

    public void setAuthoredTasks(List<Task> authoredTasks) {
        this.authoredTasks = authoredTasks;
    }

    @Override
    public int hashCode() {
        final int[] hashMultipliers = { 3,79 };
        final int hashBits = 32;
        final int hash =  hashMultipliers[0] * hashMultipliers[1] + (int) (this.id ^ (this.id >>> hashBits));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
