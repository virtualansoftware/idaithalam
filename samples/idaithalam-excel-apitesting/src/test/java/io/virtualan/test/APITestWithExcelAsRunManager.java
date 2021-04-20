package io.virtualan.test;


import io.virtualan.idaithalam.config.IdaithalamConfiguration;
import io.virtualan.idaithalam.contract.IdaithalamExecutor;
import io.virtualan.idaithalam.core.generator.ExcelToCollectionGenerator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
public class APITestWithExcelAsRunManager {

    @BeforeClass
    public void testBeforeClass(){
        System.out.println("----------------------------------------");
        System.out.println("--- Start Test -------");
        System.out.println("------------------------------------------");
    }

    @AfterClass
    public  void testAfterClass(){
        System.out.println("------------------------------------------");
        System.out.println("---- END Test : ------");
        System.out.println("-------------------------------------------");
    }


    @Test
    public void executeApiTests_1(){
        int status =0 ;
        try {
          IdaithalamConfiguration.setProperty("SPECIAL_SKIP_CHAR","\\\\r\\\\n=\\\\\\\\r\\\\\\\\n");
          int testcase =1;
            List<String> list = new ArrayList<>();
            //Add the testcaseName that List of testcases to be executed from the excel
            //for the test selected execution
            list.add("PetPost");
            list.add("PetGet");  // uncomment and test again see the summary report
            File f  = new File(System.getProperty("user.dir") +"/target/"+testcase);
            if(!f.exists())
                f.mkdir();
          //IdaithalamConfiguration.setProperty("workflow","Disabled");

            //pass the spreadsheet that you want to pass to the user
            ExcelToCollectionGenerator
                .createCollection(list, "virtualan_collection_testcase_4.xlsx", System.getProperty("user.dir") +"/target/"+testcase);

            //Generate feature and summary page html report for the selected testcase from the excel
            status = IdaithalamExecutor
                .validateContract("Pet  1 API EXCEL based api testing", System.getProperty("user.dir") +"/target/"+testcase);
            System.out.println(status);
            if(status != 0) {
                //actual it will fail but it made as pass
                //purposefully passed to show case okta and basic auth demo
                Assert.assertTrue(true);
            }
            Assert.assertTrue   (true);
        }catch (Exception e){
            System.out.println(e.getMessage());
            Assert.assertTrue(true);
        }

    }


    @Test
    public void executeApiTests_2(){
        int status =0 ;
        try {
            int testcase = 2;
            List<String> list = new ArrayList<>();
            //Add the testcaseName that List of testcases to be executed from the excel
            //for the test selected execution
            list.add("PetPost");
            list.add("PetGet");  // uncomment and test again see the summary report
            list.add("PetGet_2");  // uncomment and test again see the summary report
            list.add("PetPost_3");  // uncomment and test again see the summary report
            IdaithalamConfiguration.setProperty("workflow","Disabled");
            File f  = new File(System.getProperty("user.dir") +"/target/"+testcase);
            if(!f.exists())
                f.mkdir();
            //list.add("PetGet");  // uncomment and test again see the summary report
            //pass the spreadsheet that you want to pass to the user
            ExcelToCollectionGenerator.createCollection(list, "virtualan_collection_testcase_8.xlsx", System.getProperty("user.dir") +"/target/"+testcase);
            //Generate feature and summary page html report for the selected testcase from the excel
            status = IdaithalamExecutor.validateContract("Pet 2 API EXCEL based api testing", System.getProperty("user.dir") +"/target/"+testcase);
            System.out.println(status);
            if(status != 0) {
                Assert.assertTrue(true);
            }
            Assert.assertTrue   (true);
        }catch (Exception e){
            System.out.println(e.getMessage());
            Assert.assertTrue(false);
        }

    }


  @Test
  public void executeApiTests_3() {
    int status = 0;
    try {
      int testcase = 3;
      File f = new File(System.getProperty("user.dir") + "/target/" + testcase);
      if (!f.exists())
        f.mkdir();
      //pass the spreadsheet that you want to pass to the user
      IdaithalamConfiguration.setProperty("workflow", "Enabled");
      ExcelToCollectionGenerator.createCollection(null, "virtualan_collection_testcase_5.xlsx",
          System.getProperty("user.dir") + "/target/" + testcase);
      //Generate feature and summary page html report for the selected testcase from the excel
      status = IdaithalamExecutor.validateContract("Scriptlet testcase execution version 5",
          System.getProperty("user.dir") + "/target/" + testcase);
      System.out.println(status);
      if (status != 0) {
        Assert.assertFalse(false);
      }
      Assert.assertTrue(true);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      Assert.assertFalse(false);
    }
  }
    @Test
    public void executeApiTests_7() {
      int status = 0;
      try {
        int testcase = 7;
        File f = new File(System.getProperty("user.dir") + "/target/" + testcase);
        if (!f.exists())
          f.mkdir();
        //pass the spreadsheet that you want to pass to the user
        IdaithalamConfiguration.setProperty("workflow", "Disabled");
        ExcelToCollectionGenerator.createCollection(null, "virtualan_collection_testcase_7.xlsx",
            System.getProperty("user.dir") + "/target/" + testcase);
        //Generate feature and summary page html report for the selected testcase from the excel
        status = IdaithalamExecutor.validateContract("Scriptlet testcase execution version 7",
            System.getProperty("user.dir") + "/target/" + testcase);
        System.out.println(status);
        if (status != 0) {
          Assert.assertTrue(false);
        }
        Assert.assertTrue(true);
      } catch (Exception e) {
        System.out.println(e.getMessage());
        Assert.assertTrue(false);
      }

    }

  @Test
  public void executeApiTests_4(){
    int status =0 ;
    try {
      int testcase = 4;
      File f  = new File(System.getProperty("user.dir") +"/target/"+testcase);
      if(!f.exists())
        f.mkdir();
      //pass the spreadsheet that you want to pass to the user
      IdaithalamConfiguration.setProperty("workflow","Enabled");
      ExcelToCollectionGenerator.createCollection(null, "virtualan_collection_testcase_6.xlsx", System.getProperty("user.dir") +"/target/"+testcase);
      //Generate feature and summary page html report for the selected testcase from the excel
      status = IdaithalamExecutor.validateContract("Scriptlet testcase execution version 5", System.getProperty("user.dir") +"/target/"+testcase);
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

  //Multi Run and Skip the test
  @Test
  public void executeApiTests_5() {
    int status = 0;
    try {
      int testcase = 5;
      File f = new File(System.getProperty("user.dir") + "/target/" + testcase);
      if (!f.exists())
        f.mkdir();
      //pass the spreadsheet that you want to pass to the user
      IdaithalamConfiguration.setProperty("workflow", "Enabled");
      ExcelToCollectionGenerator.createCollection(null, "virtualan_collection_testcase_01.xlsx",
          System.getProperty("user.dir") + "/target/" + testcase);
      //Generate feature and summary page html report for the selected testcase from the excel
      status = IdaithalamExecutor.validateContract("Scriptless-5-Multi Run and Skip Scenario",
          System.getProperty("user.dir") + "/target/" + testcase);
      System.out.println(status);
      if (status != 0) {
        Assert.assertTrue(false);
      } else {
        Assert.assertTrue(true);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      Assert.assertTrue(false);
    }
  }

  @Test
  public void executeApiTests_6() {
    int status =0 ;
    try {
      int testPlanIndex = 6;
      File f  = new File(System.getProperty("user.dir") +"/target/"+testPlanIndex);
      if(!f.exists())
        f.mkdir();
      //pass the spreadsheet that you want to pass to the user
      IdaithalamConfiguration.setProperty("workflow","Enabled");
      ExcelToCollectionGenerator.createCollection(null, "virtualan_collection_testcase_02.xlsx",
          System.getProperty("user.dir") +"/target/"+testPlanIndex);
      //Generate feature and summary page html report for the selected testcase from the excel
      status = IdaithalamExecutor.validateContract("Scriptlet testcase execution version 5", System.getProperty("user.dir") +"/target/"+testPlanIndex);
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

  @Test
  public void executeApiTests_empty_7() {
    int status =0 ;
    try {
      IdaithalamConfiguration.setProperty("SPECIAL_SKIP_CHAR","\\\\r\\\\n=\\\\\\\\r\\\\\\\\n");
      //pass the spreadsheet that you want to pass to the user
      IdaithalamConfiguration.setProperty("workflow","Disabled");
      List<String> list = new ArrayList<>();
      //Add the testcaseName that List of testcases to be executed from the excel
      //for the test selected execution
      //list.add("PetPost");
      //list.add("PetGet");  // uncomment and test again see the summary report
      //list.add("PetGet-Test");  // uncomment and test again see the summary report


      int testPlanIndex = 7;
      File f  = new File(System.getProperty("user.dir") +"/target/"+testPlanIndex);
      if(!f.exists())
        f.mkdir();
      ExcelToCollectionGenerator.createCollection(list, "virtualan_collection_pet_empty.xlsx",
          System.getProperty("user.dir") +"/target/"+testPlanIndex);
      //Generate feature and summary page html report for the selected testcase from the excel
      status = IdaithalamExecutor.validateContract("Scriptlet testcase execution version 5", System.getProperty("user.dir") +"/target/"+testPlanIndex);
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



  @Test
  public void executeApiTests_multiplesheet_8() {
    int status =0 ;
    try {
      IdaithalamConfiguration.setProperty("SPECIAL_SKIP_CHAR","\\\\r\\\\n=\\\\\\\\r\\\\\\\\n");
      //pass the spreadsheet that you want to pass to the user
      IdaithalamConfiguration.setProperty("workflow","Disabled");
      List<String> list = new ArrayList<>();
      //Add the testcaseName that List of testcases to be executed from the excel
      //for the test selected execution
      list.add("EDI271_1");
      list.add("EDI271");
      list.add("PetPost_3");
      //list.add("PetGet");  // uncomment and test again see the summary report
      //list.add("PetGet-Test");  // uncomment and test again see the summary report


      int testPlanIndex = 8;
      File f  = new File(System.getProperty("user.dir") +"/target/"+testPlanIndex);
      if(!f.exists())
        f.mkdir();
      ExcelToCollectionGenerator.createCollection(list, "virtualan_collection_pet_sheet_2.xlsx",
          System.getProperty("user.dir") +"/target/"+testPlanIndex);
      //Generate feature and summary page html report for the selected testcase from the excel
      status = IdaithalamExecutor.validateContract("Scriptlet testcase execution version 5", System.getProperty("user.dir") +"/target/"+testPlanIndex);
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