/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pingis.entities.Challenge;
import pingis.entities.ChallengeImplementation;
import pingis.entities.ImplementationType;
import pingis.entities.Task;
import pingis.entities.TaskImplementation;
import pingis.entities.User;
import pingis.repositories.ChallengeImplementationRepository;
import pingis.repositories.ChallengeRepository;
import pingis.repositories.TaskImplementationRepository;
import pingis.repositories.TaskRepository;
import pingis.repositories.UserRepository;

@Component
public class DataImporter implements ApplicationRunner{
    private static final int TMC_USER_LEVEL = 100;
    private static final int TEST_USER_LEVEL = 5;
    private static final int IMPLEMENTATION_USER_LEVEL = 1;
    
    private String jsonString;
    private ChallengeRepository cr;
    private ChallengeImplementationRepository cir;
    private TaskRepository tr;
    private UserRepository ur;
    private TaskImplementationRepository tir;
    private HashMap<String, User> users = new HashMap();
    private ArrayList<Challenge> challenges = new ArrayList();
    private ArrayList<Task> tasks = new ArrayList();
    private ArrayList<TaskImplementation> taskImplementations = new ArrayList();
    private ArrayList<ChallengeImplementation> challengeImplementations = new ArrayList();
    private int taskid = 0;

    @Autowired
    public DataImporter(ChallengeRepository cr, TaskRepository tr, UserRepository ur,
            ChallengeImplementationRepository cir, TaskImplementationRepository tir) {
        this.cr = cr;
        this.tr = tr;
        this.ur = ur;
        this.cir = cir;
        this.tir = tir;
    }
    
    protected DataImporter() {}
    
    public void run(ApplicationArguments args) throws Exception {
        DataLoaderIO io = new DataLoaderIO();
        readData(io);
        generateUsers();
        generateEntities();        
        populateDatabase();
    }

    public void readData(IO io) {
        String out = "";
        while(io.hasNext()) {
            out += io.nextLine();
        }
        this.jsonString = out;
    }

    public String getJsonString() {
        return jsonString;
    }
    
    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
    
    public void generateUsers() {
        long userId = new Random().nextInt(Integer.MAX_VALUE);
        
        users.put("modeluser", new User(DataLoader.UserType.TMC_MODEL_USER.getId(), 
                  DataLoader.UserType.TMC_MODEL_USER.name(), TMC_USER_LEVEL)); 
        users.put("testuser", new User(DataLoader.UserType.TEST_USER.getId(), 
                  DataLoader.UserType.TEST_USER.name(), TEST_USER_LEVEL));
        users.put("impluser", new User(DataLoader.UserType.IMPLEMENTATION_USER.getId(), 
                  DataLoader.UserType.IMPLEMENTATION_USER.name(), IMPLEMENTATION_USER_LEVEL));

    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public ArrayList<Challenge> getChallenges() {
        return challenges;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<TaskImplementation> getTaskImplementations() {
        return taskImplementations;
    }

    public ArrayList<ChallengeImplementation> getChallengeImplementations() {
        return challengeImplementations;
    }
    
    public void generateEntities() {
        JSONObject jsonImportObject = new JSONObject(this.jsonString);
        JSONArray challenges = jsonImportObject.getJSONArray("challenges");
        
        for (int i = 0; i < challenges.length(); i++) {
            JSONObject challengeObject = challenges.getJSONObject(i);
            JSONArray tasks = challengeObject.getJSONArray("tasks");
            
            Challenge challenge = new Challenge(challengeObject.getString("name"), 
                                    users.get(challengeObject.getString("author")), 
                                    challengeObject.getString("desc"));
            
            
            ChallengeImplementation challengeImp = new ChallengeImplementation(challenge, 
                                    users.get(challengeObject.getString("author")), 
                                    users.get(challengeObject.getString("author")));
            
            this.challenges.add(challenge);
            this.challengeImplementations.add(challengeImp);
            
            for (int j = 0; j < tasks.length(); j++) {
                generateChallengeContent(tasks, j, challengeObject, challenge, challengeImp);
            }
        }
    }

    private void generateChallengeContent(JSONArray tasks1, int j, JSONObject challengeObject, 
                                    Challenge challenge, ChallengeImplementation challengeImp) {
        // Parse object out of json-string
        JSONObject taskObject = tasks1.getJSONObject(j);
        
        // Parse username from challenge-object
        User author = users.get(challengeObject.getString("author"));
        author.addAuthoredChallenge(challenge);
        
        // Parse stub code array from JSON and assemble into a string
        JSONArray codeArray = taskObject.getJSONArray("codeStub");
        String codeStub = assembleString(codeArray);
        
        String taskType = taskObject.getString("type");
        ImplementationType type;
        Task task;
        
        if (taskType.equals("test")) {
            task = createTask(ImplementationType.TEST, author, taskObject, codeStub, challenge);
        } else {
            task = createTask(ImplementationType.IMPLEMENTATION, author, taskObject, codeStub, challenge);
        }
        
        String modelImp = "";
        JSONArray modelImpArray = taskObject.getJSONArray("modelimplementation");
        modelImp = assembleString(modelImpArray);
        TaskImplementation taskImplementation = createTaskImplementation(author, modelImp, task, challengeImp);
        
        this.taskImplementations.add(taskImplementation);
    }

    private TaskImplementation createTaskImplementation(User author, String modelImp, 
                        Task task, ChallengeImplementation challengeImp) {
        // Create new TaskImplementation
        TaskImplementation taskImplementation = new TaskImplementation(author, modelImp, task);
        task.addImplementation(taskImplementation);
        challengeImp.addTaskImplementation(taskImplementation);
        
        // Set it to it's parent ChallengeImplementation
        taskImplementation.setChallengeImplementation(challengeImp);
        taskImplementation.setTask(task);
        
        return taskImplementation;
    }

    private Task createTask(ImplementationType type, User author, JSONObject taskObject, 
                        String codeStub, Challenge challenge) {
        Task task = new Task(this.taskid++, type, author,
                taskObject.getString("name"),
                taskObject.getString("desc"),
                codeStub, 1, 1);
        this.tasks.add(task);
        challenge.addTask(task);
        task.setChallenge(challenge);
        return task;
    }

    private String assembleString(JSONArray array) {
        String concatenated = "";
        
        for (int k = 0; k < array.length(); k++) {
            concatenated += array.getString(k);
        }
        
        return concatenated;
    }
    
    public void populateDatabase() {
        for (String key : this.users.keySet()) {
            ur.save(this.users.get(key));
        }
        
        for (Challenge c : this.challenges) {
            cr.save(c);
        }
        
        for (Task t : this.tasks) {
            tr.save(t);
        }       
        
        for (ChallengeImplementation c : this.challengeImplementations) {
            cir.save(c);
        }       
        
        for (TaskImplementation i : this.taskImplementations) {
            tir.save(i);
        }
    }
    
    private void printResults() {
        // For debugging-purposes
        System.out.println("users:");
        for (String key : this.users.keySet()) {
            System.out.println("key: "+key+", value: "+this.users.get(key).toString()+"\n");
        }
        System.out.println("---");
        System.out.println("challenges:");
        for (Challenge c : this.challenges) {
            System.out.println("challenge: "+c.toString()+"\n");
            for (Task t : c.getTasks()) {
                System.out.println("  task: "+t.toString());
            }
        }
        System.out.println("---");
        System.out.println("taskimplementations:");
        for (TaskImplementation i : this.taskImplementations) {
            System.out.println("ti: "+i.toString()+"\n");
        }
        System.out.println("---");
        System.out.println("challenge implementations:");
        for (ChallengeImplementation imp : this.challengeImplementations) {
            System.out.println(imp);
        }
    }
}
