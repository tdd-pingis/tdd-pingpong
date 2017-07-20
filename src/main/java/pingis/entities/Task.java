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
    private long taskId;
    private String code;
    private float rating;
    private String desc;
    private String name;
    @ManyToOne(fetch=FetchType.LAZY)
    private Challenge challenge;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="task")
    private List<Implementation> implementations;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="task")
    private List<Test> tests;

    protected Task() {}

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
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

    public List<Implementation> getImplementations() {
        return implementations;
    }

    public void setImplementations(List<Implementation> implementations) {
        this.implementations = implementations;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public Task(String name, String desc) {
        this.name = name;
        this.desc = desc;
        implementations = new ArrayList<>();
        this.rating = 0;
    }
    
    public String getDesc() {
        return this.desc;
    } 
    
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return this.name;
    }

}
