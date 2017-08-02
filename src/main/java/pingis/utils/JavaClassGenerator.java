
package pingis.utils;

import java.util.List;
import pingis.entities.Challenge;
import pingis.entities.Task;

public class JavaClassGenerator {
    private static final String classEnd = "\n}";

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
