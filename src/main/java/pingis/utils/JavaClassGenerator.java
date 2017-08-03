
package pingis.utils;

import java.util.List;
import pingis.entities.Challenge;
import pingis.entities.Task;

/**
 * Generates complete Java class-templates with proper headings out of
 * individual tasks. Also includes methods for generation of proper
 * Java-classnames (both for test-classes and for normal Java classes).
 */
public class JavaClassGenerator {
    private static final String classEnd = "\n}";
    private final static int MAX_LINE_LENGTH = 40;

    public static String generateChallenge(Challenge challenge, List<Task> tasks) {
        String classCode = "\n" + generateTestClassHeader(challenge);
        classCode += "\n\n" + indentateTaskAsMethod(tasks);
        classCode += classEnd;
        
        // before return replace all \n with system-independent line-separators
        classCode.replace("\n", System.getProperty("line.separator"));
        
        return classCode;
    } 
    
    private static String generateTestClassHeader(Challenge c) {
        return "public class " + c.getName().replaceAll("\\s+","") + "Test {";
    }
    
    public static String generateImplClassName(Challenge challenge) {
        String classFilename = returnCorrectLength(challenge.getName());
        return "src/" + classFilename.replaceAll("[\\d\\s+]","") + ".java";
    }
    
    public static String generateTestClassName(Challenge challenge) {
        String classFilename = returnCorrectLength(challenge.getName());
        return "test/" + classFilename.replaceAll("[\\d\\s+]","") + "Test.java";
    }
    
    private static String returnCorrectLength(String string) {
        return (string.length() > MAX_LINE_LENGTH) ? string.substring(0, MAX_LINE_LENGTH-1) : string;
    }
    
    private static String generateImplementationClassHeader(Challenge c) {
        return "public class " + c.getName() + " {";
    }

    private static String indentateTaskAsMethod(List<Task> tasks) {
        String codeSegment = "";
        
        for (Task task : tasks) {
            String[] lines = task.getCodeStub().split(System.getProperty("line.separator"));
            for (String line : lines) {
                codeSegment += "\t" + line + "\n";
            }
            codeSegment += "\n";
        }
        
        return codeSegment;
    }
    
}
