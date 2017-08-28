package pingis.entities;

import static pingis.entities.Task.LEVEL_MAX_VALUE;
import static pingis.entities.Task.LEVEL_MIN_VALUE;
import static pingis.entities.Task.NAME_MAX_LENGTH;
import static pingis.entities.Task.NAME_MIN_LENGTH;

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
public class Challenge {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH)
  @NotNull
  private String name;

  @NotNull
  private String description;

  @NotNull
  @Min(LEVEL_MIN_VALUE)
  @Max(LEVEL_MAX_VALUE)
  private int level;
  private float rating;
  private ChallengeType type;


  private Realm realm;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "challenge")
  private List<Task> tasks;

  @NotNull
  @ManyToOne(fetch = FetchType.EAGER)
  private User author;

  public User getSecondPlayer() {
    return secondPlayer;
  }

  public void setSecondPlayer(User secondPlayer) {
    this.secondPlayer = secondPlayer;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  private User secondPlayer;

  private boolean isOpen;

  public boolean getIsOpen() {
    return isOpen;
  }

  public void setOpen(boolean open) {
    isOpen = open;
  }

  protected Challenge() {
  }

  public Challenge(String name, User author, String description, ChallengeType type) {
    this.name = name;
    this.author = author;
    this.type = type;
    this.description = description;
    this.tasks = new ArrayList<>();
    this.isOpen = false;
  }

  public Challenge(String name, User author, String description) {
    this(name, author, description, ChallengeType.MIXED);
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public User getAuthor() {
    return this.author;
  }

  public void addTask(Task task) {
    this.tasks.add(task);
    int newTaskLevel = task.getLevel();

    if (this.level == 0) {
      this.level = newTaskLevel;
    } else {
      if (newTaskLevel < this.level) {
        this.level = newTaskLevel;
      }
    }
  }

  public String toString() {
    String out = this.name + ": " + this.description;
    out += "\ntype: " + this.type.name();
    out += "\nlevel: " + this.level;
    out += "\nauthor: " + this.author.getName();
    
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

  public long getId() {
    return id;
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

  public ChallengeType getType() {
    return type;
  }

  public void setType(ChallengeType type) {
    this.type = type;
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

  public Realm getRealm() {
    return realm;
  }

  public void setRealm(Realm realm) {
    this.realm = realm;
  }

}
