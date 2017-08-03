
package pingis.services;

import antlr.JavaCodeGenerator;
import pingis.utils.JavaClassGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.*;
import pingis.entities.Challenge;
import pingis.entities.ChallengeType;
import pingis.entities.ImplementationType;
import pingis.entities.Task;
import pingis.entities.User;

public class JavaClassGeneratorTest {
    
    private Challenge challenge1;
    private Task task1;
    private Task task2;
    private Task task3;
    private User authorUser;
    private static final int TMC_USER_LEVEL = 100;
    private final int Challenge1Level = 3;
    
    public JavaClassGeneratorTest() {
        challenge1 = new Challenge("Immutable Calculator", authorUser, 
                                   "Amazing immutable calculator.", ChallengeType.PROJECT);

        User testUser = new User(new Random().nextLong(), "Test_userfirst", Challenge1Level);
        
        task1 = new Task(0, ImplementationType.TEST, testUser, "testAddition",
                "test addition of two integers, return single value", 
                "@Test\npublic void testAddition() {\n\t//TODO: implement this\n\n}", 
                Challenge1Level, 0);
        
        task2 = new Task(1, ImplementationType.TEST, testUser, "testSubstraction",
                "test substraction of two integers, return single value", 
                "@Test\npublic void testSubstraction() {\n\t//TODO: implement this\n\n}", 
                Challenge1Level, 0);
        
        task3 = new Task(2, ImplementationType.TEST, testUser, "testMultiplication",
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
        
        assertEquals(parsedChallenge1Task1, JavaClassGenerator.generateChallenge(challenge1, tasks));
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
 
        assertEquals(parsedChallenge1TwoTasks, JavaClassGenerator.generateChallenge(challenge1, tasks));
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
        assertEquals(parsedChallenge1TwoTasks, JavaClassGenerator.generateChallenge(challenge1, tasks));
    }
    
    @Test
    public void testGenerateImplClassName() {
        final String expectedName = "src/ImmutableCalculator.java";
        assertThat(JavaClassGenerator.generateImplClassName(challenge1)).isEqualTo(expectedName);
    }
    
    @Test
    public void testGenerateImplClassNameWithOneNumber() {
        final String expectedName = "src/ImmutableCalculator.java";
        challenge1.setName("Immutable Calculator" + "9");
        assertThat(JavaClassGenerator.generateImplClassName(challenge1)).isEqualTo(expectedName);
    }
    
    @Test
    public void testGenerateImplClassNameWithNumbers() {
        final String expectedName = "src/ImmutableCalculator.java";
        challenge1.setName("Immutable Calculator" + "91223");
        assertThat(JavaClassGenerator.generateImplClassName(challenge1)).isEqualTo(expectedName);
    }
    
    @Test
    public void testGenerateImplClassNameWithNumbersAtTheBeginningAndInEnd() {
        final String expectedName = "src/ImmutableCalculator.java";
        challenge1.setName("02" + "Immutable Calculator" + "91223");
        assertThat(JavaClassGenerator.generateImplClassName(challenge1)).isEqualTo(expectedName);
    }
    
    @Test
    public void testGenerateImplClassNameTooLong() {
        final String expectedName = "src/ImmutableCalculatorImmutableCalculat.java";
        challenge1.setName("Immutable Calculator Immutable Calculator Immutable Calculator");
        assertThat(JavaClassGenerator.generateImplClassName(challenge1)).isEqualTo(expectedName);
    }
    
    @Test
    public void testGenerateImplClassNamePath() {
        final String unexpectedName = "ImmutableCalculator.java";
        assertThat(JavaClassGenerator.generateImplClassName(challenge1)).isNotEqualTo(unexpectedName);
    }
    
    @Test
    public void testGenerateTestClassNameWithOneNumber() {
        final String expectedName = "test/ImmutableCalculatorTest.java";
        challenge1.setName("Immutable Calculator" + "9");
        assertThat(JavaClassGenerator.generateTestClassName(challenge1)).isEqualTo(expectedName);
    }

    @Test
    public void testGenerateTestClassNameWithNumbers() {
        final String expectedName = "test/ImmutableCalculatorTest.java";
        challenge1.setName("Immutable Calculator" + "91223");
        assertThat(JavaClassGenerator.generateTestClassName(challenge1)).isEqualTo(expectedName);
    }

    @Test
    public void testGenerateTestClassNameTooLong() {
        final String expectedName = "test/ImmutableCalculatorImmutableCalculatTest.java";
        challenge1.setName("Immutable Calculator Immutable Calculator Immutable Calculator");
        assertThat(JavaClassGenerator.generateTestClassName(challenge1)).isEqualTo(expectedName);
    }
    
    @Test
    public void testGenerateTestClassName() {
        final String expectedName = "test/ImmutableCalculatorTest.java";
        assertThat(JavaClassGenerator.generateTestClassName(challenge1)).isEqualTo(expectedName);
    }
    
    @Test
    public void testGenerateTestClassNamePath() {
        final String unexpectedName = "ImmutableCalculatorTest.java";
        assertThat(JavaClassGenerator.generateTestClassName(challenge1)).isNotEqualTo(unexpectedName);
    }
    
}
