package io.virtualan.idaithalam.core.api;

import io.virtualan.idaithalam.contract.IdaithalamExecutor;
import io.virtualan.idaithalam.core.UnableToProcessException;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.domain.Execution;
import io.virtualan.idaithalam.core.generator.ExcelToCollectionGenerator;
import io.virtualan.idaithalam.exception.IdaithalamException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

@Slf4j
public class ParallelExecutor implements Callable<Integer> {

    private ApiExecutorParam apiExecutorParam;

    ParallelExecutor(ApiExecutorParam apiExecutorPrarm) {
        this.apiExecutorParam = apiExecutorPrarm;
    }


    @Override
    public Integer call() throws IdaithalamException, IOException, UnableToProcessException {
        boolean flag = false;
        int status = 0;
        if ((System.getenv("IDAITHALAM_EXECUTION_ENV") == null || apiExecutorParam.getEnv() == null) ||
                (System.getenv("IDAITHALAM_EXECUTION_ENV") != null &&
                        System.getenv("IDAITHALAM_EXECUTION_ENV").equalsIgnoreCase(apiExecutorParam.getEnv()))) {
            try {
                File f = new File(apiExecutorParam.getOutputDir());
                if (!f.exists()) {
                    f.mkdirs();
                }
                if (((System.getenv("IDAITHALAM") == null
                        || !"PROD".equalsIgnoreCase(System.getenv("IDAITHALAM")))
                        && apiExecutorParam.getInputExcel() != null)
                        && !Execution.EXECUTE.name().equalsIgnoreCase(apiExecutorParam.getExecution().name())) {
                    ExcelToCollectionGenerator.createCollection(apiExecutorParam);
                }
                buildProperties("cucumblan.properties", apiExecutorParam.getCucumblanProperties());
                buildProperties("cucumblan-env.properties", apiExecutorParam.getCucumblanEnvProperties());
                buildProperties("exclude-response.properties", apiExecutorParam.getExcludeProperties());

                //Generate feature and summary page html report for the selected testcase from the excel
                String title = apiExecutorParam.getEnv() != null ? apiExecutorParam.getReportTitle() + "("
                        + apiExecutorParam.getEnv() + ")" : apiExecutorParam.getReportTitle();
                status = IdaithalamExecutor
                        .validateContract(title,
                                apiExecutorParam);

            } catch (UnableToProcessException | IOException e) {
                log.warn(apiExecutorParam.getEnv() + " : " + apiExecutorParam.getReportTitle() + " : " + e
                        .getMessage());
                throw e;
            }

            log.info(
                    apiExecutorParam.getEnv() + " : " + apiExecutorParam.getReportTitle() + " : status : "
                            + status);
        }
        return status;
    }

    private void buildProperties(String fileName, Map<String, String> existingProperties)
            throws IOException, UnableToProcessException {
        if (existingProperties != null && !existingProperties.isEmpty()) {
            File file = new File(apiExecutorParam.getOutputDir() + File.separator + fileName);
            boolean isFileCreated = file.exists();
            if (!isFileCreated) {
                isFileCreated = file.createNewFile();
            }
            if (isFileCreated) {
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
