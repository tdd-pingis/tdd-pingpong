package pingis.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Helper class for checking the syntax of Java code. Created by dwarfcrank on 7/21/17.
 */
public class JavaSyntaxChecker {

  /**
   * Parses the code to check its syntax.
   *
   * @param code Code to parse. Expects a whole Java compilation unit (i.e. package
   * declarations/imports, class definitions, etc.).
   * @return Array of strings describing the encountered parse errors, or null if the syntax is
   * correct.
   */
  public static String[] parseCode(String code) {
    try {
      CompilationUnit cu = JavaParser.parse(code);
    } catch (ParseProblemException parseProblems) {
      return parseProblems.getProblems().stream().map((prob) ->
          prob.getVerboseMessage()
      ).toArray(String[]::new);
    }

    return null;
  }
  
  public static String[] checkTaskPairErrors(String submissionCode, String staticCode) {
    String[] submissionSyntaxErrors = parseCode(submissionCode);
    String[] staticSyntaxErrors = parseCode(staticCode);
    if (submissionSyntaxErrors != null || staticSyntaxErrors != null) {
      String[] errors = ArrayUtils.addAll(submissionSyntaxErrors, staticSyntaxErrors);
      return errors;
    }
    return null;
  }
}
