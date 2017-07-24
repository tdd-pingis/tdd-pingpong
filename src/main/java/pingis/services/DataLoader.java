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
//CHECKSTYLE:OFF
    private ChallengeRepository cr;
    private TaskRepository tr;

    @Autowired
    public DataLoader(ChallengeRepository cr, TaskRepository tr) {
        this.cr = cr;
        this.tr = tr;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        
    
        
        // Challenge 1 Description        
        Challenge challenge1 = new Challenge(
                "Calculator", "Test and implement a simple text-based calculator.");        

        String bodyStart = "public class CalculatorTest {\n";
        String bodyEnd = "\n}";
        
        
        // Tasks
        Task task1 = new Task(
                "Test multiplication", "Write a JUnit test case that tests the multiplication method for two integers. \n"
                        + "Method should take two integers as arguments and return the product of those as an integer.");
        task1.setCode(bodyStart+
                "\t@Test\n"+
                "\tpublic void testMultiplication() {\n"+
                "\t\t//TODO: implement this\n"+
                "\t\t\n"+
                "\t}"
                +bodyEnd);        
        task1.setLevel(1);
        
        
        
        Task task2 = new Task(
                "Test addition", "Write a JUnit test case that tests the addition method for two integers. \n"
                + "Method should take two integers as arguments and return the addition of those as an integer.");
        task2.setCode(bodyStart+
                "\t@Test\n"+
                "\tpublic void testAddition() {\n"+
                "\t\t//TODO: implement this\n"+
                "\t\t\n"+
                "\t}"
                +bodyEnd);
        task2.setLevel(1);

        
        
        Task task3 = new Task(
                "Test substraction", "Write a JUnit test case that tests the substraction method for two integers. \n"
                + "Method should take two integers as arguments and return the substraction of the latter from the former as integer.");
        task3.setCode(bodyStart+
                "\t@Test\n"+
                "\tpublic void testSubstraction() {\n"+
                "\t//TODO: implement this\n"+
                "\t\n"+
                "\t}"
                +bodyEnd);
        task3.setLevel(1);

        
        
        Task task4 = new Task(
                "Test integer division", "Write a JUnit test case that tests division method for two integers. \n"
                + "Method should take two integers (former as dividend and latter as divisor) as arguments and return the quotient as integer.");
        task4.setCode(bodyStart+
                "\t@Test\n"+
                "\tpublic void testIntegerDivision() {\n"+
                "\t\t//TODO: implement this\n"+
                "\t\t\n"+
                "\t}"
                +bodyEnd);
        task4.setLevel(2);

        
        
        Task task5 = new Task(
                "Test integer modulo", "Write a JUnit test case that tests the modulo method for two integers. \n"
                + "Method should take two integers (former as dividend and latter as divisor) as arguments and return the remainder of the division as integer.");
        task5.setCode(bodyStart+
                "\t@Test\n"+
                "\tpublic void testIntegerModulo() {\n"+
                "\t\t//TODO: implement this\n"+
                "\t\t\n"+
                "\t}"
                +bodyEnd);
        task5.setLevel(2);

        
        
        Task task6 = new Task(
                "Test integer exponentiation", "Write a JUnit test case that tests the exponentiation of two integers. \n"
                + "Method should take two integers (former as base and latter as exponent) as arguments and return the product of the exponentiation as integer.");
        task6.setCode(bodyStart+
                "\t@Test\n"+
                "\tpublic void testIntegerModulo() {\n"+
                "\t\t//TODO: implement this\n"+
                "\t\t\n"+
                "\t}"
                +bodyEnd);
        task6.setLevel(2);

        
        
        Task task7 = new Task(
                "Test euclidean algorithm", "Write a JUnit test case that tests euclidean algorithm. \n"
                + "Method should take two integers as arguments and return their greatest common divisor.");
        task7.setCode(bodyStart+
                "\t@Test\n"+
                "\tpublic void testIntegerModulo() {\n"+
                "\t\t//TODO: implement this\n"+
                "\t\t\n"+
                "\t}"
                +bodyEnd);
        task7.setLevel(10);
        
        // Save tasks

        challenge1.addTask(task1);
        challenge1.addTask(task2);
        challenge1.addTask(task3);
        challenge1.addTask(task4);
        challenge1.addTask(task5);
        challenge1.addTask(task6);
        challenge1.addTask(task7);
        task1.setChallenge(challenge1);
        task2.setChallenge(challenge1);
        task3.setChallenge(challenge1);
        task4.setChallenge(challenge1);
        task5.setChallenge(challenge1);
        task6.setChallenge(challenge1);
        task7.setChallenge(challenge1);
        cr.save(challenge1);
        tr.save(task1);
        tr.save(task2);
        tr.save(task3);
        tr.save(task4);
        tr.save(task5);
        tr.save(task6);
        tr.save(task7);
        
        
    
    
        
    }
//CHECKSTYLE:ON
}
