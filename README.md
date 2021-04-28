<h1 align="center">Idaithalam</h1>

**Idaithalam:** [![Maven Central](https://img.shields.io/maven-central/v/io.virtualan/idaithalam.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.virtualan%22%20AND%20a:%22idaithalam%22)  [![Build Status](https://ci.appveyor.com/api/projects/status/32r7s2skrgm9ubva?svg=true)] (https://ci.appveyor.com/api/projects/status/up09e6hyd8dn2c3c?svg=true)              
# What is Idaithalam: Scriptless Automation Framework.

Idaithalam is a Cucumber feature file generation product.

As a user: You need to export the POSTMAN Collection and pass to Idaithalam. **NO CODING at all** 

Currently It supports generate Feature files for **POSTMAN Collection** and Virtualan Collection and Excel format(Coming Soon). 
This will covert REST APIs based on POST, GET, PUT, DELETE and PATCH action as respective domain specific language which helps you to describe API/business behavior without the need to go into detail of implementation. 


## Steps to create POSTMAN Collection
* Most of the common use case, You can execute as Regression test suite for the test case executed manually via POSTMAN.
* Run the testcase via POSTMAN and store the response as Example in the POSTMAN.
* Run all the apis via POSTMAN for all the test cases and capture and save the response. 
* Export all the API request and Response as POSTMAN collection.
* POSTMAN Collection can be passed as Input as Idaithalam.

### Demo POSTMAN Collection: 
[https://github.com/virtualansoftware/idaithalam-contract-testing-demo/blob/master/conf/virtualan-contract.feature](https://github.com/virtualansoftware/idaithalam-contract-testing-demo/blob/master/conf/virtualan-contract.feature)

### Auto Generated Feature file: 
[https://github.com/virtualansoftware/idaithalam-contract-testing-demo/blob/master/src/test/resources/idaithalan.postman_collection.json](https://github.com/virtualansoftware/idaithalam-contract-testing-demo/blob/master/src/test/resources/idaithalan.postman_collection.json) 

### Demo Code Base:
[https://github.com/virtualansoftware/idaithalam-contract-testing-demo](https://github.com/virtualansoftware/idaithalam-contract-testing-demo)

### How to Integrate: 

1. cucumblan.properties  - Should be added in classpath

```
service.api=https://live.virtualandemo.com              # Service Endpoint URL
virtualan.data.load=idaithalan.postman_collection.json  # Collection file should be in CLASSPATH. added POSTMAN Collection  
virtualan.data.type=POSTMAN                             # Collection Type.  POSTMAN, VIRTUALAN, EXCEL

```
2.  "conf" directory: 

``` 
1. Should be created in the project root folder. 
2. Feature file will be Auto generated here. 
3. You can keep cucumblan.properties and Collection files in this location.

Example: 
https://github.com/virtualansoftware/idaithalam-contract-testing-demo/tree/master/conf 
```
3. Code to Invoke the Auto Generation:
```
//Initiate the contract testing
//Generate feature file from POSTMAN Collection
//Execute and Generate the HTML Cucumber report
IdaithalamExecutor.validateContract("Pet API Production Checkout");

```

## Used for API testing.
* Most of the common use case, You can execute as Regression test suite for the test case executed manually via POSTMAN.
* Run the testcase via POSTMAN and store the response as Example with response data in the POSTMAN for all the test cases that you had executed. 
* Export all the API request and Response as POSTMAN collection.
* POSTMAN Collection can be passed as Input as Idaithalam.
* Idaithalam can be integrated along Continuous Integration and Continuous Delivery(CI/CD) with Pipeline.
* Idaithalam take the POSTMAN Collection as Input and Covert as Cucumber Feature file. 
* Cucumblan-API another product will execute the feature file and Produce the Cucumber Report with BDD style.
## Used for Contract testing.
* Support for Contract testing. 
## Used for Production Checkout.
* Utilized to run against selected use case that needs to validated against Production Checkout(Validation during the production release).   
## Used for Agile sprint-end Regression testing.
* During the sprint, You can run all the test cases Manually and then save the response via POSTMAN. Using Idaithalam, Export the POSTMAN Collection and Pass Collection to Idaithalam and will execute and produce the Excellent cucumber HTML Report with all the charts.   
## [License](#table-of-contents)
-------

Copyright 2019 Virtualan Contributors (https://virtualan.io)  

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at [apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

---

