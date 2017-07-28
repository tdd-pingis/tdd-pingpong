Feature: User authenticates through TMC with OAuth2

Scenario Outline: <user> can authenticate with valid username and password
    Given <user> navigates to the login form
    And chooses to authenticate with TMC
    When <user> inputs their credentials for the username <username>
    And submits the TMC login form
    And gives their authorization
    Then <user> is successfully authenticated

    Examples:
        |user   |username   |
        |User   |tdd-pingis |