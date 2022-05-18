package io.virtualan.idaithalam.core.domain;

import java.util.List;
import java.util.Map;

/**
 * The type Api executor param.
 */
public class ApiExecutorParam {


    /**
     * The Generated test list.
     */
    List<String> generatedTestList;
    private String reportTitle;
    private String env;
    private String basePath;
    private String outputDir;
    private String outputJsonDir;
    private String virtualanSpecPath;
    private String inputFile;
    private String inputExcel;
    private Map<String, String> cucumblanProperties;
    private Map<String, String> cucumblanEnvProperties;
    private Map<String, String> excludeProperties;
    private ApiHeader apiHeader;
    private Execution execution = Execution.ALL;
    private Map<String, Map<String, String>> producers;
    private Map<String, Map<String, String>> consumers;
    private Map<String, String> topicProperties;

    /**
     * Instantiates a new Api executor param.
     */
    public ApiExecutorParam() {
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public ApiHeader getApiHeader() {
        return apiHeader;
    }

    public void setApiHeader(ApiHeader apiHeader) {
        this.apiHeader = apiHeader;
    }

    /**
     * Gets exclude properies.
     *
     * @return the exclude properies
     */
    public Map<String, String> getExcludeProperties() {
        return excludeProperties;
    }

    /**
     * Sets exclude properies.
     *
     * @param excludeProperties the exclude properies
     */
    public void setExcludeProperties(Map<String, String> excludeProperties) {
        this.excludeProperties = excludeProperties;
    }

    public Map<String, String> getTopicProperties() {
        return topicProperties;
    }

    public void setTopicProperties(Map<String, String> topicProperties) {
        this.topicProperties = topicProperties;
    }

    public String getVirtualanSpecPath() {
        return virtualanSpecPath;
    }

    public void setVirtualanSpecPath(String virtualanSpecPath) {
        this.virtualanSpecPath = virtualanSpecPath;
    }

    public String getOutputJsonDir() {
        return outputJsonDir;
    }

    public void setOutputJsonDir(String outputJsonDir) {
        this.outputJsonDir = outputJsonDir;
    }

    /**
     * Gets cucumblan env properies.
     *
     * @return the cucumblan env properies
     */
    public Map<String, String> getCucumblanEnvProperties() {
        return cucumblanEnvProperties;
    }

    /**
     * Sets cucumblan env properies.
     *
     * @param cucumblanEnvProperties the cucumblan env properies
     */
    public void setCucumblanEnvProperties(
            Map<String, String> cucumblanEnvProperties) {
        this.cucumblanEnvProperties = cucumblanEnvProperties;
    }

    /**
     * Gets base path.
     *
     * @return the base path
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * Sets base path.
     *
     * @param basePath the base path
     */
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    /**
     * Gets input file.
     *
     * @return the input file
     */
    public String getInputFile() {
        return inputFile;
    }

    /**
     * Sets input file.
     *
     * @param inputFile the input file
     */
    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Gets generated test list.
     *
     * @return the generated test list
     */
    public List<String> getGeneratedTestList() {
        return generatedTestList;
    }

    /**
     * Sets generated test list.
     *
     * @param generatedTestList the generated test list
     */
    public void setGeneratedTestList(List<String> generatedTestList) {
        this.generatedTestList = generatedTestList;
    }

    /**
     * Gets cucumblan props.
     *
     * @return the cucumblan props
     */
    public Map<String, String> getCucumblanProperties() {
        return cucumblanProperties;
    }

    /**
     * Sets cucumblan props.
     *
     * @param cucumblanProperties the cucumblan props
     */
    public void setCucumblanProperties(Map<String, String> cucumblanProperties) {
        this.cucumblanProperties = cucumblanProperties;
    }

    /**
     * Gets report title.
     *
     * @return the report title
     */
    public String getReportTitle() {
        return reportTitle;
    }

    /**
     * Sets report title.
     *
     * @param reportTitle the report title
     */
    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    /**
     * Gets env.
     *
     * @return the env
     */
    public String getEnv() {
        return env;
    }

    /**
     * Sets env.
     *
     * @param env the env
     */
    public void setEnv(String env) {
        this.env = env;
    }

    /**
     * Gets output dir.
     *
     * @return the output dir
     */
    public String getOutputDir() {
        return outputDir;
    }

    /**
     * Sets output dir.
     *
     * @param outputDir the output dir
     */
    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    /**
     * Gets input excel.
     *
     * @return the input excel
     */
    public String getInputExcel() {
        return inputExcel;
    }

    /**
     * Sets input excel.
     *
     * @param inputExcel the input excel
     */
    public void setInputExcel(String inputExcel) {
        this.inputExcel = inputExcel;
    }


    public Map<String, Map<String, String>> getProducers() {
        return producers;
    }

    public void setProducers(Map<String, Map<String, String>> producers) {
        this.producers = producers;
    }

    public Map<String, Map<String, String>> getConsumers() {
        return consumers;
    }

    public void setConsumers(Map<String, Map<String, String>> consumers) {
        this.consumers = consumers;
    }

    @Override
    public String toString() {
        return "ApiExecutorParam{" +
                "reportTitle='" + reportTitle + '\'' +
                ", env='" + env + '\'' +
                ", outputDir='" + outputDir + '\'' +
                ", inputExcel='" + inputExcel + '\'' +
                '}';
    }
}
