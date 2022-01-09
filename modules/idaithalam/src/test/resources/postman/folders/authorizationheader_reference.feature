Feature: Test processing multiple folder in Postman collection.

  Scenario: Load initial set of data
    Given Provided all the feature level parameters from file

  @persons
  Scenario: Gets a person - api call
    Given a user perform a api action
    And add request with given header params
      | X-API-KEY | abc456 |
    When a user get application/json in /persons/mmustermann resource on persons
    Then the status code is 200
    And Verify-all /persons/mmustermann api includes following in the response
      | firstName                       | Bill                     |
      | lastName                        | Gates                    |
      | lastTimeOnline                  | 2020-08-30T20:28:36.267Z |
      | dateOfBirth                     | 1955-10-28               |
      | spokenLanguages.additionalProp1 | Tamil                    |
      | spokenLanguages.additionalProp3 | Spanish                  |
      | spokenLanguages.additionalProp2 | English                  |
      | username                        | bgates                   |

  @persons
  Scenario: Gets some persons - api call
    Given a user perform a api action
    And add request with given query params
      | pageNumber | 1  |
      | pageSize   | 50 |
    And add request with given header params
      | X-API-KEY | abc456 |
    When a user get application/json in /persons resource on persons
    Then the status code is 200
    And Verify-all /persons api includes following in the response
      | items[0].firstName                       | Max                      |
      | items[0].lastName                        | Mustermann               |
      | items[0].lastTimeOnline                  | 2021-12-10T01:46:26.189Z |
      | items[0].dateOfBirth                     | 2021-11-10               |
      | items[0].spokenLanguages.additionalProp1 | English                  |
      | items[0].spokenLanguages.additionalProp3 | Spain                    |
      | items[0].spokenLanguages.additionalProp2 | French                   |
      | items[0].username                        | mmustermann              |

  @pets
  Scenario: Find pet by ID - api call
    Given a user perform a api action
    And add request with given header params
      | X-API-KEY | abc456 |
    When a user get application/json in /pets/101 resource on pets
    Then the status code is 200
    And Verify-all /pets/101 api includes following in the response
      | photoUrls[0]  | string    |
      | name          | doggie    |
      | id            | i~101     |
      | category.name | string    |
      | category.id   | i~0       |
      | status        | available |
      | tags[0].name  | string    |
      | tags[0].id    | i~0       |
