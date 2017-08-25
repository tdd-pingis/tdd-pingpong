package pingis.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParseStart;
import com.github.javaparser.Position;
import com.github.javaparser.Problem;
import com.github.javaparser.Providers;
import com.github.javaparser.ast.CompilationUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    ParseResult<CompilationUnit> parseResult = new JavaParser().parse(ParseStart.COMPILATION_UNIT,
        Providers.provider(code));

    if (parseResult.isSuccessful()) {
      return null;
    }

    return parseResult.getProblems().stream().map((prob) ->
        prob.getVerboseMessage()
    ).toArray(String[]::new);
  }

  private static SyntaxError convertProblem(Problem problem) {
    Position faultyToken = problem.getLocation().get().getBegin().getRange().begin;

    // Ace editor expects line numbers to be zero-based while JavaParser starts from 1.
    int row = faultyToken.line - 1;
    int column = faultyToken.column;
    String text = problem.getMessage();

    return new SyntaxError(row, column, text);
  }

  public static Optional<List<SyntaxError>> getSyntaxErrors(String code) {
    ParseResult<CompilationUnit> parseResult = new JavaParser().parse(ParseStart.COMPILATION_UNIT,
        Providers.provider(code));

    if (parseResult.isSuccessful()) {
      return Optional.empty();
    }

    return Optional.of(parseResult.getProblems().stream()
        .map(JavaSyntaxChecker::convertProblem)
        .collect(Collectors.toList()));
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
