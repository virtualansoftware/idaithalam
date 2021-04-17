package io.virtualan.idaithalam.core.domain;

import java.util.Map;

/**
 * The type Api executor param.
 */
public class ApiExecutorParam {

  private  String reportTitle;
  private  String env;
  private  String outputDir;
  private  String inputExcel;
  private Map<String,String> cucumblanProperies;


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
