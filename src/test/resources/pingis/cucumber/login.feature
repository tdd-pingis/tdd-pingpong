Feature: Logging in and out

Scenario Outline: <user> can log in with valid username and password
    When <user> logs in with username <username> and password <password>
    Then <user> is successfully authenticated
    And is redirected to the user page

    Examples:
        |user   |username   |password   |
        |User   |user       |password   |
        |Admin  |admin      |password   |

Scenario Outline: <user> cannot login with an invalid username or password
    When <user> logs in with incorrect username <username> and password <password>
    Then <user> is not authenticated
    And is redirected to the login page

    Examples:
        |user   |username   |password   |
        |User   |user       |passord    |
        |Admin  |amin       |password   |

Scenario: User can log out
    Given User is logged in with username user and password password
    When User logs out
    Then User is no longer authenticated
