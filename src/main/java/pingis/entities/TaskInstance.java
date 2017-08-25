package pingis.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import pingis.entities.sandbox.Submission;

@Entity
public class TaskInstance {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NotNull
  @Lob
  private String code;

  // TODO: Ratings range from 1 to 5, with 0 meaning "hasn't been rated yet."
  // Need to decide on a better representation for this.
  private int rating;

  @OneToOne
  private Submission submission;

  @NotNull
  private CodeStatus status;

  @NotNull
  private boolean isExample; // true if accepted as model solution

  @ManyToOne(fetch = FetchType.LAZY)
  private Task task;

  @NotNull
  @ManyToOne(fetch = FetchType.EAGER)
  private User user;

  @ManyToOne
  private TaskInstance testTaskInstance;

  @OneToMany
  private List<TaskInstance> implementationTaskInstances;

  @OneToOne(mappedBy = "mostRecentArcadeInstance")
  private User mostRecent;

  protected TaskInstance() {
  }

  public TaskInstance(User user, String code, Task task) {
    this.user = user;
    this.code = code;
    this.task = task;
    this.status = CodeStatus.IN_PROGRESS;
    this.isExample = false; // by default
    this.rating = 0;
    this.implementationTaskInstances = new ArrayList<>();
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

  public boolean isRated() {
    return rating != 0;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
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

  public void setTestTaskInstance(TaskInstance testTaskInstance) {
    this.testTaskInstance = testTaskInstance;
  }

  public TaskInstance getTestTaskinstance() {
    return this.testTaskInstance;
  }

  public void addImplementationTaskInstance(TaskInstance implementationTaskInstance) {
    this.implementationTaskInstances.add(implementationTaskInstance);
  }

  public List<TaskInstance> getImplementationTaskInstances() {
    return this.implementationTaskInstances;
  }
  
  public Challenge getChallenge() {
    return this.task.getChallenge();
  }

}
