package io.virtualan.idaithalam.core.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.virtualan.idaithalam.config.IdaithalamConfiguration;
import io.virtualan.idaithalam.core.UnableToProcessException;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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
    public void testvirtualan_bdd_testcase_run_manager() {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_bdd_testcase_run_manager.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_pet_empty() {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_pet_empty.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_pet_sheet_2() {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_pet_sheet_2.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_0() {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_0.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_01() {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_01.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_02() {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_02.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_2() {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_2.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_3() {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_3.xlsx");
         createCollection(inputFile, basePath);
    }
    

    @Test
    public void testvirtualan_collection_testcase_5() {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_5.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_6() {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_6.xlsx");
         createCollection(inputFile, basePath);
    }
    
    @Test
    public void testvirtualan_collection_testcase_7() {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_7.xlsx");
         createCollection(inputFile, basePath);
    }
    
    
    @Test
    public void testvirtualan_collection_testcase_8() throws IOException {
        IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_8.xlsx");
         createCollection(inputFile, basePath);
    }

    @Test
    public void testFileNotFoundSecenario() throws NoSuchFieldException, IllegalAccessException {
        String fileMissingMsg = "File is missing(src/test/resources/Excels) : virtualan_collection_testcase_8-filenotthere.xlsx";
        List<String> logMessages = injectMockLogger();
        IdaithalamConfiguration.setProperty("workflow", "Disabled");
        String basePath = "src/test/resources/Excels";
        File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_8-filenotthere.xlsx");
       createCollection(inputFile, basePath);
       System.out.println("Log Message "+logMessages);
        removeMockLogger();
        Assert.assertTrue(logMessages.contains(fileMissingMsg));
    }


    @Test
    public void testInvalidOutputDirectory() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException {
        String unableToGenerateCucumbalamProperties = "Unable to generate cucumblan.properties properties  E:\\cucumblan.properties (The system cannot find the path specified)";
        String unableToGenerateCucumbalamEnvProperties = "Unable to generate cucumblan-env.properties properties  E:\\cucumblan-env.properties (The system cannot find the path specified)";
        List<String> logMsgs = injectMockLogger();
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

        boolean actual = ExcelToCollectionGenerator.createCollection(apiExecutorParam);
        removeMockLogger();
        Assert.assertTrue(logMsgs.contains(unableToGenerateCucumbalamEnvProperties));
        Assert.assertTrue(logMsgs.contains(unableToGenerateCucumbalamProperties));
    }

    @Test
    public void testBuildCollectionsCreateColectionMethod() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        List<String> logMsgs = injectMockLogger();
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
        removeMockLogger();
    }
    @Test
   public void testMissingMandatoryHeaderException() throws IOException, UnableToProcessException, NoSuchFieldException, IllegalAccessException {
        String missingMandatoryHeaderErrorMessage = "Mandatory headers [TestCaseName] are missing";
        List<String> logMsgs = injectMockLogger();
        IdaithalamConfiguration.setProperty("workflow", "Disabled");
        String basePath = "src/test/resources/Excels";
        File inputFile = new File("src/test/resources/Excels/TestCaseColumnHeaderIsMissing.xlsx");
        String generatedPath = OUTPUT_FOLDER_NAME+inputFile.getName();
        if (!new File(generatedPath).exists()) {
            new File(generatedPath).mkdirs();
        }
        ApiExecutorParam apiExecutorParam = new ApiExecutorParam();
        apiExecutorParam.setBasePath(basePath);
        apiExecutorParam.setInputExcel(inputFile.getName());
        apiExecutorParam.setOutputDir(generatedPath);

        boolean actual = ExcelToCollectionGenerator.createCollection(apiExecutorParam);
        removeMockLogger();

        Assert.assertTrue(logMsgs.contains(missingMandatoryHeaderErrorMessage));

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



    private void createCollection(File inputFile, String basePath){

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
        }catch (Exception e) {
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
                        Assert.assertEquals(firstSheet.getLastRowNum(), itemLists.size());

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

    
}
