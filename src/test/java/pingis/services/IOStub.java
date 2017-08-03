/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.services;


public class IOStub implements IO{
    //CHECKSTYLE:OFF
    private String[] data = {
        "{",
        "   \"challenges\": [",
        "      {",
        "         \"name\": \"calculator\",",
        "         \"author\": \"modeluser\",",
        "         \"desc\": \"Test and implement a simple command line based calculator.\",",
        "         \"tasks\": [",
        "            {",
        "               \"author\": \"modeluser\",",
        "               \"name\": \"Test multiplication\",",
        "               \"type\": \"test\",",
        "               \"desc\": \"Write a JUnit test case that tests the multiplication method for two integers. \\nMethod should take two integers as arguments and return the product of those as an integer.\",",
        "               \"codeStub\": [\"@Test\\npublic void testMultiplication() {\\n\\t//TODO: implement this\\n\\n}\"],",
        "               \"modelimplementation\": [\"public class CalculatorTest {\\n\",",
        "                                         \"@Test\\n    public void testAddition() {\\n\",",
        "                                             \"Calculator calc = new Calculator();\\n\",",
        "                                             \"assertEquals(23, calc.multiply(18, 5));\\n\",",
        "                                             \"}\\n}\"]",
        "             }",
        "          ]",
        "       }",
        "    ]",
        "}"};
    //CHECKSTYLE:ON
    private int currentLine = 0;

    public IOStub() {
        System.out.println(data);
    }
    
    public boolean hasNext() {
        return currentLine < data.length;
    } 
    
    public String nextLine() {
        return data[currentLine++];
    }
}
