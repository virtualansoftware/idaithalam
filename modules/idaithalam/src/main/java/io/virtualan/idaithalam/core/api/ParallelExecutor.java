package io.virtualan.idaithalam.core.api;

import io.virtualan.idaithalam.contract.IdaithalamExecutor;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.generator.ExcelToCollectionGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
  private Map<String, String> cucumblanProperties;
  private Map<String, String> cucumblanEnvProperties;
  private Map<String, String> excludeProperties;
  private List<String> generatedTestList;

  ParallelExecutor(ApiExecutorParam apiExecutorPrarm) {
    this.outputDir = apiExecutorPrarm.getOutputDir();
    this.inputExcel = apiExecutorPrarm.getInputExcel();
    this.basePath = apiExecutorPrarm.getBasePath();
    this.env = apiExecutorPrarm.getEnv();
    this.reportTitle = apiExecutorPrarm.getReportTitle();
    this.cucumblanProperties = apiExecutorPrarm.getCucumblanProperties();
    this.cucumblanEnvProperties = apiExecutorPrarm.getCucumblanEnvProperties();
    this.excludeProperties = apiExecutorPrarm.getExcludeProperties();
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
      buildProperties("cucumblan.properties", cucumblanProperties);
      buildProperties("cucumblan-env.properties", cucumblanEnvProperties);
      buildProperties("exclude-response.properties", excludeProperties);

      //Generate feature and summary page html report for the selected testcase from the excel
      String title = env != null ? reportTitle + "(" + env + ")" : reportTitle;
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
  private void buildProperties(String fileName, Map<String, String> existingProperties) throws IOException {
    if(existingProperties != null && !existingProperties.isEmpty()) {
      File file = new File(outputDir +File.separator+fileName);
      boolean isFileCreated = file.exists();
      if(!isFileCreated) {
        isFileCreated =file.createNewFile();
      }
      if(isFileCreated) {
        Properties properties = new Properties();
        try (InputStream stream = new FileInputStream(file)) {
          properties.load(stream);
        }
        existingProperties.entrySet().stream().forEach(
                x -> {
                  properties.setProperty(x.getKey(), x.getValue());
                }
        );
        ExcelToCollectionGenerator.createPrpos(outputDir,
                (Map) properties,
                fileName);
      }
    }
  }
}
