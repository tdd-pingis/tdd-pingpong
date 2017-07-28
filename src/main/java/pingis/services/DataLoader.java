package pingis.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import pingis.entities.Challenge;
import pingis.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pingis.entities.TaskImplementation;
import pingis.repositories.TaskRepository;
import pingis.repositories.ChallengeRepository;

@Component
public class DataLoader implements ApplicationRunner {
    private ChallengeRepository cr;
    private TaskRepository tr;
    private LinkedHashMap<Challenge, ArrayList<Task>> challenges;

    @Autowired
    public DataLoader(ChallengeRepository cr, TaskRepository tr) {
        this.cr = cr;
        this.tr = tr;
        this.challenges = new LinkedHashMap<>();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        generateChallenges();
        populateDB();
    }
    
//CHECKSTYLE:OFF

    private void generateChallenges() {        
        ArrayList<Task> tasks = new ArrayList<>();
        
        // Challenge 1        
        Challenge challenge1 = new Challenge(
                "Calculator", "Test and implement a simple text-based calculator.");        
               
        // Challenge 1 Tasks
        
        tasks.add(new Task(tasks.size()-1,
                        "Test multiplication", "Write a JUnit test case that tests the multiplication method for two integers. \n"
                        + "Method should take two integers as arguments and return the product of those as an integer." 
                        , "@Test\npublic void testMultiplication() {\n\t//TODO: implement this\n\n}"
                        , 1
                        , 0
                ));
        
        // id = 1
        tasks.add(new Task(tasks.size()-1,
                        "Test addition", "Write a JUnit test case that tests the addition method for two integers. \n"
                        + "Method should take two integers as arguments and return the addition of those as an integer." 
                        , "@Test\npublic void testAddition() {\n\t//TODO: implement this\n\n}"
                        , 1
                        , 0
                ));
        
        // id = 2
        tasks.add(new Task(tasks.size()-1,
                        "Test substraction", "Write a JUnit test case that tests the substraction method for two integers. \n"
                        + "Method should take two integers as arguments and return the substraction of the latter from the former as integer."
                        , "@Test\npublic void testSubstraction() {\n\t//TODO: implement this\n\n}"
                        , 1
                        , 0
                ));
        
        // id = 3
        tasks.add(new Task(tasks.size()-1,
                        "Test integer division", "Write a JUnit test case that tests division method for two integers. \n"
                        + "Method should take two integers (former as dividend and latter as divisor) as arguments and return the quotient as integer."
                        , "@Test\npublic void testIntegerDivision() {\n\t//TODO: implement this\n\n}"
                        , 2
                        , 0
                ));
        
        // id = 4
        tasks.add(new Task(tasks.size()-1,
                        "Test integer modulo", "Write a JUnit test case that tests the modulo method for two integers. \n"
                        + "Method should take two integers (former as dividend and latter as divisor) as arguments and return the remainder of the division as integer."
                        , "@Test\npublic void testIntegerModulo() {\n\t//TODO: implement this\n\n}"
                        , 2
                        , 0
                ));
        
        // id = 5
        tasks.add(new Task(tasks.size()-1,
                        "Test integer exponentiation", "Write a JUnit test case that tests the exponentiation of two integers. \n"
                        + "Method should take two integers (former as base and latter as exponent) as arguments and return the product of the exponentiation as integer."
                        , "@Test\npublic void testIntegerModulo() {\n\t//TODO: implement this\n\n}"
                        , 2
                        , 0
                ));
        
        // id = 6
        tasks.add(new Task(tasks.size()-1,
                        "Test euclidean algorithm", "Write a JUnit test case that tests euclidean algorithm. \n"
                        + "Method should take two integers as arguments and return their greatest common divisor."
                        , "@Test\npublic void testIntegerModulo() {\n\t//TODO: implement this\n\n}"
                        , 10
                        , 0
                ));
        
        this.challenges.put(challenge1, tasks);
    }
          
//CHECKSTYLE:ON
    
    private void populateDB() {
        for (Challenge challenge : this.challenges.keySet()) {
            for (Task task : this.challenges.get(challenge)) {
                challenge.addTask(task);
                task.setChallenge(challenge);
            }
            cr.save(challenge);
            for (Task task : this.challenges.get(challenge)) {
                tr.save(task);
            }
        }
    }
}
