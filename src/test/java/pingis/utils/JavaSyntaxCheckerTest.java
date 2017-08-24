package pingis.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Created by dwarfcrank on 7/21/17.
 */
public class JavaSyntaxCheckerTest {

  @Test
  public void testValidClassDeclaration() {
    final String code = "public class TestClass { }";

    String[] errors = JavaSyntaxChecker.parseCode(code);
    assertNull(errors);
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

    String[] errors = JavaSyntaxChecker.parseCode(code);

    final String[] expected = new String[]{
        "(line 1,col 50) Parse error. Found  \"z\" <IDENTIFIER>, expected one of  "
            + "\",\" \";\" \"=\" \"@\" \"[\"",
        "(line 1,col 83) Parse error. Found \"}\", expected one of  \"!=\" \"%\" \"%=\" "
            + "\"&\" \"&&\" \"&=\" \")\" \"*\" \"*=\" \"+\" \"+=\" \",\" \"-\" \"-=\" "
            + "\"->\" \"/\" \"/=\" \"::\" \"<\" \"<<=\" \"<=\" \"=\" \"==\" \">\" \">=\" "
            + "\">>=\" \">>>=\" \"?\" \"^\" \"^=\" \"instanceof\" \"|\" \"|=\" \"||\"",
        "(line 1,col 90) Parse error. Found <EOF>, expected \"}\""
    };

    assertArrayEquals(expected, errors);
  }

  @Test
  public void testInvalidClassDeclaration() {
    final String code = "public class Broken {";
    final String[] expected = new String[]{
        "(line 1,col 21) Parse error. Found <EOF>, expected one of  \";\" \"<\" \"@\" \"abstract\" "
            + "\"boolean\" \"byte\" \"char\" \"class\" \"default\" \"double\" \"enum\" \"exports\" "
            + "\"final\" \"float\" \"int\" \"interface\" \"long\" \"module\" \"native\" \"open\" "
            + "\"opens\" \"private\" \"protected\" \"provides\" \"public\" \"requires\" \"short\" "
            + "\"static\" \"strictfp\" \"synchronized\" \"to\""
            + " \"transient\" \"transitive\" \"uses\" "
            + "\"void\" \"volatile\" \"with\" \"{\" \"}\" <IDENTIFIER>"
    };

    String[] errors = JavaSyntaxChecker.parseCode(code);

    assertArrayEquals(expected, errors);
  }
}
