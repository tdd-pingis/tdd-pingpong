package pingis.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.CodeStatus;
import pingis.entities.Realm;
import pingis.entities.Task;
import pingis.entities.TaskInstance;
import pingis.entities.TaskType;
import pingis.entities.User;
import pingis.entities.sandbox.Submission;
import pingis.repositories.ChallengeRepository;
import pingis.repositories.TaskInstanceRepository;
import pingis.repositories.TaskRepository;
import pingis.repositories.UserRepository;
import pingis.repositories.sandbox.SubmissionRepository;

@Component
public class DataImporter {

  private static final int TMC_USER_LEVEL = 100000;
  private static final int TEST_USER_LEVEL = 5000;
  private static final int IMPLEMENTATION_USER_LEVEL = 1000;

  private String jsonString;
  private ChallengeRepository challengeRepository;
  private TaskRepository taskRepository;
  private UserRepository userRepository;
  private SubmissionRepository submissionRepository;
  private TaskInstanceRepository taskInstanceRepository;
  private HashMap<String, User> users = new LinkedHashMap<>();
  private HashMap<String, Challenge> challenges = new HashMap<>();
  private ArrayList<Task> tasks = new ArrayList<>();
  private ArrayList<TaskInstance> taskInstances = new ArrayList<>();

  public enum UserType {
    TMC_MODEL_USER("admin", 0, true),
    TEST_USER("user", 1, false),
    IMPLEMENTATION_USER("impluser", 2, false);

    private final String login;
    private final long id;
    private final boolean admin;

    private UserType(String login, long id, boolean admin) {
      this.login = login;
      this.id = id;
      this.admin = admin;
    }

    public long getId() {
      return id;
    }

    public String getLogin() {
      return login;
    }

    public boolean isAdmin() {
      return admin;
    }
  }

  @Autowired
  public DataImporter(ChallengeRepository challengeRepository,
                      TaskRepository taskRepository,
                      UserRepository userRepository,
                      TaskInstanceRepository taskInstanceRepository,
                      SubmissionRepository submissionRepository1)
                      throws Exception {

    this.challengeRepository = challengeRepository;
    this.taskRepository = taskRepository;
    this.userRepository = userRepository;
    this.taskInstanceRepository = taskInstanceRepository;
    this.submissionRepository = submissionRepository1;
    initializeDatabase();
  }

  protected DataImporter() {
  }

  public void initializeDatabase() throws Exception {
    Io io = new DataloaderIo();
    readData(io);
    generateUsers();
    generateEntities();
    populateDatabase();
  }

  public void dropDatabase() {
    taskInstances.clear();
    tasks.clear();
    challenges.clear();
    users.clear();

    submissionRepository.deleteAll();
    taskInstanceRepository.deleteAll();
    taskRepository.deleteAll();
    challengeRepository.deleteAll();
    userRepository.deleteAll();
  }

  public void readData(Io io) {
    String out = "";
    while (io.hasNext()) {
      out += io.nextLine();
    }
    this.jsonString = out;
  }

  public String getJsonString() {
    return jsonString;
  }

  public void generateUsers() {
    long userId = new Random().nextInt(Integer.MAX_VALUE);

    users.put("modeluser", new User(
        UserType.TMC_MODEL_USER.getId(),
        UserType.TMC_MODEL_USER.getLogin(),
        Task.LEVEL_MAX_VALUE,
        UserType.TMC_MODEL_USER.isAdmin()));

    users.put("testuser", new User(
        UserType.TEST_USER.getId(),
        UserType.TEST_USER.getLogin(),
        TEST_USER_LEVEL));

    users.put("impluser", new User(
        UserType.IMPLEMENTATION_USER.getId(),
        UserType.IMPLEMENTATION_USER.getLogin(),
        IMPLEMENTATION_USER_LEVEL));
  }

  public HashMap<String, User> getUsers() {
    return users;
  }

  public HashMap<String, Challenge> getChallenges() {
    return challenges;
  }

  public ArrayList<Task> getTasks() {
    return tasks;
  }

  public ArrayList<TaskInstance> getTaskInstances() {
    return taskInstances;
  }

  public void generateEntities() {
    JSONObject jsonImportObject = new JSONObject(this.jsonString);
    JSONArray challenges = jsonImportObject.getJSONArray("challenges");

    for (int i = 0; i < challenges.length(); i++) {
      JSONObject challengeObject = challenges.getJSONObject(i);
      Challenge challenge = new Challenge(challengeObject.getString("name"),
          users.get(challengeObject.getString("author")),
          challengeObject.getString("desc"));
      challenge.setType(ChallengeType.valueOf(challengeObject.getString("type").toUpperCase()));
      challenge.setRealm(Realm.valueOf(challengeObject.getString("realm").toUpperCase()));
      challenge.setLevel(challengeObject.getInt("level"));
      JSONArray tasks = challengeObject.getJSONArray("tasks");
      this.challenges.put(challengeObject.getString("name"), challenge);
      for (int j = 0; j < tasks.length(); j++) {
        generateChallengeContent(tasks, j, challengeObject, challenge);
      }
    }
    this.createDummyInstances(jsonImportObject.getJSONArray("dummyimplementations"));

  }

  private void generateChallengeContent(JSONArray tasks1, int j, JSONObject challengeObject,
      Challenge challenge) {
    // Parse object out of json-string
    JSONObject taskObject = tasks1.getJSONObject(j);

    // Parse username from challenge-object
    User author = users.get(challengeObject.getString("author"));
    author.addAuthoredChallenge(challenge);

    // Parse stub code array from JSON and assemble into a string
    JSONArray codeArray = taskObject.getJSONArray("codeStub");
    String codeStub = assembleString(codeArray);

    String taskType = taskObject.getString("type");
    TaskType type;
    Task task;

    if (taskType.equals("test")) {
      task = createTask(TaskType.TEST, author, taskObject, codeStub, challenge);
    } else {
      task = createTask(TaskType.IMPLEMENTATION, author, taskObject, codeStub, challenge);
    }
    String modelImp = "";
    JSONArray modelImpArray = taskObject.getJSONArray("modelimplementation");
    modelImp = assembleString(modelImpArray);
    TaskInstance taskInstance = createTaskInstance(author, modelImp, task);

    this.taskInstances.add(taskInstance);
  }

  private TaskInstance createTaskInstance(User author, String modelImp,
      Task task) {
    // Create new TaskInstance
    TaskInstance taskInstance = new TaskInstance(author, modelImp, task);
    task.addTaskInstance(taskInstance);
    taskInstance.setStatus(CodeStatus.DONE);

    return taskInstance;
  }

  private Task createTask(TaskType type, User author, JSONObject taskObject,
      String codeStub, Challenge challenge) {
    Task task = new Task(taskObject.getInt("index"), type, author,
        taskObject.getString("name"),
        taskObject.getString("desc"),
        codeStub, 1, 1);
    this.tasks.add(task);
    challenge.addTask(task);
    task.setChallenge(challenge);
    return task;
  }

  private void createDummyInstances(JSONArray instances) {
    for (int i = 0; i < instances.length(); i++) {
      JSONObject instance = instances.getJSONObject(i);
      JSONArray taskInstances = instance.getJSONArray("taskinstances");
      for (int j = 0; j < taskInstances.length(); j++) {
        JSONObject implementationObject = taskInstances.getJSONObject(j);
        User user = users.get(implementationObject.getString("user"));
        List<Task> tasks = this.challenges.get(instance.getString("challenge")).getTasks();
        Task task = tasks.get(implementationObject.getInt("taskindex") - 1);
        TaskInstance taskInstance =
            createTaskInstance(
                user, "",
                task);
        this.taskInstances.add(taskInstance);
      }
    }
  }

  private String assembleString(JSONArray array) {
    String concatenated = "";

    for (int k = 0; k < array.length(); k++) {
      concatenated += array.getString(k);
    }

    return concatenated;
  }

  public void populateDatabase() {
    for (User user : this.users.values()) {
      userRepository.save(user);
    }

    for (String name : this.challenges.keySet()) {
      challengeRepository.save(this.challenges.get(name));
    }

    for (Task t : this.tasks) {
      taskRepository.save(t);
    }

    for (TaskInstance i : this.taskInstances) {
      taskInstanceRepository.save(i);
    }

  }

  private void printDatabase() {
    // For debugging-purposes
    System.out.println("users:");
    for (String key : this.users.keySet()) {
      System.out.println("key: " + key + ", value: " + this.users.get(key).toString() + "\n");
    }
    printChallenges();
    printTaskInstances();
  }

  private void printChallenges() {
    System.out.println("---");
    System.out.println("challenges:");
    for (String s : this.challenges.keySet()) {
      Challenge c = this.challenges.get(s);
      System.out.println("challenge: " + c.toString() + "\n");
      for (Task t : c.getTasks()) {
        System.out.println("  task: " + t.toString());
      }
    }
  }

  private void printTaskInstances() {
    System.out.println("---");
    System.out.println("taskinstances:");
    for (TaskInstance i : this.taskInstances) {
      System.out.println("ti: " + i.toString());
      System.out.println("-");
    }
  }

}
