package pingis.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id; // unique primary ID 
    private int taskId; // ID in relation to parent Challenge
    private String name;
    private String desc;
    private String code;
    private int level;
    private float rating;
    
    @ManyToOne(fetch=FetchType.LAZY)
    private Challenge challenge;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="task")
    private List<TaskImplementation> taskImplementations;

    protected Task() {}

    public Task(int taskId, String name, String desc, String code, int level, int rating) {
        this.taskId = taskId;
        this.name = name;
        this.desc = desc;
        this.code = code;
        this.level = level;
        this.rating = rating;
        this.taskImplementations = new ArrayList<>();
    }

    public long getId() {
        return taskId;
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

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public String getDesc() {
        return this.desc;
    } 

    public void setName(String name) {
        this.name = name;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSequenceId() {
        return taskId;
    }

    public void setSequenceId(int sequenceId) {
        this.taskId = sequenceId;
    }
    
    public List<TaskImplementation> getImplementations() {
        return taskImplementations;
    }

    public void setImplementations(List<TaskImplementation> implementations) {
        this.taskImplementations = implementations;
    }

}
