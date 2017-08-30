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
    CodeStub stub = new CodeStubBuilder("EmptyClass").build();

    assertEquals(EMPTY_CLASS, stub.code);
    assertEquals("src/EmptyClass.java", stub.filename);
    assertEquals("EmptyClass", stub.className);
  }

  @Test
  public void testWithImports() {
    CodeStub stub = new CodeStubBuilder("ImportClass")
        .withImport("foo.bar.baz")
        .withImport("heh.ebin")
        .build();

    assertEquals(IMPORT_CLASS, stub.code);
    assertEquals("src/ImportClass.java", stub.filename);
    assertEquals("ImportClass", stub.className);
  }

  @Test
  public void testWithComment() {
    CodeStub stub = new CodeStubBuilder("CommentClass")
        .withBodyComment("TODO: add methods here")
        .build();

    assertEquals(COMMENT_CLASS, stub.code);
    assertEquals("src/CommentClass.java", stub.filename);
    assertEquals("CommentClass", stub.className);
  }
}
