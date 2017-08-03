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
//CHECKSTYLE:OFF

    @Autowired
    public DataImporter(ChallengeRepository cr, TaskRepository tr, UserRepository ur,
            ChallengeImplementationRepository cir, TaskImplementationRepository tir) {
        this.cr = cr;
        this.tr = tr;
        this.ur = ur;
        this.cir = cir;
        this.tir = tir;
    }
    
    public DataImporter() {
    }
    
    public void run(ApplicationArguments args) throws Exception {
        DataLoaderIO io = new DataLoaderIO();
        readData(io);
        generateUsers();
        generateEntities();
        printResults();
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

        
        users.put("modeluser", new User(DataLoader.UserType.TMC_MODEL_USER.getId(), DataLoader.UserType.TMC_MODEL_USER.name(), 100)); 
        users.put("testuser", new User(DataLoader.UserType.TEST_USER.getId(), DataLoader.UserType.TEST_USER.name(), 5));
        users.put("impluser", new User(DataLoader.UserType.IMPLEMENTATION_USER.getId(), DataLoader.UserType.IMPLEMENTATION_USER.name(), 1));

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
        JSONObject obj = new JSONObject(this.jsonString);
        JSONArray challenges = obj.getJSONArray("challenges");
        for (int i = 0; i < challenges.length(); i++) {
            JSONObject o = challenges.getJSONObject(i);
            JSONArray tasks = o.getJSONArray("tasks");
            Challenge c = new Challenge(o.getString("name"), users.get(o.getString("author")), o.getString("desc"));
            this.challenges.add(c);
            ChallengeImplementation chImp = new ChallengeImplementation(c, users.get(o.getString("author")), users.get(o.getString("author")));
            this.challengeImplementations.add(chImp);
            

            for (int j = 0; j < tasks.length(); j++) {
                JSONObject task = tasks.getJSONObject(j);

                User author = users.get(o.getString("author"));
                author.addAuthoredChallenge(c);
                
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

                Task t = new Task(this.taskid++, type, author, task.getString("name"), task.getString("desc"), codeStub, 1, 1);
                this.tasks.add(t);
                c.addTask(t);

                t.setChallenge(c);

                String modelImp = "";
                JSONArray mImp = task.getJSONArray("modelimplementation");
                for (int k = 0; k < mImp.length(); k++) {
                    modelImp += mImp.getString(k);
                }

                TaskImplementation imp = new TaskImplementation(author, modelImp, t);
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
        System.out.println("---");
        System.out.println("challenge implementations:");
        for (ChallengeImplementation imp : this.challengeImplementations) {
            System.out.println(imp);
        }
    }
    //CHECKSTYLE:ON
}
