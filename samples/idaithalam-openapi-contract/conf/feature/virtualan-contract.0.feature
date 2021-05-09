Feature: Sample.yaml - API Contract validation status

  Scenario: Load initial set of data
    Given Provided all the feature level parameters from file

  Scenario: Create a new sample object. - POST api call
    Given a user perform a api action
    And Create api with given input
      | filename | Sample.yaml |
      | name     | Sample      |
      | version  | 0.0.1       |
    When a user post application/json in /sample resource on sample
    Then Verify the status code is 201

  Scenario: Sample path - GET api call
    Given a user perform a api action
    And add request with given path params
      | sampleId | sampleId |
    When a user get application/json in /sample/validate/{sampleId} resource on sample
    Then Verify the status code is 200
    And Verify-all /sample/validate/{sampleId} api includes following in the response
      | sample1 | one   |
      | id      | i~1   |
      | sample3 | three |
      | sample2 | two   |
