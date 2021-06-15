package io.virtualan.test;

import io.virtualan.idaithalam.core.api.VirtualanTestPlanExecutor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

public class DemoApiTest {

  @Test
  public void executePostManCollection() {
    try {
      boolean isSuccess = VirtualanTestPlanExecutor.invoke("apiexecution.yaml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }
      Assert.assertTrue(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
  }

}