
package pingis.services;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pingis.entities.Challenge;
import pingis.entities.Task;

public class JavaClassGeneratorTest {
    
    private Challenge challenge1;
    private Task task1;
    private Task task2;
    private Task task3;
    private JavaClassGenerator jparser;
    private final int Challenge1Level = 3;
    
    public JavaClassGeneratorTest() {
        challenge1 = new Challenge("Immutable Calculator", "Amazing immutable calculator.");
        jparser = new JavaClassGenerator();
        
        task1 = new Task("testAddition",
                "test addition of two integers, return single value", 
                "@Test\npublic void testAddition() {\n\t//TODO: implement this\n\n}", 
                Challenge1Level, 0);
        
        task2 = new Task("testSubstraction",
                "test substraction of two integers, return single value", 
                "@Test\npublic void testSubstraction() {\n\t//TODO: implement this\n\n}", 
                Challenge1Level, 0);
        
        task3 = new Task("testMultiplication",
                "test multiplication of two integers, return single value", 
                "@Test\npublic void testMultiplication() {\n\t//TODO: implement this\n\n}", 
                Challenge1Level, 0);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testParseChallengeWithOneTask() {
        String parsedChallenge1Task1 = "\npublic class ImmutableCalculatorTest {\n"
                + "\n"
                + "	@Test\n"
                + "	public void testAddition() {\n"
                + "		//TODO: implement this\n"
                + "	\n"
                + "	}\n"
                + "\n"
                + "\n"
                + "}";
        
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(task1);
        
        assertEquals(parsedChallenge1Task1, jparser.parseChallenge(challenge1, tasks));
    }
    
    @Test
    public void testParseChallengeWithTwoTasks() {
        String parsedChallenge1TwoTasks = "\npublic class ImmutableCalculatorTest {\n"
                + "\n"
                + "	@Test\n"
                + "	public void testAddition() {\n"
                + "		//TODO: implement this\n"
                + "	\n"
                + "	}\n"
                + "\n"
                + "	@Test\n"
                + "	public void testSubstraction() {\n"
                + "		//TODO: implement this\n"
                + "	\n"
                + "	}\n"
                + "\n"
                + "\n"
                + "}";
        
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(task1);
        tasks.add(task2);
 
        assertEquals(parsedChallenge1TwoTasks, jparser.parseChallenge(challenge1, tasks));
    }
    
    @Test
    public void testParseChallengeWithThreeTasks() {
        String parsedChallenge1TwoTasks = "\npublic class ImmutableCalculatorTest {\n"
                + "\n"
                + "	@Test\n"
                + "	public void testAddition() {\n"
                + "		//TODO: implement this\n"
                + "	\n"
                + "	}\n"
                + "\n"
                + "	@Test\n"
                + "	public void testSubstraction() {\n"
                + "		//TODO: implement this\n"
                + "	\n"
                + "	}\n"
                + "\n"
                + "	@Test\n"
                + "	public void testMultiplication() {\n"
                + "		//TODO: implement this\n"
                + "	\n"
                + "	}\n"
                + "\n"
                + "\n"
                + "}";
        
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        assertEquals(parsedChallenge1TwoTasks, jparser.parseChallenge(challenge1, tasks));
    }
    
}
