package io.virtualan.test;

import io.virtualan.idaithalam.core.api.VirtualanTestPlanExecutor;
import org.junit.Assert;
import org.openapitools.OpenAPI2SpringBoot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import io.virtualan.idaithalam.core.UnableToProcessException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenAPI2SpringBoot.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)

public class DemoApiTest {
    @Test
    public void validateContract()  {
//        int status =0 ;
//        try {
//            status = IdaithalamExecutor.validateContract("Pet API Production Checkout");
//            if(status != 0) {
//                Assert.assertTrue(false);
//            }
//            Assert.assertTrue   (true);
//        }catch (Exception e){
//            Assert.assertTrue(false);
//        }

    }


    //Exlude column testing
    @Test
    public void virtualan_collection_testcase_5() {
        //log.info("Start - virtualan_collection_testcase_5");
        try {
            boolean isSuccess = VirtualanTestPlanExecutor
                    .invoke("openapi.yaml");
            // We are expecting date comparison should be failed. that's why we are asserting as false.
            //need to check
            Assert.assertTrue(isSuccess);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.assertTrue(false);
        }
        //log.info("End - virtualan_collection_testcase_5");
    }
}