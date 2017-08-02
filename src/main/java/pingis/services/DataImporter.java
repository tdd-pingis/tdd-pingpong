/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.services;

/**
 *
 * @author lauri
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import org.json.*;
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

    private String jsonString;
    private ChallengeRepository cr;
    private ChallengeImplementationRepository cir;
    private TaskRepository tr;
    private UserRepository ur;
    private TaskImplementationRepository tir;
    //private ArrayList<User> users = new ArrayList();
    private HashMap<String, User> users = new HashMap();
    private ArrayList<Challenge> challenges = new ArrayList();
    private ArrayList<Task> tasks = new ArrayList();
    private ArrayList<TaskImplementation> taskImplementations = new ArrayList();
    private ArrayList<ChallengeImplementation> challengeImplementations = new ArrayList();
    private int taskid = 0;
//CHECKSTYLE:OFF
    public void run(ApplicationArguments args) throws Exception {
        readData("exampledata/dummychallenges.json");
        generateUsers();
        generateEntities();
        printResults();
    }
    
    public void readData(String filename) {
        Scanner s = new Scanner(getClass().getClassLoader().getResourceAsStream(filename));
        String out = "";
        while(s.hasNext()) {
            out += s.nextLine();
        }
        this.jsonString = out;
    }
    
    private void generateUsers() {
        long userId = new Random().nextInt(Integer.MAX_VALUE);

        
        users.put("modeluser", new User(DataLoader.UserType.TMC_MODEL_USER.getId(), DataLoader.UserType.TMC_MODEL_USER.name(), 100)); 
        users.put("testuser", new User(DataLoader.UserType.TEST_USER.getId(), DataLoader.UserType.TEST_USER.name(), 5));
        users.put("impluser", new User(DataLoader.UserType.IMPLEMENTATION_USER.getId(), DataLoader.UserType.IMPLEMENTATION_USER.name(), 1));

    }
    
    public void generateEntities() {
        JSONObject obj = new JSONObject(this.jsonString);
        JSONArray challenges = obj.getJSONArray("challenges");
        for (int i = 0; i < challenges.length(); i++) {
            JSONObject o = challenges.getJSONObject(i);
            JSONArray tasks = o.getJSONArray("tasks");
            Challenge c = new Challenge(o.getString("name"), users.get(o.getString("user")), o.getString("desc"));            
            this.challenges.add(c);
            for (int j = 0; j < tasks.length(); j++) {
                JSONObject task = tasks.getJSONObject(j);

                User user = users.get(task.getString("user"));
                ChallengeImplementation chImp = new ChallengeImplementation(c, user, user);
                
                
                String codeStub = "";
                JSONArray codearray = task.getJSONArray("codeStub");
                for (int k = 0; k < codearray.length(); k++) {
                    codeStub += codearray.getString(k);
                }
                
                String typeString = task.getString("type");
                
                ImplementationType type;
                
                if (typeString.equals("test")) {
                    type = ImplementationType.TEST;
                } else {
                    type = ImplementationType.IMPLEMENTATION;
                }

                Task t = new Task(this.taskid++, type, user, task.getString("name"), task.getString("desc"), codeStub, 1, 1);
                this.tasks.add(t);
                t.setChallenge(c);
                c.addTask(t);

                String modelImp = "";
                JSONArray mImp = task.getJSONArray("modelimplementation");
                for (int k = 0; k < mImp.length(); k++) {
                    modelImp += mImp.getString(k);
                }

                TaskImplementation imp = new TaskImplementation(user, modelImp, t);
                t.addImplementation(imp);
                chImp.addTaskImplementation(imp);
                imp.setChallengeImplementation(chImp);
                imp.setTask(t);
                this.taskImplementations.add(imp);
            }
        }
    }
    
    public void populateDatabase() {
        for (String key : this.users.keySet()) {
            ur.save(this.users.get(key));
        }
        
        for (Challenge c : this.challenges) {
            cr.save(c);            
        }
        
        for (TaskImplementation i : this.taskImplementations) {
            tir.save(i);
        }
    }
    
    public void printResults() {
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
    }
    //CHECKSTYLE:ON
}
