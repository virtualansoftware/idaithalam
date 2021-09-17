package io.virtualan.idaithalam.core.generator;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.stream.Collectors;

import io.virtualan.idaithalam.config.IdaithalamConfiguration;
import io.virtualan.idaithalam.core.UnableToProcessException;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.jassert.VirtualJSONAssert;
import org.json.JSONArray;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONCompareMode;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExcelToCollectionGeneratorTests {


    @Test
    public void convertStreamToStringTest() throws IOException {
        String input = "idaithalam";
        InputStream is = new ByteArrayInputStream(input.getBytes());
        String actual = ExcelToCollectionGenerator.convertStreamToString(is);
        Assertions.assertEquals(input + "\n", actual);
    }

    @Test
    public void convertStreamToStringNullTest() throws IOException {
        InputStream is = null;
        String actual = ExcelToCollectionGenerator.convertStreamToString(is);
        Assertions.assertNull(actual);
    }


    @Test
    @Order(1)
    public void createCollectionTest_HappyPath() throws IOException, UnableToProcessException, URISyntaxException {
        String basePath = "target";
        URL excelFilePath = ExcelToCollectionGeneratorTests.class.getClassLoader().getResource("customer-self-service-with-db.xlsx");
        String generatedPath = "target/1";
        if (!new File(generatedPath).exists()) {
            new File(generatedPath).mkdirs();
        }
        ApiExecutorParam apiExecutorParam = new ApiExecutorParam();
        apiExecutorParam.setBasePath(basePath);
        apiExecutorParam.setInputExcel(excelFilePath.getPath());
        apiExecutorParam.setOutputDir(generatedPath);
        boolean actual = ExcelToCollectionGenerator.createCollection(apiExecutorParam);
        Assertions.assertTrue(actual);
        JSONArray expectedSheet_0 = new JSONArray(getFileAsString("expected/createCollectionTest_HappyPath/Virtualan_0_CSS-Accept-DB_WORKFLOW_0.json"));
        JSONArray actualSheet_0 = new JSONArray(getFileAsString(new FileInputStream("target/1/Virtualan_0_CSS-Accept-DB_WORKFLOW_0.json")));
        VirtualJSONAssert.jAssertArray(expectedSheet_0, actualSheet_0, JSONCompareMode.STRICT);

        JSONArray expectedSheet_1 = new JSONArray(getFileAsString("expected/createCollectionTest_HappyPath/Virtualan_1_CSS-Reject-DB_WORKFLOW_1.json"));
        JSONArray actualSheet_1 = new JSONArray(getFileAsString(new FileInputStream("target/1/Virtualan_1_CSS-Reject-DB_WORKFLOW_1.json")));
        VirtualJSONAssert.jAssertArray(expectedSheet_1, actualSheet_1, JSONCompareMode.STRICT);

        Properties expectedCucmblanProperties = new Properties();
        expectedCucmblanProperties.load(getFileAsStream("expected/createCollectionTest_HappyPath/cucumblan.properties"));
        Properties actualCucmblanProperties = new Properties();
        actualCucmblanProperties.load(new FileInputStream("target/1/cucumblan.properties"));
        Assertions.assertTrue(expectedCucmblanProperties.equals(actualCucmblanProperties));

        Properties expectedCucmblanEnvProperties = new Properties();
        expectedCucmblanEnvProperties.load(getFileAsStream("expected/createCollectionTest_HappyPath/cucumblan-env.properties"));
        Properties actualCucmblanEnvProperties = new Properties();
        actualCucmblanEnvProperties.load(new FileInputStream("target/1/cucumblan-env.properties"));
        Assertions.assertTrue(expectedCucmblanEnvProperties.equals(actualCucmblanEnvProperties));

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
    @Order(2)
    public void createCollectionTest_With_Kafka() throws IOException, UnableToProcessException {
        String basePath = "target/2";
        URL excelFilePath = ExcelToCollectionGeneratorTests.class.getClassLoader().getResource("virtualan_collection_kafka_db_testcase_0.xlsx");
        String generatedPath = "target/2";
        if (!new File(generatedPath).exists()) {
            new File(generatedPath).mkdirs();
        }
        ApiExecutorParam apiExecutorParam = new ApiExecutorParam();
        apiExecutorParam.setBasePath(basePath);
        apiExecutorParam.setInputExcel(excelFilePath.getPath());
        apiExecutorParam.setOutputDir(generatedPath);
        boolean actual = ExcelToCollectionGenerator.createCollection(apiExecutorParam);

        Assertions.assertTrue(actual);

        JSONArray expectedSheet_0 = new JSONArray(getFileAsString("expected/createCollectionTest_With_Kafka/Virtualan_0_API-Testing_WORKFLOW_0.json"));
        JSONArray actualSheet_0 = new JSONArray(getFileAsString(new FileInputStream("target/2/Virtualan_0_API-Testing_WORKFLOW_0.json")));
        VirtualJSONAssert.jAssertArray(expectedSheet_0, actualSheet_0, JSONCompareMode.STRICT);

        Properties expectedCucmblanProperties = new Properties();
        expectedCucmblanProperties.load(getFileAsStream("expected/createCollectionTest_With_Kafka/cucumblan.properties"));
        Properties actualCucmblanProperties = new Properties();
        actualCucmblanProperties.load(new FileInputStream("target/2/cucumblan.properties"));
        Assertions.assertTrue(expectedCucmblanProperties.equals(actualCucmblanProperties));

        Properties expectedCucmblanEnvProperties = new Properties();
        expectedCucmblanEnvProperties.load(getFileAsStream("expected/createCollectionTest_With_Kafka/cucumblan-env.properties"));
        Properties actualCucmblanEnvProperties = new Properties();
        actualCucmblanEnvProperties.load(new FileInputStream("target/2/cucumblan-env.properties"));
        Assertions.assertTrue(expectedCucmblanEnvProperties.equals(actualCucmblanEnvProperties));

    }


    @Test
    @Order(3)
    public void createCollectionTest_Multirun() throws IOException, UnableToProcessException {
        String basePath = "target/4";
        URL excelFilePath = ExcelToCollectionGeneratorTests.class.getClassLoader().getResource("virtualan_collection_testcase_multirun.xlsx");
        String generatedPath = "target/4";
        if (!new File(generatedPath).exists()) {
            new File(generatedPath).mkdirs();
        }
        ApiExecutorParam apiExecutorParam = new ApiExecutorParam();
        apiExecutorParam.setBasePath(basePath);
        apiExecutorParam.setInputExcel(excelFilePath.getPath());
        apiExecutorParam.setOutputDir(generatedPath);
        boolean actual = ExcelToCollectionGenerator.createCollection(apiExecutorParam);

        Assertions.assertTrue(actual);

        JSONArray expectedSheet_0 = new JSONArray(getFileAsString("expected/createCollectionTest_Multirun/Virtualan_0_API-Testing_WORKFLOW_0.json"));
        JSONArray actualSheet_0 = new JSONArray(getFileAsString(new FileInputStream("target/4/Virtualan_0_API-Testing_WORKFLOW_0.json")));
        VirtualJSONAssert.jAssertArray(expectedSheet_0, actualSheet_0, JSONCompareMode.STRICT);

        Properties expectedCucmblanProperties = new Properties();
        expectedCucmblanProperties.load(getFileAsStream("expected/createCollectionTest_Multirun/cucumblan.properties"));
        Properties actualCucmblanProperties = new Properties();
        actualCucmblanProperties.load(new FileInputStream("target/4/cucumblan.properties"));
        Assertions.assertEquals(expectedCucmblanProperties, actualCucmblanProperties);

        Properties expectedCucmblanEnvProperties = new Properties();
        expectedCucmblanEnvProperties.load(getFileAsStream("expected/createCollectionTest_Multirun/cucumblan-env.properties"));
        Properties actualCucmblanEnvProperties = new Properties();
        actualCucmblanEnvProperties.load(new FileInputStream("target/4/cucumblan-env.properties"));
        Assertions.assertEquals(expectedCucmblanEnvProperties, actualCucmblanEnvProperties);

    }


    @Test
    @Order(4)
    public void createCollectionTest_Single_Workflow() throws IOException, UnableToProcessException {
        IdaithalamConfiguration.setProperty("workflow", "Enabled");
        String basePath = "target/3";
        URL excelFilePath = ExcelToCollectionGeneratorTests.class.getClassLoader().getResource("virtualan_collection_testcase_4.xlsx");
        String generatedPath = "target/3";
        if (!new File(generatedPath).exists()) {
            new File(generatedPath).mkdirs();
        }
        ApiExecutorParam apiExecutorParam = new ApiExecutorParam();
        apiExecutorParam.setBasePath(basePath);
        apiExecutorParam.setInputExcel(excelFilePath.getPath());
        apiExecutorParam.setOutputDir(generatedPath);
        boolean actual = ExcelToCollectionGenerator.createCollection(apiExecutorParam);

        Assertions.assertTrue(actual);

        JSONArray expectedSheet_0 = new JSONArray(getFileAsString("expected/createCollectionTest_Single_Workflow/Virtualan_0_PetGet.json"));
        JSONArray actualSheet_0 = new JSONArray(getFileAsString(new FileInputStream("target/3/Virtualan_0_PetGet.json")));
        VirtualJSONAssert.jAssertArray(expectedSheet_0, actualSheet_0, JSONCompareMode.STRICT);

        JSONArray expectedSheet = new JSONArray(getFileAsString("expected/createCollectionTest_Single_Workflow/Virtualan_1_PetGet.json"));
        JSONArray actualSheet = new JSONArray(getFileAsString(new FileInputStream("target/3/Virtualan_1_PetGet.json")));
        VirtualJSONAssert.jAssertArray(expectedSheet, actualSheet, JSONCompareMode.STRICT);

        Properties expectedCucmblanProperties = new Properties();
        expectedCucmblanProperties.load(getFileAsStream("expected/createCollectionTest_Single_Workflow/cucumblan.properties"));
        Properties actualCucmblanProperties = new Properties();
        actualCucmblanProperties.load(new FileInputStream("target/3/cucumblan.properties"));
        Assertions.assertTrue(expectedCucmblanProperties.equals(actualCucmblanProperties));

        Properties expectedCucmblanEnvProperties = new Properties();
        expectedCucmblanEnvProperties.load(getFileAsStream("expected/createCollectionTest_Single_Workflow/cucumblan-env.properties"));
        Properties actualCucmblanEnvProperties = new Properties();
        actualCucmblanEnvProperties.load(new FileInputStream("target/3/cucumblan-env.properties"));
        Assertions.assertTrue(expectedCucmblanEnvProperties.equals(actualCucmblanEnvProperties));
    }


}
