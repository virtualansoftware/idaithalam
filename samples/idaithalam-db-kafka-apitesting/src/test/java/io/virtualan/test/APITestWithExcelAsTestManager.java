package io.virtualan.test;


import io.virtualan.idaithalam.core.api.MassApiExecutor;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
public class APITestWithExcelAsTestManager {

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
  public static void execute_workflow() {
    try {
      boolean isSuccess = MassApiExecutor.invoke("work-flow.yaml");
      if (!isSuccess) {
        Assert.assertTrue(false);
      }

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

//
//  @Test
//  public void executeApiTests_Kafka_9() {
//    int status =0 ;
//    try {
//
//      ApplicationConfiguration.setProperty("pet.cucumblan.jdbc.driver-class-name", "org.hsqldb.jdbc.JDBCDriver");
//      ApplicationConfiguration.setProperty("pet.cucumblan.jdbc.username","SA");
//      ApplicationConfiguration.setProperty("pet.cucumblan.jdbc.password", "");
//      ApplicationConfiguration.setProperty("pet.cucumblan.jdbc.url","jdbc:hsqldb:file:c:\\db\\cucumblan;hsqldb.lock_file=false");
//
//      //pass the spreadsheet that you want to pass to the user
//      IdaithalamConfiguration.setProperty("workflow","Disabled");
//      List<String> list = new ArrayList<>();
//      //Add the testcaseName that List of testcases to be executed from the excel
//      //for the test selected execution
//
//      int testPlanIndex = 9;
//      File f  = new File(System.getProperty("user.dir") +"/target/"+testPlanIndex);
//      if(!f.exists())
//        f.mkdir();
//      ExcelToCollectionGenerator.createCollection(null, "virtualan_collection_kafka_db_testcase_0.xlsx",
//          System.getProperty("user.dir") +"/target/"+testPlanIndex);
//      //Generate feature and summary page html report for the selected testcase from the excel
//      status = IdaithalamExecutor.validateContract("Scriptlet testcase execution version 5", System.getProperty("user.dir") +"/target/"+testPlanIndex);
//      System.out.println(status);
//      if(status != 0) {
//        Assert.assertTrue(false);
//      }
//      Assert.assertTrue   (true);
//    }catch (Exception e){
//      System.out.println(e.getMessage());
//      Assert.assertTrue(false);
//    }
//  }

}