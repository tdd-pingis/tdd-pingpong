package pingis.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CodeStubBuilderTest {

  private static final String EMPTY_CLASS =
      "public class EmptyClass {\n"
          + "}\n";

  private static final String IMPORT_CLASS =
      "import foo.bar.baz;\n"
          + "import heh.ebin;\n\n"
          + "public class ImportClass {\n}\n";

  private static final String COMMENT_CLASS =
      "public class CommentClass {\n"
          + "    // TODO: add methods here\n"
          + "}\n";

  @Test
  public void testEmpty() {
    String code = new CodeStubBuilder("EmptyClass").build();

    assertEquals(EMPTY_CLASS, code);
  }

  @Test
  public void testWithImports() {
    String code = new CodeStubBuilder("ImportClass")
        .withImport("foo.bar.baz")
        .withImport("heh.ebin")
        .build();

    assertEquals(IMPORT_CLASS, code);
  }

  @Test
  public void testWithComment() {
    String code = new CodeStubBuilder("CommentClass")
        .withBodyComment("TODO: add methods here")
        .build();

    assertEquals(COMMENT_CLASS, code);
  }
}