package pingis.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Entity
public class User {

  public static final int POINTS_MIN_VALUE = 1;

  @Id
  @NotNull
  private long id;

  @NotNull
  public String name;
  public String email;
  public boolean administrator;

  @NotNull
  @Min(POINTS_MIN_VALUE)
  private int points;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private List<TaskInstance> taskInstances;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
  private List<Challenge> authoredChallenges;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
  private List<Task> authoredTasks;
  
  @OneToMany(mappedBy = "secondPlayer")
  private List<Challenge> participatingLiveChallenges;

  public User() {
  }

  public User(String name) {
    // This constructor is only for 'dev'-profile, thus the pseudo-random id generation
    // -> real user-id's are fetched under 'oauth'-profile from TMC-server
    this(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE, name, POINTS_MIN_VALUE);
  }

  public User(long id, String name, int points) {
    this(id, name, points, false);
  }

  public User(long id, String name, int points, boolean isAdministrator) {
    this.id = id;
    this.name = name;
    this.points = points;
    this.administrator = isAdministrator;
    this.taskInstances = new ArrayList<>();
    this.authoredChallenges = new ArrayList<>();
    this.authoredTasks = new ArrayList<>();
    this.participatingLiveChallenges = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getPoints() {
    return points;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setUsername(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setAdministrator(boolean administrator) {
    this.administrator = administrator;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public void addPoints(int points) {
    this.points += points;
  }

  public void setTaskInstances(List<TaskInstance> taskInstances) {
    this.taskInstances = taskInstances;
  }

  public List<TaskInstance> getTaskInstances() {
    return this.taskInstances;
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

  public boolean isAdministrator() {
    return administrator;
  }

  @Override
  public int hashCode() {
    final int[] hashMultipliers = {3, 79};
    final int hashBits = 32;
    final int hash =
        hashMultipliers[0] * hashMultipliers[1] + (int) (this.id ^ (this.id >>> hashBits));
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

  public void addAuthoredChallenge(Challenge c) {
    this.authoredChallenges.add(c);
  }

  @Override
  public String toString() {
    return "User Details: "
        + "n\ttype: User"
        + "\n\tname: " + getName()
        + "\n\tid: " + getId()
        + "\n\tadmin: " + isAdministrator()
        + "\n\tpoints: " + getPoints();
  }

  public List<Challenge> getParticipatingLiveChallenges() {
    return participatingLiveChallenges;
  }

  public void setParticipatingLiveChallenges(List<Challenge> participatingLiveChallenges) {
    this.participatingLiveChallenges = participatingLiveChallenges;
  }
  
  public void addParticipatingLiveChallenge(Challenge challenge) {
    this.participatingLiveChallenges.add(challenge);
  }

}
