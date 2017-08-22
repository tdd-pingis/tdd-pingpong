package pingis.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestStubBuilderTest {

  private static final String SINGLE_METHOD_CLASS =
      "public class TestMe {"
          + "    public int something(int a, int b) {"
          + "        return a + b;"
          + "    }"
          + "}";

  private static final String SINGLE_METHOD_CLASS_EXPECTED =
      "import org.junit.Test;\n"
          + "import fi.helsinki.cs.tmc.edutestutils.Points;\n"
          + "import static org.junit.Assert.*;\n"
          + "\n"
          + "@Points(\"03-03\")\n"
          + "public class TestMeTest {\n"
          + "\n"
          + "    @Test\n"
          + "    public void testSomething() {\n"
          + "    }\n"
          + "}\n";

  // TODO: More test cases

  @Test
  public void testMethod() {
    CodeStub stub = new TestStubBuilder(SINGLE_METHOD_CLASS)
        .withTestImports()
        .build();

    assertEquals(SINGLE_METHOD_CLASS_EXPECTED, stub.code);
    assertEquals("TestMeTest", stub.className);
    assertEquals("test/TestMeTest.java", stub.filename);
  }

}