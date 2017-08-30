Feature: Content check for multiple editor tabs

  Scenario: User can examine content in multiple editors
    Given User is logged in with username user and password password
    And User has a task open for class Calculator
    Then the second editor contains public class Calculator
    Then the first editor contains public class CalculatorTest
