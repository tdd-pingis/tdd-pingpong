Feature: User authenticates through TMC with OAuth2

Scenario: User can authenticate with valid username and password
    Given user navigates to the login form
    And chooses to authenticate with TMC
    When user inputs their credentials for TMC
    And submits the TMC login form
    And gives their authorization
    Then user is successfully authenticated
