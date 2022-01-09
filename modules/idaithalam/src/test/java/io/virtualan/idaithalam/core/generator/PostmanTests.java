package io.virtualan.idaithalam.core.generator;

import io.virtualan.idaithalam.core.api.VirtualanTestPlanExecutor;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.domain.ExecutionPlanner;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PostmanTests {

    @Test
    public void addApikey() throws Exception {
        final String WORKFLOWYAML = "postman/folders/folders.yaml";
        Yaml yaml = new Yaml(new Constructor(ExecutionPlanner.class));
        InputStream inputStream = VirtualanTestPlanExecutor.class.getClassLoader()
                .getResourceAsStream(WORKFLOWYAML);
        //Check reading apiHeader
        ExecutionPlanner executionPlanner = yaml.load(inputStream);
        List<ApiExecutorParam> apiExecutor = executionPlanner.getApiExecutor();
        Assert.assertTrue(apiExecutor.size() > 0);

        ApiExecutorParam apiExecutorParam1 = apiExecutor.get(0);
        List<Map<String, Object>> apiHeaderList = apiExecutorParam1.getApiHeader().getHeaderList();
        Assert.assertNotNull(apiHeaderList);
        boolean checkApikey = false;
        for (Map<String, Object> map : apiHeaderList) {
            checkApikey = checkApikey || (map.get("X-API-KEY") != null && map.get("X-API-KEY").toString().equals("abc123"));
        }
        Assert.assertTrue(checkApikey);
    }

    /** Issue 121: Multiple API key values must be a list if overwrite: false. 
     * */
    @Test
    public void duplicateApikey() throws Exception {
        final String WORKFLOWYAML = "postman/duplicateheader/duplicateheader.yaml"; //Only generate
        VirtualanTestPlanExecutor.invoke(WORKFLOWYAML);
        File file1 = new File("src/test/resources/postman/duplicateheader/apiheaderreference.feature");
        File file2 = new File("target/POSTMANTESTREPORTSAPIHEADER/feature/virtualan-contract.0.feature");
        Assert.assertTrue(file1.isFile());
        Assert.assertTrue(file1.isFile());
        assertEquals("There is a breaking change in the Feature file fpr multiple API header keys!",
                FileUtils.readFileToString(file1, "utf-8"),
                FileUtils.readFileToString(file2, "utf-8"));

    }

    /** Issue 124: Tests after the first folder were lost. 
     * */
    @Test
    public void folderStructure() throws Exception {
        final String WORKFLOWYAML = "postman/folders/folders.yaml"; //Only generate
        VirtualanTestPlanExecutor.invoke(WORKFLOWYAML);
        File file1 = new File("src/test/resources/postman/folders/foldersreference.feature");
        File file2 = new File("target/POSTMANTESTFOLDERS/feature/virtualan-contract.0.feature");
        Assert.assertTrue(file1.isFile());
        Assert.assertTrue(file1.isFile());
        assertEquals("There is a breaking change in the Feature file for the Postman folder structure!",
                FileUtils.readFileToString(file1, "utf-8"),
                FileUtils.readFileToString(file2, "utf-8"));

    }

}
