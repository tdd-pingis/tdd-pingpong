package pingis.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.Test;

/**
 * Created by dwarfcrank on 7/21/17.
 */
public class JavaSyntaxCheckerTest {

  @Test
  public void testValidClassDeclaration() {
    final String code = "public class TestClass { }";

    Optional<List<SyntaxError>> errors = JavaSyntaxChecker.getSyntaxErrors(code);
    assertFalse(errors.isPresent());
  }

  @Test
  public void testMultipleErrors() {
    final String code = "public class TestClass {"
        + "public void broken1() {"
        + "x y z;"
        + "}"
        + "public void broken2() {"
        + "hurr(\"durr\""
        + "}"
        + "}";

    Optional<List<SyntaxError>> errorsOpt = JavaSyntaxChecker.getSyntaxErrors(code);
    assertTrue(errorsOpt.isPresent());

    final int[] expectedRows = new int[] { 0, 0, 0 };
    final int[] expectedColumns = new int[] { 50, 83, 90 };

    List<SyntaxError> errors = errorsOpt.get();
    assertEquals(3, errors.size());

    for (int i = 0; i < errors.size(); i++) {
      SyntaxError error = errors.get(i);

      assertEquals(expectedRows[i], error.row);
      assertEquals(expectedColumns[i], error.column);
      assertTrue(error.text.startsWith("Parse error"));
    }
  }

  @Test
  public void testInvalidClassDeclaration() {
    final String code = "public class Broken {";

    Optional<List<SyntaxError>> errorsOpt = JavaSyntaxChecker.getSyntaxErrors(code);
    assertTrue(errorsOpt.isPresent());

    List<SyntaxError> errors = errorsOpt.get();
    assertEquals(1, errors.size());

    assertEquals(0, errors.get(0).row);
    assertEquals(21, errors.get(0).column);
    assertTrue(errors.get(0).text.startsWith("Parse error"));
  }
}
