Feature: Content check for multiple editor tabs

  Scenario Outline: <user> can examine content in multiple editors
    Given <user> is logged in
    Given <user> navigates to the task page at <page>
    When <user> clicks the <tab_name> tab
    Then the page contains <content>

    Examples:

      |user   |tab_name                 |content                               |page          |
      |User   |test/CalculatorTest.java |public void testMultiplication        |task/1        |
      |User   |test/CalculatorTest.java |CalculatorTest                        |task/2        |
      |Admin  |src/Calculator.java      |add                                   |task/3        |


