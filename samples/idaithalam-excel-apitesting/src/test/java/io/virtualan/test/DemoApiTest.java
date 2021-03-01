package io.virtualan.test;

import io.virtualan.cucumblan.props.ApplicationConfiguration;
import io.virtualan.idaithalam.contract.IdaithalamExecutor;
import io.virtualan.idaithalam.core.contract.validator.ExcelToCollectionGenerator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;;
import org.junit.Assert;;
import org.junit.Test;
public class DemoApiTest {
    @Test
    public void validateContract()  {
        int status =0 ;
        try {
            int count =0;
            List<String> list = new ArrayList<>();
            //Add the testcaseName that List of testcases to be executed from the excel
            //for the test selected execution
            list.add("PetPost");
            list.add("PetGet");  // uncomment and test again see the summary report
            count++;
            File f  = new File(System.getProperty("user.dir") +"/target/"+count);
            if(!f.exists())
               f.mkdir();
            //pass the spreadsheet that you want to pass to the user
            ExcelToCollectionGenerator.createCollection(list, "virtualan_collection_pet.xlsx", System.getProperty("user.dir") +"/target/"+count);

            //Generate feature and summary page html report for the selected testcase from the excel
            status = IdaithalamExecutor.validateContract("Pet  1 API EXCEL based api testing", System.getProperty("user.dir") +"/target/"+count);
            System.out.println(status);
            list.clear();
            count++;
            f  = new File(System.getProperty("user.dir") +"/target/"+count);
            if(!f.exists())
                f.mkdir();
            list.add("PetGet");  // uncomment and test again see the summary report
            //pass the spreadsheet that you want to pass to the user
            ExcelToCollectionGenerator.createCollection(list, "virtualan_collection_pet.xlsx", System.getProperty("user.dir") +"/target/"+count);

            //Generate feature and summary page html report for the selected testcase from the excel
            status = IdaithalamExecutor.validateContract("Pet 2 API EXCEL based api testing", System.getProperty("user.dir") +"/target/"+count);
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