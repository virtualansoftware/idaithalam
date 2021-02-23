# Idaithalam Excel support

## Create the Excel in the following format

|TestCaseName|TestCaseNameDesc|URL|ContentType|RequestFile|RequestProcessingType|ResponseFile|ResponseProcessingType|HTTPAction|ExcludeField|HttpStatusCode|
| -----------|:--------------:|-----:| -----:| -----:| -----:| -----:| -----:| -----:| -----:| -----:|
|PetPost|pet post api test|http://mockbin.org/bin/2c5f64fe-4b65-4453-85a5-5308767e79e8|application/xml|input.xml||output.xml|VirtualanStdType=EDI-271|POST|Date|200|
|PetGet|get API testing|https://live.virtualandemo.com/api/pets/findByTags?tags=grey|application/json|||get_response.json||GET||200|


## Example excel is attached.
[Example excel](https://github.com/virtualansoftware/idaithalam/blob/master/samples/idaithalam-excel-apitesting/src/test/resources/virtualan_collection_pet.xlsx)

## Example Project.
[idaithalam-excel-apitesting](https://github.com/virtualansoftware/idaithalam/tree/master/samples/idaithalam-excel-apitesting)

## Example Genrated reports:

