package io.virtualan.test;

import io.virtualan.idaithalam.contract.IdaithalamExecutor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

public class DemoApiTest {
    @Test
    public void validateContract()  {
        int status =0 ;
        try {
            status = IdaithalamExecutor.validateContract("Pet API Production Checkout");
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