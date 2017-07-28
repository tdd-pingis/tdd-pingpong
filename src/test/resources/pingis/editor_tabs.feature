
Feature: Content check for multiple editor tabs

  Scenario Outline: <user> can examine content in multiple editors
    Given <user> navigates to the task page at <page>
    When <user> clicks the <tab_name>
    Then the page contains <content>

    Examples:
      |user   |tab_name     |content                               |page                  |
      |User   |Editor 1     |public void testAddition()            |task/1/1/rambo        |
      |User   |Editor 2     |System.out.println("Hello, tabs!");   |task/1/3/chucknorris  |
      |Admin  |Editor 3     |public void testing tabs              |task/1/1/hardcore     |
      |Admin  |Editor 1     |testIntegerDivision()                 |task/1/3/supereasy    |
