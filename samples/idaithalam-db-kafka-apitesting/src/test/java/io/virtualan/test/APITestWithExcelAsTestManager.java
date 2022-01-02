package io.virtualan.test;


import io.virtualan.idaithalam.core.api.VirtualanTestPlanExecutor;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
public class APITestWithExcelAsTestManager {


    @BeforeClass
    public void testBeforeClass(){
    }

    @AfterClass
    public  void testAfterClass(){
    }


  @Test
  public static void execute_workflow() {
    try {
      boolean isSuccess = VirtualanTestPlanExecutor.invoke("work-flow.yaml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}