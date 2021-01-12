Feature: Pet API Production Checkout - API Contract validation status

  Scenario: Load initial set of data
    Given Provided all the feature level parameters from file

  Scenario: read pet2 api - GET api call
    Given a user perform a api action
    When a user get application/json in /api/pets/200 resource on api
    Then Verify the status code is 200
    And Verify-all /api/pets/200 api includes following in the response
      | photoUrls[0]  | string    |
      | name          | Butch     |
      | id            | i~201     |
      | category.name | Bulldog   |
      | category.id   | i~200     |
      | status        | available |
      | tags[0].name  | grey      |
      | tags[0].id    | i~201     |

  Scenario: read pet1 api - GET api call
    Given a user perform a api action
    When a user get application/json in /api/pets/100 resource on api
    Then Verify the status code is 200
    And Verify-all /api/pets/100 api includes following in the response
      | photoUrls[0]  | string          |
      | name          | Rocky           |
      | id            | i~101           |
      | category.name | german shepherd |
      | category.id   | i~100           |
      | status        | available       |
      | tags[0].name  | brown           |
      | tags[0].id    | i~101           |

  Scenario: update pet1 api - PUT api call
    Given a user perform a api action
    And add request with given header params
      | Content-Type | application/json |
    And Update api with given input
      | photoUrls[0]  | string          |
      | name          | Rocky           |
      | id            | i~101           |
      | category.name | german shepherd |
      | category.id   | i~100           |
      | status        | available       |
      | tags[0].name  | brown           |
      | tags[0].id    | i~101           |
    When a user put application/json in /api/pets/100 resource on api
    Then Verify the status code is 200
    And Verify-all /api/pets/100 api includes following in the response
      | photoUrls[0]  | string          |
      | name          | Rocky           |
      | id            | i~101           |
      | category.name | german shepherd |
      | category.id   | i~100           |
      | status        | available       |
      | tags[0].name  | brown           |
      | tags[0].id    | i~101           |

  Scenario: update pet2 api - PUT api call
    Given a user perform a api action
    And add request with given header params
      | Content-Type | application/json |
    And Update api with given input
      | photoUrls[0]  | string     |
      | name          | Butch      |
      | id            | i~201      |
      | category.name | Bulldog    |
      | category.id   | i~200      |
      | status        | available  |
      | tags[0].name  | white gray |
      | tags[0].id    | i~201      |
    When a user put application/json in /api/pets/200 resource on api
    Then Verify the status code is 200
    And Verify-all /api/pets/200 api includes following in the response
      | photoUrls[0]  | string     |
      | name          | Butch      |
      | id            | i~201      |
      | category.name | Bulldog    |
      | category.id   | i~200      |
      | status        | available  |
      | tags[0].name  | white gray |
      | tags[0].id    | i~201      |

  Scenario: create pet1 api - POST api call
    Given a user perform a api action
    And add request with given header params
      | Content-Type | application/json |
    And Create api with given input
      | photoUrls[0]  | string          |
      | name          | Rocky           |
      | id            | i~101           |
      | category.name | German Shepherd |
      | category.id   | i~100           |
      | status        | available       |
      | tags[0].name  | brown           |
      | tags[0].id    | i~101           |
    When a user post application/json in /api/pets resource on api
    Then Verify the status code is 201
    And Verify-all /api/pets api includes following in the response
      | photoUrls[0]  | string          |
      | name          | Rocky           |
      | id            | i~101           |
      | category.name | German Shepherd |
      | category.id   | i~100           |
      | status        | available       |
      | tags[0].name  | brown           |
      | tags[0].id    | i~101           |

  Scenario: create pet2 api - POST api call
    Given a user perform a api action
    And add request with given header params
      | Content-Type | application/json |
    And Create api with given input
      | photoUrls[0]  | string    |
      | name          | Butch     |
      | id            | i~201     |
      | category.name | Bulldog   |
      | category.id   | i~200     |
      | status        | available |
      | tags[0].name  | grey      |
      | tags[0].id    | i~201     |
    When a user post application/json in /api/pets resource on api
    Then Verify the status code is 201
    And Verify-all /api/pets api includes following in the response
      | photoUrls[0]  | string    |
      | name          | Butch     |
      | id            | i~201     |
      | category.name | Bulldog   |
      | category.id   | i~200     |
      | status        | available |
      | tags[0].name  | grey      |
      | tags[0].id    | i~201     |

  Scenario: delete pet1 - DELETE api call
    Given a user perform a api action
    When a user delete application/json in /api/pets/100 resource on api
    Then Verify the status code is 200

  Scenario: delete pet2 - DELETE api call
    Given a user perform a api action
    When a user delete application/json in /api/pets/200 resource on api
    Then Verify the status code is 200

  Scenario: get pet2 by tags - GET api call
    Given a user perform a api action
    And add request with given query params
      | tags | grey |
    When a user get application/json in /api/pets/findByTags resource on api
    Then Verify the status code is 200
    And Verify-all /api/pets/findByTags api includes following in the response
      | photoUrls[0]  | string    |
      | name          | Butch     |
      | id            | i~201     |
      | category.name | Bulldog   |
      | category.id   | i~200     |
      | status        | available |
      | tags[0].name  | grey      |
      | tags[0].id    | i~201     |

  Scenario: get pet1 by tags - GET api call
    Given a user perform a api action
    And add request with given query params
      | tags | brown |
    When a user get application/json in /api/pets/findByTags resource on api
    Then Verify the status code is 200
    And Verify-all /api/pets/findByTags api includes following in the response
      | photoUrls[0]  | string          |
      | name          | Rocky           |
      | id            | i~101           |
      | category.name | german shepherd |
      | category.id   | i~100           |
      | status        | available       |
      | tags[0].name  | brown           |
      | tags[0].id    | i~101           |

  Scenario: get pet by status - GET api call
    Given a user perform a api action
    And add request with given query params
      | status | available |
    When a user get application/json in /api/pets/findByStatus resource on api
    Then Verify the status code is 200
    And Verify-all /api/pets/findByStatus api includes following in the response
      | photoUrls[0]  | string          |
      | name          | Rocky           |
      | id            | i~101           |
      | category.name | german shepherd |
      | category.id   | i~100           |
      | status        | available       |
      | tags[0].name  | brown           |
      | tags[0].id    | i~101           |

  Scenario: risk calculations - POST api call
    Given a user perform a api action
    And add request with given header params
      | Content-Type | application/json |
    And Create api with given input
      | birthday   | 1978-10-24 |
      | postalCode | 60563      |
    When a user post application/json in /api/riskfactor/compute resource on api
    Then Verify the status code is 200
    And Verify api response with 40 includes in the response
