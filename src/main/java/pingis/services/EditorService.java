package pingis.services;

import org.springframework.stereotype.Service;
import pingis.entities.Task;
import pingis.utils.EditorTabData;

import java.util.LinkedHashMap;

@Service
public class EditorService {

    private LinkedHashMap<String, EditorTabData> content;

    public EditorService() {
        content = new LinkedHashMap<>();
    }

    public LinkedHashMap<String, EditorTabData> generateContent(Task t) {
        // Implement DB solution here
        content.put("editor1", new EditorTabData("CalculatorTest.java", t.getCodeStub()));
        content.put("editor2", new EditorTabData("Calculator.java",
                "public class Calculator {\n"
                        + "    \n"
                        + "    public Calculator() {}\n"
                        + "\n"
                        + "    public int multiply(int a, int b) {\n"
                        + "        return a*b;\n"
                        + "    }\n"
                        + "}"));
        content.put("editor3", new EditorTabData("Model solution", "public void testAddition() {\n"
                + "    Calculator c = new Calculator();\n"
                + "    assertEquals(8, c.multiply(2, 4));\n"
                + "}"));

        return content;
    }

    public LinkedHashMap<String, EditorTabData> getContent() {
        return content;
    }
}
