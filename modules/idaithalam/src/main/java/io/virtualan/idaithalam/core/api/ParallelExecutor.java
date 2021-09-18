package io.virtualan.idaithalam.core.api;

import io.virtualan.idaithalam.contract.IdaithalamExecutor;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.generator.ExcelToCollectionGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParallelExecutor implements Callable<Integer> {

  private ApiExecutorParam apiExecutorParam;
  ParallelExecutor(ApiExecutorParam apiExecutorPrarm) {
    this.apiExecutorParam = apiExecutorPrarm;
  }


  @Override
  public Integer call() {
    int status = 0;
    try {
      File f = new File(apiExecutorParam.getOutputDir());
      if (!f.exists()) {
        f.mkdirs();
      }
      if ((System.getenv("IDAITHALAM") == null
            || !"PROD".equalsIgnoreCase(System.getenv("IDAITHALAM"))
              && apiExecutorParam.getInputExcel() != null)) {
          ExcelToCollectionGenerator.createCollection(apiExecutorParam);
      }
      buildProperties("cucumblan.properties", apiExecutorParam.getCucumblanProperties());
      buildProperties("cucumblan-env.properties", apiExecutorParam.getCucumblanEnvProperties());
      buildProperties("exclude-response.properties", apiExecutorParam.getExcludeProperties());

      //Generate feature and summary page html report for the selected testcase from the excel
      String title = apiExecutorParam.getEnv() != null ? apiExecutorParam.getReportTitle() + "(" + apiExecutorParam.getEnv() + ")" : apiExecutorParam.getReportTitle();
      status = IdaithalamExecutor
          .validateContract(title,
                  apiExecutorParam);
    } catch (Exception e) {
      log.warn(apiExecutorParam.getEnv() + " : " + apiExecutorParam.getReportTitle() + " : " + e.getMessage());
      status = 1;
    }
    log.info(apiExecutorParam.getEnv() + " : " + apiExecutorParam.getReportTitle() + " : status : " + status);
    return status;
  }
  private void buildProperties(String fileName, Map<String, String> existingProperties) throws IOException {
    if(existingProperties != null && !existingProperties.isEmpty()) {
      File file = new File(apiExecutorParam.getOutputDir() +File.separator+fileName);
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
        ExcelToCollectionGenerator.createPrpos(apiExecutorParam.getOutputDir(),
                (Map) properties,
                fileName);
      }
    }
  }
}
