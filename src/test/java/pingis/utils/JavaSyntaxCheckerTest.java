package pingis.utils;

import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testInvalidClassDeclaration() {
        final String code = "public class Broken {";
        final String[] expected = new String[] {
                "(line 1,col 21) Parse error. Found <EOF>, expected one of  \";\" \"<\" \"@\" \"abstract\" "
                        + "\"boolean\" \"byte\" \"char\" \"class\" \"default\" \"double\" \"enum\" \"exports\" "
                        + "\"final\" \"float\" \"int\" \"interface\" \"long\" \"module\" \"native\" \"open\" "
                        + "\"opens\" \"private\" \"protected\" \"provides\" \"public\" \"requires\" \"short\" "
                        + "\"static\" \"strictfp\" \"synchronized\" \"to\" \"transient\" \"transitive\" \"uses\" "
                        + "\"void\" \"volatile\" \"with\" \"{\" \"}\" <IDENTIFIER>"
        };

        String[] errors = JavaSyntaxChecker.parseCode(code);

        assertArrayEquals(expected, errors);
    }
}
