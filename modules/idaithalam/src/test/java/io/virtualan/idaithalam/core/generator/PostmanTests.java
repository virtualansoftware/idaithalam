package io.virtualan.idaithalam.core.generator;

import io.virtualan.idaithalam.config.IdaithalamConfiguration;
import io.virtualan.idaithalam.core.UnableToProcessException;
import io.virtualan.idaithalam.core.api.VirtualanTestPlanExecutor;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.domain.ExecutionPlanner;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostmanTests {

    @Test
    public void addApikey() throws IOException{
        String basePath = "target";
//        URL excelFilePath = ExcelToCollectionGeneratorTests.class.getClassLoader().getResource("customer-self-service-with-db.xlsx");
        String generatedPath = "target/1"; //TODO
        if (!new File(generatedPath).exists()) {
            new File(generatedPath).mkdirs();
        }
        ApiExecutorParam apiExecutorParam = new ApiExecutorParam();
        apiExecutorParam.setBasePath(basePath);
//        apiExecutorParam.setInputExcel(excelFilePath.getPath());
        apiExecutorParam.setInputFile("idaithalamserver_withoutapikey.postman_collection.json");
        apiExecutorParam.setOutputDir(generatedPath);

        Yaml yaml = new Yaml(new Constructor(ExecutionPlanner.class));
        InputStream inputStream = VirtualanTestPlanExecutor.class.getClassLoader()
                .getResourceAsStream("work-flow-apikey.yaml");
        ExecutionPlanner executionPlanner = yaml.load(inputStream);
        Properties expectedCucmblanProperties = new Properties();
//        expectedCucmblanProperties.load(getFileAsStream("expected/apikeyTest/work-flow-apikey.yaml"));
        
//        Properties actualCucmblanProperties = new Properties();
//        actualCucmblanProperties.load(new FileInputStream("target/1/cucumblan.properties"));
//        Assertions.assertTrue(expectedCucmblanProperties.equals(actualCucmblanProperties));
//
//        Properties expectedCucmblanEnvProperties = new Properties();
//        expectedCucmblanEnvProperties.load(getFileAsStream("expected/createCollectionTest_HappyPath/cucumblan-env.properties"));
//        Properties actualCucmblanEnvProperties = new Properties();
//        actualCucmblanEnvProperties.load(new FileInputStream("target/1/cucumblan-env.properties"));
//        Assertions.assertTrue(expectedCucmblanEnvProperties.equals(actualCucmblanEnvProperties));
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


}
