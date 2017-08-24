@dev
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


  Scenario Outline: <user> can successfully create the first pair of tests for live challenge
    Given <user> is logged in
    And <user> wants to participate in Live challenge
    And <user> inputs and submits the challenge description
    When <user> inputs and submits data for first task pair
    Then the <page> is shown 
  
    Examples:
  
      |user   |page                               |
      |User   |implement next task page           |
      |Admin  |implement next task page           |


  Scenario Outline: <user2> can successfully join an open live challenge
    Given <user1> is logged in
    And <user1> has successfully submitted new challenge with first task pair
    And <user1> has successfully submitted test code for first task pair
    When <user2> wants to join in Live challenge
    And <user2> clicks the join live challenge button
    Then the <page> is shown 
    And page <contains> the test code input by <user1>
  
    Examples:

      |user1      |user2   |contains          |page                             |
      |User       |Admin   |contains          |implement next task page         |     
      |User       |User    |does not contain  |user page                        |  


  Scenario Outline: After <user2> has successfully joined live challenge and submitted implementation code for first task pair, he can continue by creating new task pair
    Given <user1> is logged in
    And <user1> has successfully submitted new challenge with first task pair
    And <user1> has successfully submitted test code for first task pair
    And <user1> clicks the next task button
    And <user2> wants to join in Live challenge
    And <user2> clicks the join live challenge button
    And <user2> has successfully submitted implementation code for first task pair
    When <user2> clicks the next task button
    Then the <page> is shown
  
    Examples:

      |user1      |user2         |page              |
      |User       |Admin         |new task pair     |


  Scenario Outline: <user1> can successfully continue a live challenge started by him/her
    Given <user1> is logged in
    And <user1> has successfully submitted new challenge with first task pair
    And <user1> has successfully submitted test code for first task pair
    And <user2> wants to join in Live challenge
    And <user2> clicks the join live challenge button
    And <user2> has successfully submitted implementation code for first task pair
    When <user2> clicks the next task button
    And <user2> inputs and submits data for second task pair
    And <user2> has successfully submitted test code for second task pair
    And <user1> wants to continue Live challenge
    And <user1> clicks the continue live challenge button
    Then the <page> is shown
    And page contains the implementation code input by <user1>
  
    Examples:

      |user1      |user2         |page                       |
      |User       |Admin         |implement next task page   |

  

  

