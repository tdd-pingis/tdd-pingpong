Feature: Content check for multiple editor tabs

  Scenario Outline: <user> can examine content in multiple editors
    Given <user> is logged in
    Given <user> has chosen to embark on a new challenge
    When <user> clicks the <tab_name> tab
    Then the page contains <content>

    Examples:

      |user   |tab_name                 |content                               |  
      |User   |test/CalculatorTest.java |public void testMultiplication        |
      |User   |test/CalculatorTest.java |CalculatorTest                        |
