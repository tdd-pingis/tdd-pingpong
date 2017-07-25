/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author mrreflex
 */
public class JavaClassParserTest {
    
    private Challenge challenge1;
    private Task task1;
    private JavaClassParser jparser;
    
    public JavaClassParserTest() {
        challenge1 = new Challenge("Immutable Calculator", "Amazing immutable calculator.");
        task1 = new Task("testAddition",
                "test addition of two integers, return single value", 
                "@Test\npublic void testAddition() {\n\t//TODO: implement this\n\n}", 
                3, 0);
        jparser = new JavaClassParser();
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

    /**
     * Test of parseChallenge method, of class JavaClassParser.
     */
    @Test
    public void testParseChallengeWithOneTask() {
        String parsedChallenge1Task1 = "public class CalculatorTest {\n"
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
        assertEquals(jparser.parseChallenge(challenge1, tasks), parsedChallenge1Task1);
    }
    
}
