package io.virtualan.idaithalam.core.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.virtualan.idaithalam.config.IdaithalamConfiguration;
import io.virtualan.idaithalam.core.UnableToProcessException;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.domain.SheetObject;
import io.virtualan.idaithalam.exception.IdaithalamException;
import io.virtualan.idaithalam.exception.MandatoryFieldMissingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class ExcelToCollectionGeneratorTests {

    private static final String OUTPUT_FOLDER_NAME = "target/output/";

    static{
        try {
            FileUtils.deleteDirectory(new File(OUTPUT_FOLDER_NAME));
        } catch (IOException e) {
            log.warn("Not able to cleanup output folder "+e.getMessage());
        }
    }

    @Before
    public void before(){
        log.info("*****************************************************************************");
    }

    @After
    public void after(){
        log.info("*****************************************************************************");
    }

    @Test
    public void convertStreamToStringTest() throws IOException {
        String input = "idaithalam";
        InputStream is = new ByteArrayInputStream(input.getBytes());
        String actual = ExcelToCollectionGenerator.convertStreamToString(is);
        Assert.assertEquals(input + "\n", actual);
    }

    @Test
    public void convertStreamToStringNullTest() throws IOException {
        InputStream is = null;
        String actual = ExcelToCollectionGenerator.convertStreamToString(is);
        Assert.assertNull(actual);
    }


    @Test
    public void createCollectionTestA_HappyPath() throws IOException, UnableToProcessException, URISyntaxException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
        String basePath = "src/test/resources/Excels";
        File inputFile = new File("src/test/resources/Excels/customer-self-service-with-db.xlsx");
        createCollection(inputFile, basePath);
    }

    private InputStream getFileAsStream(String fileName) throws IOException {
        return ExcelToCollectionGeneratorTests.class.getClassLoader().getResourceAsStream(fileName);
    }


    private String getFileAsString(String fileName) throws IOException {
        return getFileAsString(ExcelToCollectionGeneratorTests.class.getClassLoader().getResourceAsStream(fileName));
    }

    private String getFileAsString(InputStream inputStream)
            throws IOException {
        String content = new BufferedReader(new InputStreamReader(inputStream))
                .lines().parallel().collect(Collectors.joining("\n"));
        return content;
    }


    @Test
    public void createCollectionTestB_With_Kafka() throws IOException, UnableToProcessException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
        String basePath = "src/test/resources/Excels";
        File inputFile = new File("src/test/resources/Excels/virtualan_collection_kafka_db_testcase_0.xlsx");
        createCollection(inputFile, basePath);
    }


    @Test
    public void createCollectionTestC_Multirun() throws IOException, UnableToProcessException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
        String basePath = "src/test/resources/Excels";
        File inputFile = new File("src/test/resources/virtualan_collection_testcase_multirun.xlsx");
        createCollection(inputFile, basePath);
    }


    @Test
    public void createCollectionTestD_Single_Workflow() throws IOException, UnableToProcessException {
        IdaithalamConfiguration.setProperty("workflow", "Enabled");
        String basePath = "src/test/resources/Excels";
        File inputFile = new File("src/test/resources/virtualan_collection_testcase_4.xlsx");
        createCollection(inputFile, basePath);
    }


    
    
    @Test
    public void testvirtualan_bdd_testcase_run_manager() throws UnableToProcessException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_bdd_testcase_run_manager.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_pet_empty() throws UnableToProcessException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_pet_empty.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_pet_sheet_2() throws UnableToProcessException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_pet_sheet_2.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_0() throws UnableToProcessException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_0.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_01() throws UnableToProcessException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_01.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_02() throws UnableToProcessException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_02.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_2() throws UnableToProcessException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_2.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_3() throws UnableToProcessException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_3.xlsx");
         createCollection(inputFile, basePath);
    }
    

    @Test
    public void testvirtualan_collection_testcase_5() throws UnableToProcessException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_5.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_6() throws UnableToProcessException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_6.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_7() throws UnableToProcessException {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_7.xlsx");
         createCollection(inputFile, basePath);
    }
    
    
    @Test
    public void testvirtualan_collection_testcase_8() throws IOException, UnableToProcessException {
        IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_8.xlsx");
         createCollection(inputFile, basePath);
    }

    @Test
    public void testFileNotFoundSecenario() throws NoSuchFieldException, IllegalAccessException {
        String fileMissingMsg = "File is missing(src/test/resources/Excels) : virtualan_collection_testcase_8-filenotthere.xlsx";
        IdaithalamConfiguration.setProperty("workflow", "Disabled");
        String basePath = "src/test/resources/Excels";
        File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_8-filenotthere.xlsx");
        try {
            createCollection(inputFile, basePath);
        }catch (UnableToProcessException unableToProcessException){
            Assert.assertEquals(fileMissingMsg.trim(), unableToProcessException.getMessage().trim());
        }
    }


    @Test
    public void testInvalidOutputDirectory() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException, IdaithalamException {
        String assertMessage = "Unable to generate Virtualan  JSON  API-Testing-0 : E:\\API-Testing-0.json (The system cannot find the path specified)";
        IdaithalamConfiguration.setProperty("workflow", "Disabled");
        String basePath = "src/test/resources/Excels";
        File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_0.xlsx");
        String generatedPath = "E:/";
        if (!new File(generatedPath).exists()) {
            new File(generatedPath).mkdirs();
        }
        ApiExecutorParam apiExecutorParam = new ApiExecutorParam();
        apiExecutorParam.setBasePath(basePath);
        apiExecutorParam.setInputExcel(inputFile.getName());
        apiExecutorParam.setOutputDir(generatedPath);

        try {
            boolean actual = ExcelToCollectionGenerator.createCollection(apiExecutorParam);
        }catch (UnableToProcessException unableToProcessException){
            Assert.assertEquals(assertMessage.trim(), unableToProcessException.getMessage().trim());
        }
    }

    @Test
    public void testBuildCollectionsCreateColectionMethod() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        IdaithalamConfiguration.setProperty("workflow", "Disabled");
        String basePath = "src/test/resources/Excels";
        File inputFile = new File("src/test/resources/Excels/TestBuildCollectionsExcel.xlsx");
        String generatedPath = OUTPUT_FOLDER_NAME+inputFile.getName();
        if (!new File(generatedPath).exists()) {
            new File(generatedPath).mkdirs();
        }
        ApiExecutorParam apiExecutorParam = new ApiExecutorParam();
        apiExecutorParam.setBasePath(basePath);
        apiExecutorParam.setInputExcel(inputFile.getName());
        apiExecutorParam.setOutputDir(generatedPath);



        ExcelToCollectionGenerator excelToCollectionGenerator = new ExcelToCollectionGenerator();
        Constructor constructor = ExcelToCollectionGenerator.class.getDeclaredClasses()[0].getDeclaredConstructor();
        constructor.setAccessible(true);
        Object object = constructor.newInstance();
        Method method = ExcelToCollectionGenerator.class.getDeclaredClasses()[0].getDeclaredMethod("createCollection" , ApiExecutorParam.class);
        method.setAccessible(true);
        Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) method.invoke(object, apiExecutorParam);
        System.out.println("Map "+map);

        List<String> sheetNames = getExcelSheetNames(inputFile);
        Map<String, String> cucumbalamMap = map.get("cucumblan");
        String virtualanDataHeading = cucumbalamMap.get("virtualan.data.heading");
        sheetNames.forEach(sheetName->{
            Assert.assertTrue(virtualanDataHeading.contains(sheetName));
        });
    }
    @Test(expected = MandatoryFieldMissingException.class)
   public void testTestCaseFieldMissing() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException, IdaithalamException {
        String missingMandatoryHeaderErrorMessage = "Sheet Name : API-Testing - Row : 2 - Type : REST - [TestCaseName] mandatory fields are missing";
        assertMandatoryFieldMissingMessage(missingMandatoryHeaderErrorMessage, "src/test/resources/Excels/REST-TestCaseNameMissing.xlsx");
   }
    @Test(expected = MandatoryFieldMissingException.class)
    public void testTestCaseNameDescFieldMissing() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException, IdaithalamException {
        String missingMandatoryHeaderErrorMessage = "Sheet Name : API-Testing - Row : 3 - Type : REST - [TestCaseNameDesc] mandatory fields are missing";
        assertMandatoryFieldMissingMessage(missingMandatoryHeaderErrorMessage, "src/test/resources/Excels/REST-TestCaseNameDescMissing.xlsx");
    }
    @Test(expected = MandatoryFieldMissingException.class)
    public void testActionFieldMissing() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException, IdaithalamException {
        String missingMandatoryHeaderErrorMessage = "Sheet Name : API-Testing - Row : 3 - Type : REST - [Action] mandatory fields are missing";
        assertMandatoryFieldMissingMessage(missingMandatoryHeaderErrorMessage, "src/test/resources/Excels/REST-ActionMissing.xlsx");
    }

    @Test(expected = MandatoryFieldMissingException.class)
    public void testStatusCodeFieldMissing() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException, IdaithalamException {
        String missingMandatoryHeaderErrorMessage = "Sheet Name : API-Testing - Row : 3 - Type : REST - [StatusCode] mandatory fields are missing";
        assertMandatoryFieldMissingMessage(missingMandatoryHeaderErrorMessage, "src/test/resources/Excels/REST-StatusCodeMissing.xlsx");
    }

    @Test(expected = MandatoryFieldMissingException.class)
    public void testURLFieldMissing() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException, IdaithalamException {
        String missingMandatoryHeaderErrorMessage = "Sheet Name : API-Testing - Row : 3 - Type : REST - [URL] mandatory fields are missing";
        assertMandatoryFieldMissingMessage(missingMandatoryHeaderErrorMessage, "src/test/resources/Excels/REST-URLMissing.xlsx");
    }

    @Test(expected = MandatoryFieldMissingException.class)
    public void testContentTypeFieldMissing() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException, IdaithalamException {
        String missingMandatoryHeaderErrorMessage = "Sheet Name : API-Testing - Row : 3 - Type : REST - [ContentType] mandatory fields are missing";
        assertMandatoryFieldMissingMessage(missingMandatoryHeaderErrorMessage, "src/test/resources/Excels/REST-ContentTypeMissing.xlsx");
    }

    @Test(expected = MandatoryFieldMissingException.class)
    public void testResourceFieldMissing() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException, IdaithalamException {
        String missingMandatoryHeaderErrorMessage = "Sheet Name : API-Testing - Row : 3 - Type : REST - [Resource] mandatory fields are missing";
        assertMandatoryFieldMissingMessage(missingMandatoryHeaderErrorMessage, "src/test/resources/Excels/REST-ResourceMissing.xlsx");
    }

    @Test(expected = MandatoryFieldMissingException.class)
    public void testDBResourceFieldMissing() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException, IdaithalamException {
        String missingMandatoryHeaderErrorMessage = "Sheet Name : API-Testing - Row : 3 - Type : DB - [Resource] mandatory fields are missing";
        assertMandatoryFieldMissingMessage(missingMandatoryHeaderErrorMessage, "src/test/resources/Excels/DB-ResourceMissing.xlsx");
    }


    @Test(expected = MandatoryFieldMissingException.class)
    public void testDBTestCaseFieldMissing() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException, IdaithalamException {
        String missingMandatoryHeaderErrorMessage = "Sheet Name : API-Testing - Row : 2 - Type : DB - [TestCaseName] mandatory fields are missing";
        assertMandatoryFieldMissingMessage(missingMandatoryHeaderErrorMessage, "src/test/resources/Excels/DB-TestCaseNameMissing.xlsx");
    }
    @Test(expected = MandatoryFieldMissingException.class)
    public void testDBTestCaseNameDescFieldMissing() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException, IdaithalamException {
        String missingMandatoryHeaderErrorMessage = "Sheet Name : API-Testing - Row : 3 - Type : DB - [TestCaseNameDesc] mandatory fields are missing";
        assertMandatoryFieldMissingMessage(missingMandatoryHeaderErrorMessage, "src/test/resources/Excels/DB-TestCaseNameDescMissing.xlsx");
    }
    private void assertMandatoryFieldMissingMessage(String missingMandatoryHeaderErrorMessage, String fileName) throws IOException, UnableToProcessException, IdaithalamException {

        IdaithalamConfiguration.setProperty("workflow", "Disabled");
        String basePath = "src/test/resources/Excels";
        File inputFile = new File(fileName);
        String generatedPath = OUTPUT_FOLDER_NAME+inputFile.getName();
        if (!new File(generatedPath).exists()) {
            new File(generatedPath).mkdirs();
        }
        ApiExecutorParam apiExecutorParam = new ApiExecutorParam();
        apiExecutorParam.setBasePath(basePath);
        apiExecutorParam.setInputExcel(inputFile.getName());
        apiExecutorParam.setOutputDir(generatedPath);
        try {
            boolean actual = ExcelToCollectionGenerator.createCollection(apiExecutorParam);
        }catch (MandatoryFieldMissingException mandatoryFieldMissingException){
            Assert.assertTrue(mandatoryFieldMissingException.getMessage().contains(missingMandatoryHeaderErrorMessage));
            throw mandatoryFieldMissingException;
        }
    }
    private List<String> injectMockLogger() throws NoSuchFieldException, IllegalAccessException {
        Logger log = Mockito.mock(Logger.class);
        List<String> logMessages = new ArrayList<>();
        doAnswer(invocation -> {
            String arg0 = invocation.getArgumentAt(0, String.class);
            logMessages.add(arg0.trim());
            return null;
        }).when(log).error(anyString());

        doAnswer(invocation -> {
            String arg0 = invocation.getArgumentAt(0, String.class);
            logMessages.add(arg0.trim());
            return null;
        }).when(log).info(anyString());

        doAnswer(invocation -> {
            String arg0 = invocation.getArgumentAt(0, String.class);
            logMessages.add(arg0.trim());
            return null;
        }).when(log).warn(anyString());
        Field field = ExcelToCollectionGenerator.class.getDeclaredField("log");
        field.setAccessible(true);
        field.set(ExcelToCollectionGenerator.class, log);
        return logMessages;
    }

    private void removeMockLogger() throws NoSuchFieldException, IllegalAccessException {
        Logger log = LoggerFactory.getLogger(ExcelToCollectionGenerator.class);
        Field field = ExcelToCollectionGenerator.class.getDeclaredField("log");
        field.setAccessible(true);
        field.set(ExcelToCollectionGenerator.class, log);
    }



    private void createCollection(File inputFile, String basePath) throws UnableToProcessException {

        try {
            String generatedPath = OUTPUT_FOLDER_NAME+inputFile.getName();
            if (!new File(generatedPath).exists()) {
                new File(generatedPath).mkdirs();
            }
            ApiExecutorParam apiExecutorParam = new ApiExecutorParam();
            apiExecutorParam.setBasePath(basePath);
            apiExecutorParam.setInputExcel(inputFile.getName());
            apiExecutorParam.setOutputDir(generatedPath);
            boolean actual = ExcelToCollectionGenerator.createCollection(apiExecutorParam);
            if(!actual){
                return ;
            }
            Assert.assertTrue(actual);

            File expectedFileFolder = new File("src/test/resources/expected/"+inputFile.getName());
            File outputFileFolder = new File(OUTPUT_FOLDER_NAME+inputFile.getName());
            String ouputFolderAssertMessage =  outputFileFolder.exists()? OUTPUT_FOLDER_NAME+inputFile.getName()+" is generated ":OUTPUT_FOLDER_NAME+inputFile.getName()+" is not generated";
            Assert.assertTrue(ouputFolderAssertMessage, outputFileFolder.exists());
            log.info("Input File : "+inputFile.getName() +", Number of expected files :"+expectedFileFolder.listFiles().length + ", Number of generated files :"+outputFileFolder.listFiles().length);
            String countAssertMessage  = (expectedFileFolder.listFiles().length==outputFileFolder.listFiles().length) ? "Expected  and generated files count is same" : "Expected  and generated files count is not same";
            Assert.assertTrue(countAssertMessage,(expectedFileFolder.listFiles().length==outputFileFolder.listFiles().length));
            Stream.of(expectedFileFolder.listFiles()).forEach(expectedFile->{
                File outputFile = new File(OUTPUT_FOLDER_NAME+inputFile.getName()+"/"+expectedFile.getName());
                String outputFileAssertMessage = outputFile.exists()?outputFile.getName()+" is generated" : outputFile.getName()+" is not generated";
                Assert.assertTrue(outputFileAssertMessage, outputFile.exists());
                if(expectedFile.getName().endsWith("json")) {
                    try {
                        JSONArray expectedSheet_0 = new JSONArray(getFileAsString("expected/"+inputFile.getName()+"/"+expectedFile.getName()));
                        JSONArray actualSheet_0 = new JSONArray(getFileAsString(new FileInputStream(OUTPUT_FOLDER_NAME+inputFile.getName()+"/"+expectedFile.getName())));
                        JSONCompareResult result = JSONCompare.compareJSON(expectedSheet_0, actualSheet_0, JSONCompareMode.STRICT);
                        if(result.failed()) {
                            Assert.assertTrue(result.getMessage(), result.passed());

                        }

                    } catch (JSONException | IOException e) {
                        log.error("Exception occured : "+e.getMessage(),e);
                        Assert.assertTrue(false);
                    }


                } else if(expectedFile.getName().endsWith("properties")) {

                    Properties expectedCucmblanProperties = new Properties();
                    Properties actualCucmblanProperties = new Properties();
                    try {

                        expectedCucmblanProperties.load(getFileAsStream("expected/"+inputFile.getName()+"/"+expectedFile.getName()));
                        actualCucmblanProperties.load(new FileInputStream(OUTPUT_FOLDER_NAME+inputFile.getName()+"/"+expectedFile.getName()));
                        Assert.assertTrue(expectedCucmblanProperties.equals(actualCucmblanProperties));
                    } catch (IOException e) {
                        log.error("Exception occured : "+e.getMessage(),e);
                        Assert.assertTrue(false);
                    }



                }
            });


            extractExcelInfo(apiExecutorParam);
        }catch (UnableToProcessException exception){
            throw exception;
        }
        catch (Exception e) {
            log.error("Exception occured : "+e.getMessage(),e);
            Assert.assertTrue(false);
        }
	
    }


    private void extractExcelInfo(ApiExecutorParam apiExecutorParam) throws IOException {
       
        InputStream stream = new FileInputStream(new File(apiExecutorParam.getBasePath()+"\\"+
                apiExecutorParam.getInputExcel()));
        Workbook workbook = new XSSFWorkbook(stream);
        List<String> fileNames = Arrays.asList(new File(apiExecutorParam.getOutputDir()).list());

        for (int sheet = 0; sheet < workbook.getNumberOfSheets(); sheet++) {
            Sheet firstSheet = workbook.getSheetAt(sheet);
            int testCaseNameHeaderIndex = getTestCaseNameColumnIndex(firstSheet);
            for(String fileName : fileNames){
                if(fileName.equals(firstSheet.getSheetName()+"-"+sheet+".json")) {
                    log.info("File  Name : "+fileName +" Sheet Name "+firstSheet.getSheetName());
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        String fileContent =  FileUtils.readFileToString(new File(apiExecutorParam.getOutputDir()+"/"+fileName));
                        List<HashMap<String, Object>> itemLists = objectMapper.readValue(fileContent,List.class);

                        for(int i=0;i<itemLists.size();i++){
                            if(firstSheet.getRow(i+1).getCell(testCaseNameHeaderIndex) != null &&
                                    StringUtils.isNotEmpty(firstSheet.getRow(i+1).getCell(testCaseNameHeaderIndex).getStringCellValue() ) ) {
                                Assert.assertEquals(firstSheet.getRow(i + 1).getCell(testCaseNameHeaderIndex).getStringCellValue(), itemLists.get(i).get("scenarioId"));
                            }
                        }
                    } catch (JSONException | IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    private List<String>  getExcelSheetNames(File inputExcelFile) throws IOException {
        List<String> sheetNames = new ArrayList<>();
        InputStream stream = new FileInputStream(inputExcelFile);
        Workbook workbook = new XSSFWorkbook(stream);

        for (int sheet = 0; sheet < workbook.getNumberOfSheets(); sheet++) {
            Sheet firstSheet = workbook.getSheetAt(sheet);
            sheetNames.add(firstSheet.getSheetName());
        }
        return sheetNames;
    }

    private int getTestCaseNameColumnIndex(Sheet firstSheet) {
        int testCaseNameColumnIndex = -1;
        String TEST_CASE_NAME_HEADER="TestCaseName";
        for(int i=0; i<firstSheet.getRow(0).getLastCellNum();i++){
            if(firstSheet.getRow(0).getCell(i).getStringCellValue().equals(TEST_CASE_NAME_HEADER)) {
                testCaseNameColumnIndex = i;
                break;
            }
        }
        return testCaseNameColumnIndex;
    }

    @Test
    public void testgetObjectSheetMissingMandatoryHeaderRest() throws IOException, InvalidFormatException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
       String testCaseNameMissingMsg = "Sheet Name : REST - Row : 2 - Type : REST - [TestCaseName] mandatory fields are missing";
        String testCaseDescNameMissingMsg = "Sheet Name : REST - Row : 3 - Type : REST - [TestCaseNameDesc] mandatory fields are missing";
        String urlMissingMsg = "Sheet Name : REST - Row : 4 - Type : REST - [URL] mandatory fields are missing";
        String actionMissingMsg = "Sheet Name : REST - Row : 5 - Type : REST - [Action] mandatory fields are missing";
        String statusCodeMissingMsg = "Sheet Name : REST - Row : 6 - Type : REST - [StatusCode] mandatory fields are missing";
        String basePath = "src/test/resources/Excels";
        File inputFile = new File("src/test/resources/Excels/testgetObjectSheetMissingMandatoryHeaderRest.xlsx");
        Workbook workbook = new XSSFWorkbook(inputFile);
        Sheet restSheet = workbook.getSheet("REST");
        Method method = ExcelToCollectionGenerator.class.getDeclaredMethod("getObjectSheet", int.class, List.class, SheetObject.class, List.class);
        method.setAccessible(true);
        ExcelToCollectionGenerator excelToCollectionGenerator = new ExcelToCollectionGenerator();
        SheetObject sheetObject = new SheetObject();
        sheetObject.setFirstSheet(restSheet);
        sheetObject.setBasePath(basePath);

        Map<String, String> excludeResponseMap = new HashMap<>();


        Map<String, String> cucumblanMap = new HashMap<>();
        cucumblanMap.put("virtualan.data.load", "");
        cucumblanMap.put("virtualan.data.heading", "");
        cucumblanMap.put("virtualan.data.type", "VIRTUALAN");
        sheetObject.setCucumblanMap(cucumblanMap);
        sheetObject.setExcludeResponseMap(excludeResponseMap);
        List<String> logMessages = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) method.invoke(excelToCollectionGenerator, 1, new ArrayList<String>(), sheetObject, logMessages);
        Assert.assertTrue(!jsonArray.isEmpty());
        Assert.assertTrue(logMessages.contains(testCaseNameMissingMsg));
        Assert.assertTrue(logMessages.contains(testCaseDescNameMissingMsg));
        Assert.assertTrue(logMessages.contains(urlMissingMsg));
        Assert.assertTrue(logMessages.contains(actionMissingMsg));
        Assert.assertTrue(logMessages.contains(statusCodeMissingMsg));

    }

    @Test
    public void testgetObjectSheetMissingMandatoryHeaderDB() throws IOException, InvalidFormatException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
       List<String> logMsgs = new ArrayList<>();
        String testCaseNameMissingMsg = "Sheet Name : DB - Row : 2 - Type : DB - [TestCaseName] mandatory fields are missing";
        String testCaseDescNameMissingMsg = "Sheet Name : DB - Row : 3 - Type : DB - [TestCaseNameDesc] mandatory fields are missing";
        String resourceMissingMsg = "Sheet Name : DB - Row : 4 - Type : DB - [Resource] mandatory fields are missing";
        String basePath = "src/test/resources/Excels";
        File inputFile = new File("src/test/resources/Excels/testgetObjectSheetMissingMandatoryHeaderRest.xlsx");
        Workbook workbook = new XSSFWorkbook(inputFile);
        Sheet restSheet = workbook.getSheet("DB");
        Method method = ExcelToCollectionGenerator.class.getDeclaredMethod("getObjectSheet", int.class, List.class, SheetObject.class, List.class);
        method.setAccessible(true);
        ExcelToCollectionGenerator excelToCollectionGenerator = new ExcelToCollectionGenerator();
        SheetObject sheetObject = new SheetObject();
        sheetObject.setFirstSheet(restSheet);
        sheetObject.setBasePath(basePath);

        Map<String, String> excludeResponseMap = new HashMap<>();


        Map<String, String> cucumblanMap = new HashMap<>();
        cucumblanMap.put("virtualan.data.load", "");
        cucumblanMap.put("virtualan.data.heading", "");
        cucumblanMap.put("virtualan.data.type", "VIRTUALAN");
        sheetObject.setCucumblanMap(cucumblanMap);
        sheetObject.setExcludeResponseMap(excludeResponseMap);
        JSONArray jsonArray = (JSONArray) method.invoke(excelToCollectionGenerator, 1, new ArrayList<String>(), sheetObject, logMsgs);
        Assert.assertTrue(!jsonArray.isEmpty());
        log.info(logMsgs.toString());
        Assert.assertTrue(logMsgs.contains(testCaseNameMissingMsg));
        Assert.assertTrue(logMsgs.contains(testCaseDescNameMissingMsg));
        Assert.assertTrue(logMsgs.contains(resourceMissingMsg));

    }
    
}
