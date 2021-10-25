package io.virtualan.idaithalam.core.generator;

import io.virtualan.idaithalam.core.api.VirtualanTestPlanExecutor;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.domain.ExecutionPlanner;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostmanTests {

    @Test
    public void addApikey() throws Exception {
        Yaml yaml = new Yaml(new Constructor(ExecutionPlanner.class));
        InputStream inputStream = VirtualanTestPlanExecutor.class.getClassLoader()
                .getResourceAsStream("work-flow-apikey.yaml");
        //Check reading apiHeader
        ExecutionPlanner executionPlanner = yaml.load(inputStream);
        List<ApiExecutorParam> apiExecutor = executionPlanner.getApiExecutor();
        Assert.assertTrue( apiExecutor.size() > 0);

        ApiExecutorParam apiExecutorParam1 = apiExecutor.get(0);
        List<Map<String, String>> apiHeaderList = apiExecutorParam1.getApiHeader().getHeaderList();
        Assert.assertNotNull(apiHeaderList);
        boolean checkApikey = false;
        for (Map<String, String> map : apiHeaderList) {
            checkApikey = checkApikey || map.get("X-API-KEY").equals("abc123");
        }
        Assert.assertTrue(checkApikey);

//        boolean isSuccess = VirtualanTestPlanExecutor.invoke("work-flow-apikey.yaml");
//        Assert.assertTrue(isSuccess);
    }

}
