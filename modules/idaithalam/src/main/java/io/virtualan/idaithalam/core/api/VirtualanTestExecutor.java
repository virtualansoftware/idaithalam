package io.virtualan.idaithalam.core.api;

import io.virtualan.idaithalam.contract.IdaithalamExecutor;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.generator.ExcelToCollectionGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VirtualanTestExecutor {

  private String outputDir;
  private String inputExcel;
  private String env;
  private String reportTitle;
  private Map<String, String> cucumblanProperies;
  List<String> generatedTestList;

  public VirtualanTestExecutor(ApiExecutorParam apiExecutorPrarm) {
    this.outputDir = apiExecutorPrarm.getOutputDir();
    this.inputExcel = apiExecutorPrarm.getInputExcel();
    this.env = apiExecutorPrarm.getEnv();
    this.reportTitle = apiExecutorPrarm.getReportTitle();
    this.cucumblanProperies = apiExecutorPrarm.getCucumblanProperies();
    this.generatedTestList = apiExecutorPrarm.getGeneratedTestList();
  }


  public Integer call() {
    int status = 0;
    try {
      File f = new File(outputDir);
      if (!f.exists()) {
        f.mkdirs();
      }

      if (inputExcel != null){
        ExcelToCollectionGenerator.createCollection(generatedTestList, inputExcel, outputDir);
      }
      if(cucumblanProperies != null && !cucumblanProperies.isEmpty()) {
        File file = new File(outputDir +File.separator+"cucumblan.properties");
        if(!file.exists()){
          file.createNewFile();
        }
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        cucumblanProperies.entrySet().stream().forEach(
            x -> {
              properties.setProperty(x.getKey(), x.getValue());
            }
        );
        ExcelToCollectionGenerator.createPrpos(outputDir,
            (Map)properties,
            "cucumblan.properties");
      }
      //Generate feature and summary page html report for the selected testcase from the excel
      status = IdaithalamExecutor
          .validateContract(env + " : " + reportTitle,
              outputDir);
    } catch (Exception e) {
      log.warn(env + " : " + reportTitle + " : " + e.getMessage());
      status = 1;
      e.printStackTrace();
    }
    log.info(env + " : " + reportTitle + " : status : " + status);
    return status;
  }
}
