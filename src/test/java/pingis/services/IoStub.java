package pingis.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoStub implements Io {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private String[] data = {
      "{",
      "   \"challenges\": [",
      "      {",
      "         \"name\": \"calculator\",",
      "         \"author\": \"modeluser\",",
      "         \"level\": 1,",
      "         \"desc\": \"Test and implement a simple command line based calculator.\",",
      "         \"type\": \"PROJECT\",",
      "         \"realm\": \"BEGINNER\",",
      "         \"tasks\": [",
      "            {",
      "               \"author\": \"modeluser\",",
      "               \"index\": 1,",
      "               \"name\": \"Test multiplication\",",
      "               \"type\": \"test\",",
      "               \"desc\": \"Write a JUnit test case that tests the multiplication method for "
          + "two integers. \\nMethod should take two integers as arguments "
          + "and return the product of those as an integer.\",",
      "               \"codeStub\": [\"@Test\\npublic void testMultiplication() {"
          + "\\n\\t//TODO: implement this\\n\\n}\"],",
      "               \"modelimplementation\": [\"public class CalculatorTest {\\n\",",
      "                                         \"@Test\\n    public void testAddition() {\\n\",",
      "                                            \"Calculator calc = new Calculator();\\n\",",
      "                                            \"assertEquals(23, calc.multiply(18, 5));\\n\",",
      "                                            \"}\\n}\"]",
      "             }",
      "          ]",
      "       }",
      "    ],",
      "   \"dummyimplementations\": []",

      "}"};

  private int currentLine = 0;

  public IoStub() {
    for (String row : data) {
      logger.debug(row);
    }
  }

  public boolean hasNext() {
    return currentLine < data.length;
  }

  public String nextLine() {
    return data[currentLine++];
  }
}
