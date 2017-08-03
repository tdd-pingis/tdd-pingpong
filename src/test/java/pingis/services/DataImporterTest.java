package pingis.services;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pingis.entities.Challenge;
import pingis.entities.ChallengeImplementation;
import pingis.entities.Task;
import pingis.entities.TaskImplementation;
import pingis.entities.User;

public class DataImporterTest {

    private DataImporter importer;
    private IOStub io;

    public DataImporterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.importer = new DataImporter();
        this.io = new IOStub();
        importer.readData(io);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testReadData() {
        assertTrue(importer.getJsonString() != null);
    }

    @Test
    public void testGenerateUsers() {
        importer.generateUsers();
        HashMap<String, User> users = importer.getUsers();
        assertEquals(users.get("testuser").getName(), DataLoader.UserType.TEST_USER.name());
        assertEquals(users.get("modeluser").getName(), DataLoader.UserType.TMC_MODEL_USER.name());
        assertEquals(users.get("impluser").getName(), DataLoader.UserType.IMPLEMENTATION_USER.name());
    }

    @Test
    public void testGenerateEntities() {
        importer.generateUsers();
        importer.generateEntities();
        ArrayList<Challenge> challenges = importer.getChallenges();
        ArrayList<Task> tasks = importer.getTasks();
        ArrayList<TaskImplementation> taskImplementations = importer.getTaskImplementations();
        ArrayList<ChallengeImplementation> challengeImplementations = importer.getChallengeImplementations();

        assertEquals(challenges.get(0).getName(), "calculator");
        assertEquals(tasks.get(0).getName(), "Test multiplication");
        assertEquals(taskImplementations.get(0).getTask(), tasks.get(0));
        assertEquals(challengeImplementations.get(0).getChallenge(), challenges.get(0));
    }

}
