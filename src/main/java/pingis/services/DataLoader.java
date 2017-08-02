package pingis.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import pingis.entities.Challenge;
import pingis.entities.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pingis.entities.ChallengeImplementation;
import pingis.entities.ImplementationType;
import pingis.entities.TaskImplementation;
import pingis.entities.User;
import pingis.repositories.ChallengeImplementationRepository;
import pingis.repositories.TaskRepository;
import pingis.repositories.ChallengeRepository;
import pingis.repositories.TaskImplementationRepository;
import pingis.repositories.UserRepository;

@Component
public class DataLoader implements ApplicationRunner {
    private ChallengeRepository cr;
    private ChallengeImplementationRepository cir;
    private TaskRepository tr;
    private UserRepository ur;
    private TaskImplementationRepository tir;
    
    private Challenge calculatorChallenge;
    private List<User> users;
    private LinkedHashMap<Challenge, ArrayList<Task>> challenges;
    private List<ChallengeImplementation> challengeImplementations;
    private List<TaskImplementation> taskImplementations;
    
    public enum UserType {
        TMC_MODEL_USER(0), TEST_USER(1), IMPLEMENTATION_USER(2);
        private final long id;
        
        private UserType(long id) {
            this.id = id;
        }
        
        public long getId() { 
            return id; 
        }
    }

    @Autowired
    public DataLoader(ChallengeRepository cr, TaskRepository tr, UserRepository ur, 
            ChallengeImplementationRepository cir, TaskImplementationRepository tir) {
        this.cr = cr;
        this.tr = tr;
        this.ur = ur;
        this.cir = cir;
        this.tir = tir;
        this.users = new ArrayList<>();
        this.challenges = new LinkedHashMap<>();
        this.challengeImplementations = new ArrayList<>();
        this.taskImplementations = new ArrayList<>();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        generateUsers();
        generateChallenges();
        generateChallengeImplementations();
        generateTaskImplementations();
        populateDB();
    }
    //CHECKSTYLE:OFF  
    
    private void generateUsers() {
        long userId = new Random().nextInt(Integer.MAX_VALUE);
        
        this.users.add(new User(UserType.TMC_MODEL_USER.getId(), UserType.TMC_MODEL_USER.name(), 100)); 
        this.users.add(new User(UserType.TEST_USER.getId(), UserType.TEST_USER.name(), 5)); 
        this.users.add(new User(UserType.IMPLEMENTATION_USER.getId(), UserType.IMPLEMENTATION_USER.name(), 1));
    }

    private void generateChallenges() {        
        ArrayList<Task> tasks = new ArrayList<>();
        
        // Challenge 1        
        this.calculatorChallenge = new Challenge(
                "Calculator", users.get((int) UserType.TMC_MODEL_USER.getId()), "Test and implement a simple text-based calculator.",
                "src/Calculator.java", "test/CalculatorTest.java");
               
        // Challenge 1 Tasks, authored by Test_user1
        
        tasks.add(new Task(tasks.size()-1,
                        users.get((int) UserType.TMC_MODEL_USER.getId()),
                        "Test multiplication", "Write a JUnit test case that tests the multiplication method for two integers. \n"
                        + "Method should take two integers as arguments and return the product of those as an integer." 
                        , "@Test\npublic void testMultiplication() {\n\t//TODO: implement this\n\n}"
                        , 1
                        , 0
                ));
        
        // id = 1
        tasks.add(new Task(tasks.size()-1,
                        users.get((int) UserType.TMC_MODEL_USER.getId()),
                        "Test addition", "Write a JUnit test case that tests the addition method for two integers. \n"
                        + "Method should take two integers as arguments and return the addition of those as an integer." 
                        , "@Test\npublic void testAddition() {\n\t//TODO: implement this\n\n}"
                        , 1
                        , 0
                ));
        
        // id = 2
        tasks.add(new Task(tasks.size()-1,
                        users.get((int) UserType.TMC_MODEL_USER.getId()),
                        "Test substraction", "Write a JUnit test case that tests the substraction method for two integers. \n"
                        + "Method should take two integers as arguments and return the substraction of the latter from the former as integer."
                        , "@Test\npublic void testSubstraction() {\n\t//TODO: implement this\n\n}"
                        , 1
                        , 0
                ));
        
        // id = 3
        tasks.add(new Task(tasks.size()-1,
                        users.get((int) UserType.TMC_MODEL_USER.getId()),
                        "Test integer division", "Write a JUnit test case that tests division method for two integers. \n"
                        + "Method should take two integers (former as dividend and latter as divisor) as arguments and return the quotient as integer."
                        , "@Test\npublic void testIntegerDivision() {\n\t//TODO: implement this\n\n}"
                        , 2
                        , 0
                ));
        
        // id = 4
        tasks.add(new Task(tasks.size()-1,
                        users.get((int) UserType.TMC_MODEL_USER.getId()),
                        "Test integer modulo", "Write a JUnit test case that tests the modulo method for two integers. \n"
                        + "Method should take two integers (former as dividend and latter as divisor) as arguments and return the remainder of the division as integer."
                        , "@Test\npublic void testIntegerModulo() {\n\t//TODO: implement this\n\n}"
                        , 2
                        , 0
                ));
        
        // id = 5
        tasks.add(new Task(tasks.size()-1,
                        users.get((int) UserType.TMC_MODEL_USER.getId()),
                        "Test integer exponentiation", "Write a JUnit test case that tests the exponentiation of two integers. \n"
                        + "Method should take two integers (former as base and latter as exponent) as arguments and return the product of the exponentiation as integer."
                        , "@Test\npublic void testIntegerModulo() {\n\t//TODO: implement this\n\n}"
                        , 2
                        , 0
                ));
        
        // id = 6
        tasks.add(new Task(tasks.size()-1,
                        users.get((int) UserType.TMC_MODEL_USER.getId()),
                        "Test euclidean algorithm", "Write a JUnit test case that tests euclidean algorithm. \n"
                        + "Method should take two integers as arguments and return their greatest common divisor."
                        , "@Test\npublic void testIntegerModulo() {\n\t//TODO: implement this\n\n}"
                        , 10
                        , 0
                ));
        
        this.challenges.put(calculatorChallenge, tasks);
    }
    
    public void generateChallengeImplementations() {
        // Generate first ChallengeImplementation for Challenge: Calculator
        this.challengeImplementations.add(new ChallengeImplementation(
                calculatorChallenge, 
                users.get((int) UserType.TEST_USER.getId()),             // Challenge tester
                users.get((int) UserType.IMPLEMENTATION_USER.getId()))); // Challenge implementator
    }
    
    private void generateTaskImplementations() {
        // Generate first TaskImplementation for Challenge: Calculator
        String testCode = "public class CalculatorTest {\n"
                        + "    @Test\n"
                        + "    public void testAddition() {\n"
                        + "        Calculator calc = new Calculator();\n"
                        + "        assertEquals(23, calc.Addition(18, 5));\n"
                        + "    }\n"
                        + "}";

        TaskImplementation taskImplementation = new TaskImplementation(
                        users.get((int) UserType.TEST_USER.getId()),
                        testCode,
                        ImplementationType.TEST,
                        challenges.get(calculatorChallenge).get(0));

        this.taskImplementations.add(taskImplementation);
    }
          
    private void populateDB() {
        // Save all users
        for (User user : this.users) {
            ur.save(user);
        }
        
        // Save each challenge and given tasks
        for (Challenge challenge : this.challenges.keySet()) {
            for (Task task : this.challenges.get(challenge)) {
                challenge.addTask(task);
                task.setChallenge(challenge);
            }
            cr.save(challenge); 
        }
        
        // Save all ChallengeImplementations and given TaskImplementations
        for (ChallengeImplementation chaImp : this.challengeImplementations) {
            for (TaskImplementation taskImp : this.taskImplementations) {
                challenges.get(calculatorChallenge).get(0).addImplementation(taskImp);
                chaImp.addTaskImplementation(taskImp);
                taskImp.setChallengeImplementation(chaImp);
            }
            cir.save(chaImp);
        }
    }
    //CHECKSTYLE:ON
}
