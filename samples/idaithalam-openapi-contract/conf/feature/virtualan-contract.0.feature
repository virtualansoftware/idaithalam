Feature: Sample.yaml - API Contract validation status
    Scenario: Load initial set of data
      Given Provided all the feature level parameters from file
    @sample
    Scenario: Create a new sample object. - POST api call
      Given a user perform a api action
      When a user post application/json in /sample resource on sample
      Then Verify the status code is 201
    @sample
    Scenario: Sample path - GET api call
      Given a user perform a api action
      And add request with given path params
        | sampleId                   | sampleId                         |
      When a user get application/json in /sample/validate/{sampleId} resource on sample
      Then Verify the status code is 200
