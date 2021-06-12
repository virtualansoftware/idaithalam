package io.virtualan.idaithalam.core.api;

import io.virtualan.idaithalam.contract.IdaithalamExecutor;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.generator.ExcelToCollectionGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParallelExecutor implements Callable<Integer> {

  private String outputDir;
  private String inputExcel;
  private String env;
  private String reportTitle;
  private String basePath;
  private Map<String, String> cucumblanProperies;
  private List<String> generatedTestList;

  ParallelExecutor(ApiExecutorParam apiExecutorPrarm) {
    this.outputDir = apiExecutorPrarm.getOutputDir();
    this.inputExcel = apiExecutorPrarm.getInputExcel();
    this.basePath = apiExecutorPrarm.getBasePath();
    this.env = apiExecutorPrarm.getEnv();
    this.reportTitle = apiExecutorPrarm.getReportTitle();
    this.cucumblanProperies = apiExecutorPrarm.getCucumblanProperies();
    this.generatedTestList = apiExecutorPrarm.getGeneratedTestList();
  }


  @Override
  public Integer call() {
    int status = 0;
    try {
      File f = new File(outputDir);
      if (!f.exists()) {
        f.mkdirs();
      }
      if (inputExcel != null) {
        ExcelToCollectionGenerator
            .createCollection(basePath, generatedTestList, inputExcel, outputDir);
      }
      if (cucumblanProperies != null && !cucumblanProperies.isEmpty()) {
        File file = new File(outputDir + File.separator + "cucumblan.properties");
        if (!file.exists()) {
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
            (Map) properties,
            "cucumblan.properties");
      }
      //Generate feature and summary page html report for the selected testcase from the excel
      String title = env != null ? env + " : " + reportTitle : reportTitle;
      status = IdaithalamExecutor
          .validateContract(title,
              outputDir);
    } catch (Exception e) {
      log.warn(env + " : " + reportTitle + " : " + e.getMessage());
      status = 1;
    }
    log.info(env + " : " + reportTitle + " : status : " + status);
    return status;
  }
}
