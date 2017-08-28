package pingis.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class TaskPair {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NotNull
  @Size(min = 3, max = 30)
  private String className;

  @NotNull
  @Size(min = 3, max = 50)
  private String testTaskName;

  @NotNull
  @Size(min = 5, max = 200)
  private String testTaskDesc;

  private String implementationTaskName;
  private String implementationTaskDesc;

  private String testCodeStub;
  private String implementationCodeStub;

  protected TaskPair() {}

  public TaskPair(String className, String testTaskName, String implementationTaskName,
      String testTaskDesc, String implementationTaskDesc) {
    this.className = className;

    this.testTaskName = testTaskName;
    this.implementationTaskName = implementationTaskName;
    this.testTaskDesc = testTaskDesc;
    this.implementationTaskDesc = implementationTaskDesc;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTestTaskName() {
    return testTaskName;
  }

  public void setTestTaskName(String testTaskName) {
    this.testTaskName = testTaskName;
  }

  public String getImplementationTaskName() {
    return implementationTaskName;
  }

  public void setImplementationTaskName(String implementationTaskName) {
    this.implementationTaskName = implementationTaskName;
  }

  public String getTestTaskDesc() {
    return testTaskDesc;
  }

  public void setTestTaskDesc(String testTaskDesc) {
    this.testTaskDesc = testTaskDesc;
  }

  public String getImplementationTaskDesc() {
    return implementationTaskDesc;
  }

  public void setImplementationTaskDesc(String implementationTaskDesc) {
    this.implementationTaskDesc = implementationTaskDesc;
  }

  public String getTestCodeStub() {
    return testCodeStub;
  }

  public void setTestCodeStub(String testCodeStub) {
    this.testCodeStub = testCodeStub;
  }

  public String getImplementationCodeStub() {
    return implementationCodeStub;
  }

  public void setImplementationCodeStub(String implementationCodeStub) {
    this.implementationCodeStub = implementationCodeStub;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }
}
