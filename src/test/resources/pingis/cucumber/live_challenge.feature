Feature: Participate in Live Challenge
  
  @dev
  Scenario Outline: <user> can successfully create the first pair of tests for live challenge
    Given <user> is logged in
    When <user> wants to participate in Live challenge
    And <user> inputs and submits the challenge description
    When <user> inputs and submits data for new Task pair
    Then the <page> is shown 
  
    Examples:
  
      |user   |page                               |
      |User   |implement next task page           |
      |Admin  |implement next task page           |   

  @dev
  Scenario Outline: <user> can successfully create live-challenge and submit his first code to test task
    Given <user> is logged in
    And <user> has successfully submitted new challenge with first task pair
    When <user> inputs and submits <code-type> code
    Then the <page> is shown 
  
    Examples:
  
      |user         |page                       |code-type     |
      |User         |feedback page              |test          |
      |Admin        |implement next task page   |              |

  



