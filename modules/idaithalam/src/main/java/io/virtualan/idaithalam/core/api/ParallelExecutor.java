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

  private ApiExecutorParam apiExecutorPrarm;
  ParallelExecutor(ApiExecutorParam apiExecutorPrarm) {
    this.apiExecutorPrarm = apiExecutorPrarm;
  }


  @Override
  public Integer call() {
    int status = 0;
    try {
      File f = new File(apiExecutorPrarm.getOutputDir());
      if (!f.exists()) {
        f.mkdirs();
      }
      if (apiExecutorPrarm.getInputExcel() != null) {
        ExcelToCollectionGenerator
            .createCollection(apiExecutorPrarm);
      }
      buildProperties("cucumblan.properties", apiExecutorPrarm.getCucumblanProperties());
      buildProperties("cucumblan-env.properties", apiExecutorPrarm.getCucumblanEnvProperties());
      buildProperties("exclude-response.properties", apiExecutorPrarm.getExcludeProperties());

      //Generate feature and summary page html report for the selected testcase from the excel
      String title = apiExecutorPrarm.getEnv() != null ? apiExecutorPrarm.getReportTitle() + "(" + apiExecutorPrarm.getEnv() + ")" : apiExecutorPrarm.getReportTitle();
      status = IdaithalamExecutor
          .validateContract(title,
                  apiExecutorPrarm);
    } catch (Exception e) {
      log.warn(apiExecutorPrarm.getEnv() + " : " + apiExecutorPrarm.getReportTitle() + " : " + e.getMessage());
      status = 1;
    }
    log.info(apiExecutorPrarm.getEnv() + " : " + apiExecutorPrarm.getReportTitle() + " : status : " + status);
    return status;
  }
  private void buildProperties(String fileName, Map<String, String> existingProperties) throws IOException {
    if(existingProperties != null && !existingProperties.isEmpty()) {
      File file = new File(apiExecutorPrarm.getOutputDir() +File.separator+fileName);
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
        ExcelToCollectionGenerator.createPrpos(apiExecutorPrarm.getOutputDir(),
                (Map) properties,
                fileName);
      }
    }
  }
}
