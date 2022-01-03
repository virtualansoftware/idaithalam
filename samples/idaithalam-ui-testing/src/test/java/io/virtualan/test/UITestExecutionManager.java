package io.virtualan.test;


import io.virtualan.idaithalam.core.api.VirtualanTestPlanExecutor;
import org.junit.Assert;
import org.testng.annotations.Test;

public class UITestExecutionManager {


  @Test
  public void main() {
    try {
       boolean isSuccess = VirtualanTestPlanExecutor.invoke("ui-execution.yaml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}