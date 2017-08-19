package pingis.utils;

import java.util.List;
import pingis.entities.Challenge;
import pingis.entities.Task;

/**
 * Generates complete Java class-templates with proper headings out of individual tasks. Also
 * includes methods for generation of proper Java-classnames (both for test-classes and for normal
 * Java classes).
 */
public class JavaClassGenerator {

  private static final String classEnd = "\n}";
  private static final int MAX_LINE_LENGTH = 40;

  public static String generateChallenge(Challenge challenge, List<Task> tasks) {
    String classCode = "\n" + generateTestClassHeader(challenge);
    classCode += "\n\n" + indentateTaskAsMethod(tasks);
    classCode += classEnd;

    // before return replace all \n with system-independent line-separators
    classCode.replace("\n", System.getProperty("line.separator"));

    return classCode;
  }

  private static String generateTestClassHeader(Challenge c) {
    return "public class " + c.getName().replaceAll("\\s+", "") + "Test {";
  }

  public static String generateImplClassFilename(Challenge challenge)
      throws IllegalArgumentException {
    checkNameLength(challenge.getName());
    return "src/" + challenge.getName().replaceAll("[\\s+]", "") + ".java";
  }

  public static String generateTestClassFilename(Challenge challenge)
      throws IllegalArgumentException {
    checkNameLength(challenge.getName());
    return "test/" + challenge.getName().replaceAll("[\\s+]", "") + "Test.java";
  }

  private static void checkNameLength(String string) throws IllegalArgumentException {
    if (string.length() > MAX_LINE_LENGTH) {
      throw new IllegalArgumentException(
              "Given string is too long. max length is " + MAX_LINE_LENGTH);
    }
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
