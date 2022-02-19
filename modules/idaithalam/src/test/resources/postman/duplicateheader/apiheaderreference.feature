Feature: Test processing multiple headers.

  Scenario: Load initial set of data
    Given provided all the feature level parameters from file

  @persons
  Scenario: Gets some persons - api call
    Given a user perform a api action
    And add request with given query params
      | pageNumber | 1  |
      | pageSize   | 50 |
    And add request with given header params
      | X-API-KEY   | abc123,abc789 |
      | X-API-Test1 | 123           |
      | X-API-Test2 | test2         |
    When a user get application/json in /persons resource on persons
    Then the status code is 200
    And verify-all /persons api includes following in the response
      | items[0].firstName                       | Max                      |
      | items[0].lastName                        | Mustermann               |
      | items[0].lastTimeOnline                  | 2021-12-10T01:46:26.189Z |
      | items[0].dateOfBirth                     | 2021-11-10               |
      | items[0].spokenLanguages.additionalProp1 | English                  |
      | items[0].spokenLanguages.additionalProp3 | Spain                    |
      | items[0].spokenLanguages.additionalProp2 | French                   |
      | items[0].username                        | mmustermann              |
