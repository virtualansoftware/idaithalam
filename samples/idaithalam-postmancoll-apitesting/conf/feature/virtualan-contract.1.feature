Feature: virtualan.json - API Contract validation status
    Scenario: Load initial set of data
      Given Provided all the feature level parameters from file
    @xml
    Scenario: temp - POST api call
      Given a user perform a api action
      And add request with given header params
        | Content-Type                   | text/xml                         |
       And add request data inline with text/xml given input
        | <?xml version="1.0" encoding="utf-8"?> |
        | <soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"> |
        | <soap:Body> |
        | <CelsiusToFahrenheit xmlns="https://www.w3schools.com/xml/"> |
        | <Celsius>100</Celsius> |
        | </CelsiusToFahrenheit> |
        | </soap:Body> |
        | </soap:Envelope> |
      When a user post text/xml in /xml/tempconvert.asmx resource on xml
      Then Verify the status code is 200
      And Verify api response inline includes in the response
        | <?xml version="1.0" encoding="utf-8"?> |
        | <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema"> |
        | <soap:Body> |
        | <CelsiusToFahrenheitResponse xmlns="https://www.w3schools.com/xml/"> |
        | <CelsiusToFahrenheitResult>212</CelsiusToFahrenheitResult> |
        | </CelsiusToFahrenheitResponse> |
        | </soap:Body> |
        | </soap:Envelope> |

