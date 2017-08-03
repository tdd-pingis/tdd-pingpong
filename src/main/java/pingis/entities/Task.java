package pingis.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.*;

@Entity
public class Task {
    // Constraint values
    public static final int NAME_MIN_LENGTH = 3;
    public static final int NAME_MAX_LENGTH = 50;
    public static final int LEVEL_MIN_VALUE = 1;
    public static final int LEVEL_MAX_VALUE = 200;
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id; // unique primary ID

    @NotNull
    private int taskId; // ID in relation to parent Challenge

    @NotNull
    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH)
    private String name;

    @NotNull
    private String desc;
    
    @NotNull
    private ImplementationType type;

    @NotNull
    private String codeStub;

    @NotNull
    @Min(LEVEL_MIN_VALUE)
    @Max(LEVEL_MAX_VALUE)
    private int level;
    private float rating;

    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    private Challenge challenge;

    @NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    private User author;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="task")
    private List<TaskImplementation> implementations;

    protected Task() {}
    
    public Task(int taskId,  ImplementationType type, User author,
            String name, String desc, String codeStub, int level, int rating) {
        this.taskId = taskId;
        this.type = type;
        this.author = author;
        this.name = name;
        this.desc = desc;
        this.codeStub = codeStub;
        this.level = level;
        this.rating = rating;
        this.implementations = new ArrayList<>();
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void addImplementation(TaskImplementation taskImplementation) {
        this.implementations.add(taskImplementation);
    }

    public User getAuthor() {
        return this.author;
    }

    public long getId() {
        return this.id;
    }

    public String getCodeStub() {
        return this.codeStub;
    }

    public void setCodeStub(String codeStub) {
        this.codeStub = codeStub;
    }

    public float getRating() {
        return this.rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Challenge getChallenge() {
        return this.challenge;
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
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<TaskImplementation> getImplementations() {
        return this.implementations;
    }

    public void setImplementations(List<TaskImplementation> implementations) {
        this.implementations = implementations;
    }

    public int getTaskId() {
        return this.taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
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

}
