
package pingis.services;

import java.util.List;
import pingis.entities.Challenge;
import pingis.entities.Task;

public class JavaClassGenerator {

    public JavaClassGenerator() {
    }   
    
    public String parseChallenge(Challenge challenge, List<Task> tasks) {
        String classCode = "\n" + generateTestClassHeader(challenge);
        classCode += "\n\n" + indentateTaskAsMethod(tasks);
        classCode += generateClassEnd();
        
        // before return replace all \n with system-independent line-separators
        classCode.replace("\n", System.getProperty("line.separator"));
        
        return classCode;
    } 
    
    private String generateTestClassHeader(Challenge c) {
        return "public class " + c.getName().replaceAll("\\s+","") + "Test {";
    }
    
    private String generateImplementationClassHeader(Challenge c) {
        return "public class " + c.getName() + " {";
    }
    
    private String generateClassEnd() {
        return "\n}";
    }
    
    private String indentateTaskAsMethod(List<Task> tasks) {
        String codeSegment = "";
        
        for (Task task : tasks) {
            String[] lines = task.getCode().split(System.getProperty("line.separator"));
            for (String line : lines) {
                codeSegment += "\t" + line + "\n";
            }
            codeSegment += "\n";
        }
        
        return codeSegment;
    }
    
}
