package io.virtualan.test;


import io.virtualan.idaithalam.core.api.VirtualanTestPlanExecutor;
import org.junit.Assert;
import org.testng.annotations.Test;

public class BatchAPITestExecutionManager {


  @Test
  public static void main() {
    try {
       boolean isSuccess = VirtualanTestPlanExecutor.invoke("apiexecution.yaml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}