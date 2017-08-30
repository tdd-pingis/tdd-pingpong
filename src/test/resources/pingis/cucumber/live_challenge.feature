Feature: Live Challenge

  Scenario: User can successfully create a Live challenge and submit his first code to test task
    Given User is logged in with username user and password password
    And has a task open for class Calculator
    When User submits correct code
    Then the User is redirected to the feedback page
    And feedback of success is shown

  Scenario: User can successfully create the first pair of tests for live challenge
    Given User is logged in with username user and password password
    When User creates a new Live challenge Calculator and an addition task pair
    Then the addition test task for Calculator is shown

  Scenario: Admin can successfully join an open live challenge
    Given User is logged in with username user and password password
    And has created a new Live challenge Calculator and an addition task pair
    And has submit test code containing testAddition
    And feedback of success is shown
    And has logged out
    Given Admin is logged in with username admin and password password
    When Admin joins the Live challenge
    Then the addition implementation task for Calculator is shown
    And the second editor contains testAddition

  Scenario: User cannot immediately join his own live challenge
    Given User is logged in with username user and password password
    And has created a new Live challenge Calculator and an addition task pair
    And has submit test code containing testAddition
    And feedback of success is shown
    When User returns to the dashboard
    And User tries to continue the challenge
    Then User is redirected to the dashboard


  Scenario: After Admin has successfully joined live challenge and submitted implementation code for first task pair, he can continue by creating new task pair
    Given User is logged in with username user and password password
    And has created a new Live challenge Calculator and an addition task pair
    And has submit test code containing testAddition
    And feedback of success is shown
    And has logged out
    Given Admin is logged in with username admin and password password
    When Admin joins the Live challenge
    And submits implementation code containing addition
    And feedback of success is shown
    And continues to the next task
    Then Admin is redirected to the new task pair page

  Scenario: User can successfully continue a live challenge started by him/her
    Given User is logged in with username user and password password
    And has created a new Live challenge Calculator and an addition task pair
    And has submit test code containing testAddition
    And feedback of success is shown
    And has logged out
    And Admin is logged in with username admin and password password
    And Admin joins the Live challenge
    And submits implementation code containing addition
    And feedback of success is shown
    And continues to the next task
    And creates a new multiplication task pair for Calculator
    And submits test code containing testMultiplication
    And feedback of success is shown
    And has logged out
    And User is logged in with username user and password password
    And User continues the Live challenge
    Then the multiplication implementation task for Calculator is shown
