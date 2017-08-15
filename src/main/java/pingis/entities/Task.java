package pingis.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Task {

  // Constraint values
  public static final int NAME_MIN_LENGTH = 3;
  public static final int NAME_MAX_LENGTH = 50;
  public static final int LEVEL_MIN_VALUE = 1;
  public static final int LEVEL_MAX_VALUE = 200;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id; // unique primary ID

  @NotNull
  private int index; // sequence number in relation to parent Challenge

  @NotNull
  @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH)
  private String name;

  @NotNull
  private String desc;

  @NotNull
  private TaskType type;

  @NotNull
  private String codeStub;

  @NotNull
  @Min(LEVEL_MIN_VALUE)
  @Max(LEVEL_MAX_VALUE)
  private int level;
  private float rating;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  private Challenge challenge;

  @NotNull
  @ManyToOne(fetch = FetchType.EAGER)
  private User author;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
  private List<TaskInstance> taskInstances;

  protected Task() {
  }

  public Task(int index, TaskType type, User author,
      String name, String desc, String codeStub, int level, int rating) {
    this.index = index;
    this.type = type;
    this.author = author;
    this.name = name;
    this.desc = desc;
    this.codeStub = codeStub;
    this.level = level;
    this.rating = rating;
    this.taskInstances = new ArrayList<>();
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public void addTaskInstance(TaskInstance taskInstance) {
    this.taskInstances.add(taskInstance);
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

  public List<TaskInstance> getTaskInstances() {
    return this.taskInstances;
  }

  public void setTaskInstances(List<TaskInstance> taskInstances) {
    this.taskInstances = taskInstances;
  }

  public int getIndex() {
    return this.index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public TaskType getType() {
    return type;
  }

  public void setType(TaskType type) {
    this.type = type;
  }

  public void setTypeTest() {
    this.type = TaskType.TEST;
  }

  public void setTypeImplementation() {
    this.type = TaskType.IMPLEMENTATION;
  }

}
