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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostmanTests {

    @Test
    public void addApikey() throws IOException{
        String generatedPath = "target/1"; //TODO
        if (!new File(generatedPath).exists()) {
            new File(generatedPath).mkdirs();
        }
        Yaml yaml = new Yaml(new Constructor(ExecutionPlanner.class));
        InputStream inputStream = VirtualanTestPlanExecutor.class.getClassLoader()
                .getResourceAsStream("work-flow-apikey.yaml");
        //Check reading apiHeader
        ExecutionPlanner executionPlanner = yaml.load(inputStream);
        List<ApiExecutorParam> apiExecutor = executionPlanner.getApiExecutor();
        Assert.assertTrue( apiExecutor.size() > 0);
        
        ApiExecutorParam apiExecutorParam1 = apiExecutor.get(0);
        List<Map<String, String>> apiHeaderList = apiExecutorParam1.getApiHeader();
        Assert.assertTrue(apiHeaderList.size() > 0);
        boolean checkApikey = false;
        for ( Map<String, String> map : apiHeaderList){
            checkApikey = checkApikey || map.get("X-API-KEY").equals("abc123");
        }
        Assert.assertTrue(checkApikey);
        
        
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
