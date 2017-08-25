Feature: Participate in Live Challenge

  Scenario Outline: <user> can successfully create live-challenge and submit his first code to test task
    Given <user> is logged in
    And <user> has successfully submitted new challenge with first task pair
    When <user> inputs and submits <code-type> code
    Then the <page> is shown

    Examples:

      |user         |page                       |code-type     |
      |User         |feedback page              |test          |
      |Admin        |implement next task page   |empty         |


  Scenario: User can successfully create the first pair of tests for live challenge
    Given User is logged in
    And User creates a new Live challenge
    And User inputs and submits the challenge description
    When User inputs and submits data for first task pair
    Then the implement next task page is shown


  Scenario: Admin can successfully join an open live challenge
    Given User is logged in
    And User has successfully submitted new challenge with first task pair
    And User has successfully submitted test code for first task pair
    When Admin wants to join in Live challenge
    And Admin clicks the join live challenge button
    Then the implement next task page is shown
    And page contains the test code input by User


  Scenario: User cannot immediately join his own live challenge
    Given User is logged in
    And User has successfully submitted new challenge with first task pair
    And User has successfully submitted test code for first task pair
    When User wants to join in Live challenge
    Then the join live challenge button is not shown


  Scenario:  After Admin has successfully joined live challenge and submitted implementation code for first task pair, he can continue by creating new task pair
    Given User is logged in
    And User has successfully submitted new challenge with first task pair
    And User has successfully submitted test code for first task pair
    And User clicks the next task button
    And Admin wants to join in Live challenge
    And Admin clicks the join live challenge button
    And Admin has successfully submitted implementation code for first task pair
    When Admin clicks the next task button
    Then the new task pair is shown

  Scenario: User can successfully continue a live challenge started by him/her
    Given User is logged in
    And User has successfully submitted new challenge with first task pair
    And User has successfully submitted test code for first task pair
    And Admin wants to join in Live challenge
    And Admin clicks the join live challenge button
    And Admin has successfully submitted implementation code for first task pair
    When Admin clicks the next task button
    And Admin inputs and submits data for second task pair
    And Admin has successfully submitted test code for second task pair
    And User wants to continue Live challenge
    And User clicks the continue live challenge button
    Then the implement next task page is shown
    And page contains the implementation code input by User
