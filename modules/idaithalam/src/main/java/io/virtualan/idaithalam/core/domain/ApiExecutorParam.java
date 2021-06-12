package io.virtualan.idaithalam.core.domain;

import java.util.List;
import java.util.Map;

/**
 * The type Api executor param.
 */
public class ApiExecutorParam {

  private  String reportTitle;
  private  String env;
  private  String basePath;
  private  String outputDir;
  private  String inputFile;
  private  String inputExcel;
  private Map<String,String> cucumblanProperies;


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
   * The Generated test list.
   */
  List<String> generatedTestList;


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
   * Instantiates a new Api executor param.
   */
  public ApiExecutorParam() {
  }

  /**
   * Gets cucumblan props.
   *
   * @return the cucumblan props
   */
  public Map<String, String> getCucumblanProperies() {
    return cucumblanProperies;
  }

  /**
   * Sets cucumblan props.
   *
   * @param cucumblanProperies the cucumblan props
   */
  public void setCucumblanProperies(Map<String, String> cucumblanProperies) {
    this.cucumblanProperies = cucumblanProperies;
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
   * Gets env.
   *
   * @return the env
   */
  public String getEnv() {
    return env;
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
   * Gets input excel.
   *
   * @return the input excel
   */
  public String getInputExcel() {
    return inputExcel;
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
   * Sets env.
   *
   * @param env the env
   */
  public void setEnv(String env) {
    this.env = env;
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
   * Sets input excel.
   *
   * @param inputExcel the input excel
   */
  public void setInputExcel(String inputExcel) {
    this.inputExcel = inputExcel;
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
