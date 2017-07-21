package pingis.services;

import pingis.entities.Challenge;
import pingis.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pingis.repositories.TaskRepository;
import pingis.repositories.ChallengeRepository;

@Component
public class DataLoader implements ApplicationRunner {

    private ChallengeRepository cr;
    private TaskRepository tr;

    @Autowired
    public DataLoader(ChallengeRepository cr, TaskRepository tr) {
        this.cr = cr;
        this.tr = tr;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        
    //CHECKSTYLE:OFF
        
        // Challenge 1 Description        
        Challenge challenge1 = new Challenge(
                "Calculator", "Test and implement a simple text-based calculator.");        
        
        // Tasks
        Task task1 = new Task(
                "Test multiplication", "Write a JUnit test case that tests the multiplication method for two integers. \n"
                        + "Method should take two integers as arguments and return the product of those as an integer.");
        task1.setCode(
                "@Test\npublic void testMultiplication() {\n//TODO: implement this\n\n}");        
        task1.setLevel(1);
        
        Task task2 = new Task(
                "Test addition", "Write a JUnit test case that tests the addition method for two integers. \n"
                        + "Method should take two integers as arguments and return the addition of those as an integer.");
        task2.setCode(
                "@Test\npublic void testAddition() {\n//TODO: implement this\n\n}");
        task2.setLevel(1);
        
        // Save tasks
        
        challenge1.addTask(task1);
        challenge1.addTask(task2);
        task1.setChallenge(challenge1);
        task2.setChallenge(challenge1);
        cr.save(challenge1);
        tr.save(task1);
        tr.save(task2);
        
        // Save Challenge
        
        
    }
}
