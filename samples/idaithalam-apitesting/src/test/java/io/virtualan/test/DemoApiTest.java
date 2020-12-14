package io.virtualan.test;

import io.virtualan.idaithalam.contract.IdaithalamExecutor;
import org.junit.Assert;
import org.openapitools.OpenAPI2SpringBoot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenAPI2SpringBoot.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

public class DemoApiTest {
    @Test
    public void validateContract() {
        IdaithalamExecutor.validateContract("Pet API Production Checkout");
        Assert.assertTrue(true);
    }
}