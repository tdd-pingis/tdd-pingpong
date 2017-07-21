package pingis.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long challengeId;
    private String name;
    private String description;
    private int level;
    private float rating;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="challenge")
    private List<Task> tasks;

    protected Challenge() {
    }

    public Challenge(String name, String description) {
        this.description = description;
        this.name = name;
        this.tasks = new ArrayList<Task>();
        this.rating = 0;
    }
    
    public void addTask(Task t) {
        this.tasks.add(t);
    }
    
    public String toString() {
        String out = this.name+": "+this.description;
        return out;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDesc() {
        return this.description;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public float getRating() {
        return this.rating;
    }

    public long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(long challengeId) {
        this.challengeId = challengeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
    
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

}
