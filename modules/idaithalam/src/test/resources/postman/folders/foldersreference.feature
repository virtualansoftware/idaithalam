Feature: Test processing multiple folder in Postman collection.

  Scenario: Load initial set of data
    Given provided all the feature level parameters from file

  @persons
  Scenario: Gets a person - api call
    Given a user perform a api action
    And add request with given header params
      | X-API-KEY   | abc123 |
      | X-API-Test1 | 123    |
      | X-API-Test2 | test2  |
    When a user get application/json in /persons/mmustermann resource on persons
    Then the status code is 200
    And verify-all /persons/mmustermann api includes following in the response
      | firstName                       | Bill                     |
      | lastName                        | Gates                    |
      | lastTimeOnline                  | 2020-08-30T20:28:36.267Z |
      | dateOfBirth                     | 1955-10-28               |
      | spokenLanguages.additionalProp1 | Tamil                    |
      | spokenLanguages.additionalProp3 | Spanish                  |
      | spokenLanguages.additionalProp2 | English                  |
      | username                        | bgates                   |
