package io.virtualan.test;

import io.virtualan.idaithalam.contract.IdaithalamExecutor;
import io.virtualan.idaithalam.core.contract.validator.ExcelToCollectionGenerator;
import java.util.ArrayList;
import java.util.List;
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
        int status =0 ;
        try {
            List<String> list = new ArrayList<>();
            list.add("PetPost");
            ExcelToCollectionGenerator.createCollection(list, "virtualan_collection_pet.xlsx", System.getProperty("user.dir") +"/target/classes/");
            status = IdaithalamExecutor.validateContract("Pet API EXCEL based api testing", System.getProperty("user.dir") +"/target/classes/");
            System.out.println(status);
            if(status != 0) {
                Assert.assertTrue(false);
            }
            Assert.assertTrue   (true);
        }catch (Exception e){
            System.out.println(e.getMessage());
            Assert.assertTrue(false);
        }

    }
}