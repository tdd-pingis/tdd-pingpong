Feature: Logging in and out

@dev
Scenario Outline: <user> can log in with valid username and password
    When <user> clicks the Login button
    And inputs their username <username> and password <password>
    And clicks the Log in button
    Then <user> is successfully authenticated
    And is redirected to the front page

    Examples:
        |user   |username   |password   |
        |User   |user       |password   |
        |Admin  |admin      |password   |

@dev
Scenario Outline: <user> cannot login with an invalid username or password
    When <user> clicks the Login button
    And inputs their username <username> and password <password>
    And clicks the Log in button
    Then <user> is not authenticated
    And is redirected to the login error page

    Examples:
        |user   |username   |password   |
        |User   |user       |passord    |
        |Admin  |admin      |passord    |
        |User   |uer        |password   |
        |Admin  |amin       |password   |

@dev
Scenario: User can log out
    Given user is logged in
    And clicks the My Account button
    And clicks the Logout button
    Then user is successfully signed out


@prod
Scenario: User can authenticate through TMC
    When user clicks the Login button
    And inputs their TMC username and password
    And clicks Sign in
    Then user is successfully authenticated
    And is redirected to the front page
