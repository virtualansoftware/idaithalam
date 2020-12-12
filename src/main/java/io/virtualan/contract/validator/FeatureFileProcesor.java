package io.virtualan.contract.validator;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import io.cucumber.core.cli.Main;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.presentation.PresentationMode;
import org.apache.http.annotation.Contract;


class FeatureFileProcessor {

  List<Item> items = FeatureFileGenerator.generateFeatureFile();

  static class Item {
    String scenario;
    String httpStatusCode;
    String resource;
    String method;
    String action;
    String input;
    String output;
    Map<String, String> hasInputJsonMap;
    Map<String, String > hasOutputJsonMap;
    Set<Entry<String, String>> inputJsonMap;
    Set<Entry<String, String>> outputJsonMap;
    List<AvailableParam> availableParams;
    List<AvailableParam> pathParams;
    List<AvailableParam> queryParams;
    List<AvailableParam> headerParams;
    boolean hasPathParams;
    boolean hasQueryParams;
    boolean hasHeaderParams;
    boolean isPut;
    boolean isGet;
    boolean isPost;
    boolean isDelete;
    boolean isPatch;
    String url;

    Item(){ }



    public String getScenario() {
      return scenario;
    }

    public void setScenario(String scenario) {
      this.scenario = scenario;
    }


    public void setHasPathParams(boolean hasPathParams) {
      this.hasPathParams = hasPathParams;
    }


    public void setHasQueryParams(boolean hasQueryParams) {
      this.hasQueryParams = hasQueryParams;
    }

    public void setHasHeaderParams(boolean hasHeaderParams) {
      this.hasHeaderParams = hasHeaderParams;
    }

    public void setInputJsonMap(Map<String, String> inputJsonMap) {
      this.inputJsonMap = inputJsonMap.entrySet();
      this.hasInputJsonMap = inputJsonMap;
    }

    public void setOutputJsonMap(Map<String, String> outputJsonMap) {
      this.outputJsonMap = outputJsonMap.entrySet();
      this.hasOutputJsonMap = outputJsonMap;
    }

    public Map<String, String> getHasInputJsonMap() {
      return hasInputJsonMap;
    }

    public void setHasInputJsonMap(Map<String, String> hasInputJsonMap) {
      this.hasInputJsonMap = hasInputJsonMap;
    }

    public Map<String, String> getHasOutputJsonMap() {
      return hasOutputJsonMap;
    }

    public void setHasOutputJsonMap(Map<String, String> hasOutputJsonMap) {
      this.hasOutputJsonMap = hasOutputJsonMap;
    }

    public Set<Entry<String, String>> getInputJsonMap() {
      return inputJsonMap;
    }

    public void setInputJsonMap(
        Set<Entry<String, String>> inputJsonMap) {
      this.inputJsonMap = inputJsonMap;
    }

    public Set<Entry<String, String>> getOutputJsonMap() {
      return outputJsonMap;
    }

    public void setOutputJsonMap(
        Set<Entry<String, String>> outputJsonMap) {
      this.outputJsonMap = outputJsonMap;
    }

    public List<AvailableParam> getPathParams() {
      return pathParams;
    }

    public void setPathParams(
        List<AvailableParam> pathParams) {
      this.pathParams = pathParams;
    }

    public List<AvailableParam> getQueryParams() {
      return queryParams;
    }

    public void setQueryParams(
        List<AvailableParam> queryParams) {
      this.queryParams = queryParams;
    }

    public List<AvailableParam> getHeaderParams() {
      return headerParams;
    }

    public void setHeaderParams(
        List<AvailableParam> headerParams) {
      this.headerParams = headerParams;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getHttpStatusCode() {
      return httpStatusCode;
    }

    public void setHttpStatusCode(String httpStatusCode) {
      this.httpStatusCode = httpStatusCode;
    }

    public String getResource() {
      return resource;
    }

    public void setResource(String resource) {
      this.resource = resource;
    }

    public String getMethod() {
      return method;
    }

    public void setMethod(String method) {
      this.method = method;
      if ("POST".equalsIgnoreCase(method)) {
        isPost = true;
      } else if ("GET".equalsIgnoreCase(method)) {
        isGet = true;
      } else if ("DELETE".equalsIgnoreCase(method)) {
        isDelete = true;
      } else if ("PUT".equalsIgnoreCase(method)) {
        isPut = true;
      } else if ("PATCH".equalsIgnoreCase(method)) {
        isPatch = true;
      }
    }

    public String getInput() {
      return input;
    }

    public void setInput(String input) {
      this.input = input;
    }

    public String getOutput() {
      return output;
    }

    public void setOutput(String output) {
      this.output = output;
    }

    public List<AvailableParam> getAvailableParams() {
      return availableParams;
    }


    public void setAvailableParams(
        List<AvailableParam> availableParams) {
      this.availableParams = availableParams;
      Map<String, List<AvailableParam>>  stringListMap = availableParams.stream()
          .collect(Collectors.groupingBy(AvailableParam::getParameterType));
      if(!stringListMap.isEmpty()) {
        pathParams = stringListMap.get("PATH_PARAM");
        hasPathParams = pathParams != null ? !pathParams.isEmpty() : false ;
        queryParams = stringListMap.get("QUERY_PARAM");
        hasQueryParams = queryParams != null ? !queryParams.isEmpty() : false ;
        headerParams = stringListMap.get("HEADER_PARAM");
        hasHeaderParams = headerParams != null ? !headerParams.isEmpty() : false ;
      }
    }

    public String getAction() {
      return action;
    }

    public void setAction(String action) {
      this.action = action;
    }
  }


  static class AvailableParam {
    AvailableParam(String  key, String value, String parameterType) {
      this.key = key;
      this.value = value;
      this.parameterType = parameterType;
    }

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

    public String getParameterType() {
      return parameterType;
    }

    public void setParameterType(String parameterType) {
      this.parameterType = parameterType;
    }

    String key;
    String value;
    String parameterType;
  }

  static String feature = "Idithalan";



  public static void main(String[] args) throws Exception {
    if(args.length > 0) {
      feature =  args[0];
    }
    generateFeatureFile(feature);
    addConfToClasspath();
    String[] argv = getCucumberOptions();
    byte exitStatus = Main.run(argv, Thread.currentThread().getContextClassLoader());
    generateReport();
    System.exit(exitStatus);
  }

  private static void generateReport() {
    File reportOutputDirectory = new File("target");
    List<String> jsonFiles = new ArrayList<>();
    jsonFiles.add("target/cucumber.json");
    String buildNumber = "1";
    String projectName = feature+" - API Contract Testing";

    Configuration configuration = new Configuration(reportOutputDirectory, projectName);
// optional configuration - check javadoc for details
    configuration.addPresentationModes(PresentationMode.RUN_WITH_JENKINS);
// do not make scenario failed when step has status SKIPPED
    configuration.setNotFailingStatuses(Collections.singleton(Status.SKIPPED));
    configuration.setBuildNumber(buildNumber);
// addidtional metadata presented on main page
    configuration.addClassifications("Platform", "Windows");
    configuration.addClassifications("Browser", "Firefox");
    configuration.addClassifications("Branch", "release/1.0");
// optionally specify qualifiers for each of the report json files
    configuration.addPresentationModes(PresentationMode.PARALLEL_TESTING);
    configuration.setQualifier("cucumber-report-1", "First report");
    configuration.setQualifier("cucumber-report-2", "Second report");
    ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
    Reportable result = reportBuilder.generateReports();
  }

  private static String[] getCucumberOptions() {
    return new String[]{
          "-p","json:target/cucumber.json",
          "-p","html:target/cucumber-html-report.html",
          "--glue", "io.virtualan.cucumblan.core", "", "virtualan-contract.feature",
        };
  }

  private static void addConfToClasspath() throws MalformedURLException {
    ClassLoader prevCl = Thread.currentThread().getContextClassLoader();

    ClassLoader urlCl = URLClassLoader
        .newInstance(new URL[]{new File("conf").toURI().toURL()}, prevCl);
    Thread.currentThread().setContextClassLoader(urlCl);
  }

  private static void generateFeatureFile(String feature) throws IOException {
    MustacheFactory mf = new DefaultMustacheFactory();
    Mustache mustache = mf.compile("virtualan-contract.mustache");
    FileOutputStream outputStream = new FileOutputStream("virtualan-contract.feature");
    Writer writer = new OutputStreamWriter(outputStream);
    mustache.execute(writer, new FeatureFileProcessor()).flush();
    writer.close();
  }
}