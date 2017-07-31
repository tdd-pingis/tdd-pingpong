Feature: Simple authentication for two sample users

Scenario Outline: <user> can log in with valid username and password
    Given <user> navigates to the login page
    When <user> inputs their username <username> and password <password>
    And submits the form
    Then <user> is successfully authenticated

    Examples:
        |user   |username   |password   |
        |User   |user       |password   |
        |Admin  |admin      |password   |

Scenario Outline: <user> cannot login with an invalid username or password
    Given <user> navigates to the login page
    When <user> inputs their username <username> and password <password>
    And submits the form
    Then <user> is not authenticated

    Examples:
        |user   |username   |password   |
        |User   |user       |passord    |
        |Admin  |admin      |passord    |
        |User   |uer        |password   |
        |Admin  |amin       |password   |