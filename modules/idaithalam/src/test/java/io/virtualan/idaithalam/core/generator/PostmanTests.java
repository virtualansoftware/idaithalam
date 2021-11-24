package io.virtualan.idaithalam.core.generator;

import io.virtualan.idaithalam.core.api.VirtualanTestPlanExecutor;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.domain.ExecutionPlanner;
import org.junit.jupiter.api.Assertions;
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
        final String yamlFile = "work-flow-apikey.yaml";
        Yaml yaml = new Yaml(new Constructor(ExecutionPlanner.class));
        InputStream inputStream = VirtualanTestPlanExecutor.class.getClassLoader()
                .getResourceAsStream(yamlFile);
        //Check reading apiHeader
        ExecutionPlanner executionPlanner = yaml.load(inputStream);
        List<ApiExecutorParam> apiExecutor = executionPlanner.getApiExecutor();
        Assertions.assertTrue(apiExecutor.size() > 0);

        ApiExecutorParam apiExecutorParam1 = apiExecutor.get(0);
        List<Map<String, Object>> apiHeaderList = apiExecutorParam1.getApiHeader().getHeaderList();
        Assertions.assertNotNull(apiHeaderList);
        boolean checkApikey = false;
        for (Map<String, Object> map : apiHeaderList) {
            checkApikey = checkApikey || (map.get("X-API-KEY") != null && map.get("X-API-KEY").toString().equals("abc123"));
        }
        Assertions.assertTrue(checkApikey);

        boolean isSuccess = VirtualanTestPlanExecutor.invoke(yamlFile);
        Assertions.assertTrue(isSuccess);
    }

}
