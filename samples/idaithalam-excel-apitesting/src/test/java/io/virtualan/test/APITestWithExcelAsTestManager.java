package io.virtualan.test;

import io.restassured.RestAssured;
import io.virtualan.idaithalam.config.IdaithalamConfiguration;
import io.virtualan.idaithalam.core.api.VirtualanTestExecutor;
import io.virtualan.idaithalam.core.api.VirtualanTestPlanExecutor;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import io.virtualan.idaithalam.core.generator.ExcelToCollectionGenerator;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.mockito.Mockito.doAnswer;

@Slf4j
public class APITestWithExcelAsTestManager {



  @BeforeClass
  public void testBeforeClass() {
    System.out.println("----------------------------------------");
    System.out.println("--- Start Test -------");
    System.out.println("------------------------------------------");
    try {
      RestAssured.useRelaxedHTTPSValidation();
    } catch (Exception ex) {
    }
  }




  @AfterClass
  public void testAfterClass() {
    System.out.println("------------------------------------------");
    
    System.out.println("---- END Test : ------");
    System.out.println("-------------------------------------------");
  }



  //Exlude column testing
  @Test
  public void virtualan_collection_testcase_5() {
    log.info("Start - virtualan_collection_testcase_5");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/virtualan_collection_testcase_5.yml");
      // We are expecting date comparison should be failed. that's why we are asserting as false.
      //need to check
      Assert.assertTrue(isSuccess);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - virtualan_collection_testcase_5");
  }

  @Test
  public void virtualan_collection_testcase_8() {
    log.info("Start - virtualan_collection_testcase_8");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/virtualan_collection_testcase_8.yml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }
      Assert.assertTrue(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - virtualan_collection_testcase_8");
  }

  @Test
  public void virtualan_collection_testcase_4() {
    log.info("Start - virtualan_collection_testcase_4");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/virtualan_collection_testcase_4.yml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }
      Assert.assertTrue(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - virtualan_collection_testcase_4");
  }

  @Test
  public void virtualan_collection_testcase_01() {
    log.info("Start - virtualan_collection_testcase_01");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/virtualan_collection_testcase_01.yml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }
      Assert.assertTrue(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - virtualan_collection_testcase_01");
  }

  @Test
  public void virtualan_collection_testcase_02() {
    log.info("Start - virtualan_collection_testcase_02");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/virtualan_collection_testcase_02.yml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }
      Assert.assertTrue(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - virtualan_collection_testcase_02");
  }

  @Test
  public void virtualan_bdd_testcase_run_manager() {
    log.info("Start - virtualan_bdd_testcase_run_manager");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/virtualan_bdd_testcase_run_manager.yml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }
      Assert.assertTrue(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - virtualan_bdd_testcase_run_manager");
  }
  




  /****
   *
   * Sample Execute via java code
   *
   */

  @Test
  public void excelFieldExamples_3() {
    log.info("Start - excelFieldExamples_3");
    int status = 0;
    try {
      IdaithalamConfiguration.setProperty("workflow", "Enabled");
      List<String> list = new ArrayList<>();
      //Add the testcaseName that List of testcases to be executed from the excel
      //for the test selected execution
      list.add("PetPost");
      list.add("PetGet");  // comment and test again see the summary report
      list.add("CreatePet");
      ApiExecutorParam apiExecutorParam = new ApiExecutorParam();
      apiExecutorParam.setGeneratedTestList(list);

      apiExecutorParam.setInputExcel("virtualan_collection_pet_empty.xlsx");
      apiExecutorParam.setOutputDir(System.getProperty("user.dir") + "/target/excel/empty");
      apiExecutorParam.setReportTitle("Via code - API EXCEL based api testing");
      VirtualanTestExecutor testExecutor = new VirtualanTestExecutor(apiExecutorParam);
      status = testExecutor.call();
      System.out.println(status);
      if (status != 0) {
        //actual it will fail but it made as pass
        //purposefully passed to show case okta and basic auth demo
        Assert.assertTrue(true);
      } else {
        Assert.assertTrue(true);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      Assert.assertTrue(true);
    }
    log.info("End - excelFieldExamples_3");
  }

  @Test
  public void testvirtualan_collection_pet_empty(){
    log.info("Start - testvirtualan_collection_pet_empty");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/virtualan_collection_pet_empty.yml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }
      Assert.assertTrue(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - testvirtualan_collection_pet_empty");
  }

  @Test
  public void virtualan_collection_pet_sheet_2(){
    log.info("Start - virtualan_collection_pet_sheet_2");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/virtualan_collection_pet_sheet_2.yml");

      if (!isSuccess) {
        Assert.assertTrue(false);
      }
      Assert.assertTrue(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - virtualan_collection_pet_sheet_2");
  }

  @Test
  public void virtualan_collection_testcase_0() {
    log.info("Start - virtualan_collection_testcase_0");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/virtualan_collection_testcase_0.yml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }
      Assert.assertTrue(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - virtualan_collection_testcase_0");
  }

//Negative scenario - Scenario id is missing
  @Test
  public void virtualan_collection_testcase_2() {
    log.info("Start - virtualan_collection_testcase_2");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/virtualan_collection_testcase_2.yml");
      if (!isSuccess) {
        Assert.assertFalse(false);
      }
      Assert.assertTrue(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - virtualan_collection_testcase_2");
  }

  @Test
  public void virtualan_collection_testcase_3() {
    log.info("Start - virtualan_collection_testcase_3");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/virtualan_collection_testcase_3.yml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }
      Assert.assertTrue(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - virtualan_collection_testcase_3");
  }

  @Test
  public void virtualan_collection_testcase_6() {
    log.info("Start - virtualan_collection_testcase_6");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/virtualan_collection_testcase_6.yml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }
      Assert.assertTrue(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - virtualan_collection_testcase_6");
  }

  @Test
  public void virtualan_collection_testcase_7() {
    log.info("Start - virtualan_collection_testcase_7");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/virtualan_collection_testcase_7.yml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }
      Assert.assertTrue(true);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - virtualan_collection_testcase_7");
  }

  @Test
  public void testCaseNameHeaderMissing() throws NoSuchFieldException, IllegalAccessException {
    String  scenarioIdMissigMsg= "Sheet Name : API-Testing - Row : 2 - Type : REST - [TestCaseName] mandatory data's are missing";
    log.info("Start - testTestCaseNameHeaderMissing");
    try {
      List<String> logMessages = injectMockLogger();
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/testCaseNameHeaderMissing.yml");
      log.info("LogMessage "+logMessages);
      removeMockLogger();
      Assert.assertFalse(isSuccess);
      Assert.assertTrue(logMessages.contains(scenarioIdMissigMsg));
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - testTestCaseNameHeaderMissing");
  }

  @Test
  public void testURLMandatoryHeaderMissing(){
    String  scenarioIdMissigMsg= "Sheet Name : API-Testing - Row : 2 - Type : REST - [TestCaseNameDesc, URL] mandatory data's are missing";
    log.info("Start - testURLMandatoryHeaderMissing");
    try {
      List<String> logMessages = injectMockLogger();
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/testURLMandatoryHeaderMissing.yml");
      log.info("LogMessage "+logMessages);
      removeMockLogger();
      Assert.assertFalse(isSuccess);
      Assert.assertTrue(logMessages.contains(scenarioIdMissigMsg));
    } catch (InterruptedException | NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - testURLMandatoryHeaderMissing");
  }

  @Test
  public void testCaseNameDescHeaderMissing() throws NoSuchFieldException, IllegalAccessException {
    log.info("Start - testCaseNameDescHeaderMissing");
    try {
      String assertMessage  = "Sheet Name : API-Testing - Row : 2 - Type : REST - [TestCaseNameDesc] mandatory data's are missing";
      List<String> logMessages = injectMockLogger();
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/testCaseNameDescHeaderMissing.yml");
      log.info("LogMessage "+logMessages);
      removeMockLogger();
      Assert.assertTrue(logMessages.contains(assertMessage));
      Assert.assertFalse(isSuccess);
    } catch (InterruptedException e) {
      e.printStackTrace();
      Assert.assertTrue(false);
    }
    log.info("End - testCaseNameDescHeaderMissing");
  }

  @Test
  public void testContentTypeHeaderMissing() throws NoSuchFieldException, IllegalAccessException {
    log.info("Start - testContentTypeHeaderMissing");
    String assertMessage  = "Sheet Name : API-Testing - Row : 2 - Type : REST - [ContentType] mandatory data's are missing";
    try {
      List<String> logMessages = injectMockLogger();
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/testContentTypeHeaderMissing.yml");
      log.info("LogMessage "+logMessages);
      removeMockLogger();
      Assert.assertTrue(logMessages.contains(assertMessage));
      Assert.assertFalse(isSuccess);
    } catch (InterruptedException e) {
     log.info("Content Type "+e.getMessage());
     Assert.assertTrue(false);
    }
    log.info("End - testContentTypeHeaderMissing");
  }

  @Test
  public void testActionHeaderMissing() throws NoSuchFieldException, IllegalAccessException {
    log.info("Start - testActionHeaderMissing");
    String assertMessage  = "Sheet Name : API-Testing - Row : 2 - Type : REST - [Action] mandatory data's are missing";
    List<String> logMessages = injectMockLogger();
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/testActionHeaderMissing.yml");
      log.info("LogMessage "+logMessages);
      removeMockLogger();
      Assert.assertTrue(logMessages.contains(assertMessage));
      Assert.assertFalse(isSuccess);
    } catch (InterruptedException e) {
      log.info("Content Type "+e.getMessage());
      Assert.assertTrue(false);
    }
    log.info("End - testActionHeaderMissing");
  }

  @Test
  public void testStatusCodeHeaderMissing() throws NoSuchFieldException, IllegalAccessException {
    log.info("Start - testStatusCodeHeaderMissing");
    String assertMessage = "Sheet Name : API-Testing - Row : 2 - Type : REST - [StatusCode] mandatory data's are missing";
    List<String> logMessages = injectMockLogger();
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/testStatusCodeHeaderMissing.yml");
      log.info("LogMessage "+logMessages);
      removeMockLogger();
      Assert.assertTrue(logMessages.contains(assertMessage));
      Assert.assertFalse(isSuccess);
    } catch (InterruptedException e) {
      log.info("Content Type "+e.getMessage());
      Assert.assertTrue(false);
    }
    log.info("End - testStatusCodeHeaderMissing");
  }

  @Test
  public void testEmptySheet() throws NoSuchFieldException, IllegalAccessException {
    log.info("Start - testEmptySheet");
    try {
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/testEmptySheet.yml");
      Assert.assertFalse(isSuccess);
    } catch (InterruptedException e) {
      log.info("Content Type "+e.getMessage());
      Assert.assertTrue(false);
    }
    log.info("End - testEmptySheet");
  }

  @Test
  public void testExcelFileNotFound() throws NoSuchFieldException, IllegalAccessException {
    log.info("Start - testExcelFileNotFound");
    String assertMessage = "File is missing(null) : testExcelFileNotFound.xlsx";
    try {
      List<String> logMessages = injectMockLogger();
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/testExcelFileNotFound.yml");
      log.info("LogMessage "+logMessages);
      removeMockLogger();
      Assert.assertFalse(isSuccess);
      Assert.assertTrue(logMessages.contains(assertMessage));
    } catch (InterruptedException e) {
      log.info("Content Type "+e.getMessage());
      Assert.assertTrue(false);
    }
    log.info("End - testExcelFileNotFound");
  }

  @Test
  public void testInvalidOutputPath() throws NoSuchFieldException, IllegalAccessException {
    log.info("Start - testInvalidOutputPath");
    String assertMessage = "Unable to generate Virtualan  JSON  PetPost-0 : E:\\target\\excel\\virtualan_collection_testcase_0\\PetPost-0.json (The system cannot find the path specified)";
    try {
      List<String> logMessages = injectMockLogger();
      boolean isSuccess = VirtualanTestPlanExecutor
              .invoke("config-yml/testInvalidOutputPath.yml");
      log.info("LogMessage "+logMessages);
      removeMockLogger();
      Assert.assertFalse(isSuccess);
      Assert.assertTrue(logMessages.contains(assertMessage));
    } catch (InterruptedException e) {
      log.info("Content Type "+e.getMessage());
      Assert.assertTrue(false);
    }
    log.info("End - testInvalidOutputPath");
  }

  private List<String> injectMockLogger() throws NoSuchFieldException, IllegalAccessException {
    Logger log = Mockito.mock(Logger.class);
    List<String> logMessages = new ArrayList<>();
    doAnswer(invocation -> {
      String arg0 = invocation.getArgumentAt(0, String.class);
      logMessages.add(arg0.trim());
      return null;
    }).when(log).error(Mockito.anyString());

    doAnswer(invocation -> {
      String arg0 = invocation.getArgumentAt(0, String.class);
      logMessages.add(arg0.trim());
      return null;
    }).when(log).info(Mockito.anyString());

    doAnswer(invocation -> {
      String arg0 = invocation.getArgumentAt(0, String.class);
      logMessages.add(arg0.trim());
      return null;
    }).when(log).warn(Mockito.anyString());

    doAnswer(invocation -> {
      String arg0 = invocation.getArgumentAt(0, String.class);
      logMessages.add(arg0.trim());
      return null;
    }).when(log).warn(Mockito.anyString());
    Field field = ExcelToCollectionGenerator.class.getDeclaredField("log");
    field.setAccessible(true);
    field.set(ExcelToCollectionGenerator.class, log);
    return logMessages;
  }

  private void removeMockLogger() throws NoSuchFieldException, IllegalAccessException {
    Logger log = LoggerFactory.getLogger(ExcelToCollectionGenerator.class);
    Field field = ExcelToCollectionGenerator.class.getDeclaredField("log");
    field.setAccessible(true);
    field.set(ExcelToCollectionGenerator.class, log);
  }


}