package io.virtualan.idaithalam.core.generator;

import static org.mockito.Mockito.when;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.virtualan.idaithalam.config.IdaithalamConfiguration;
import io.virtualan.idaithalam.core.UnableToProcessException;
import io.virtualan.idaithalam.core.api.VirtualanTestPlanExecutor;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.domain.ExecutionPlanner;
import io.virtualan.idaithalam.core.domain.Item;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;


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
    public void testvirtualan_collection_testcase_8() {
    	IdaithalamConfiguration.setProperty("workflow", "Disabled");
    	 String basePath = "src/test/resources/Excels";
         File inputFile = new File("src/test/resources/Excels/virtualan_collection_testcase_8.xlsx");
         createCollection(inputFile, basePath);
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

            fileNames.forEach(e->{
                if(e.contains(firstSheet.getSheetName())) {
//                    try {

//                        Gson gson = new GsonBuilder().create();
//                        JSONArray jsonArray = new JSONArray(Files.readString(Paths.get(apiExecutorParam.getOutputDir()+"/"+e)));
//                        //Assert.assertEquals(jsonArray.length(), firstSheet.getLastRowNum()-1);
//                        for(int i=0; i<jsonArray.length();i++) {
//                            Item item = gson.fromJson(jsonArray.get(i).toString(), Item.class);
//                            Row row = firstSheet.getRow(i+1);
//                            Assert.assertEquals(item.getScenario().toString(), row.getCell(3).toString());
//                            Assert.assertEquals(item.isRest()==true?"REST":"DB", row.getCell(1).toString());
//                        }



//                    } catch (JSONException | IOException e1) {
//                        // TODO Auto-generated catch block
//                        e1.printStackTrace();
//                    }
                }
            });
        }
    }

    
}
