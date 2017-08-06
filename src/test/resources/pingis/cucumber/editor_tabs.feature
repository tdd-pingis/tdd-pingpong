
Feature: Content check for multiple editor tabs

  Scenario Outline: <user> can examine content in multiple editors
    Given <user> navigates to the task page at <page>
    When <user> clicks the <tab_name>
    Then the page contains <content>

    Examples:

      |user   |tab_name                |content                               |page          |
      |User   |Write your test here    |public void testMultiplication()      |task/1        |
      |User   |Implement code here     |public class Calculator               |task/2        |
      |Admin  |Test to fulfill         |assertEquals                          |task/2        |

