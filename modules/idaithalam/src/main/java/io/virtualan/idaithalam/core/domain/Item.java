/*
 *
 *  Copyright (c) 2020.  Virtualan Contributors (https://virtualan.io)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under the License
 *   is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *   or implied. See the License for the specific language governing permissions and limitations under
 *   the License.
 *
 */

package io.virtualan.idaithalam.core.domain;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Item.
 */
public class Item {


  /**
   * The Event.
   */
  String event;

  /**
   * The Message type.
   */
  String messageType;

  /**
   * The Identifier.
   */
  String identifier;

  /**
   * The Scenario.
   */
  String contentType;
  /**
   * The Scenario.
   */
  String scenario;
  /**
   * The Http status code.
   */
  String httpStatusCode;
  /**
   * The Resource.
   */
  String resource;
  /**
   * The Resource.
   */
  String okta;
  /**
   * The Resource.
   */
  String basicAuth;
  /**
   * The Method.
   */
  String method;
  /**
   * The Action.
   */
  String action;
  /**
   * The Input.
   */
  String input;
  /**
   * The Output.
   */
  String output;
  /**
   * The Std type.
   */
  String tags;

  /**
   * The db output.
   */
  boolean dbOutput;

  /**
   * The db input.
   */
  boolean dbInput;

  /**
   * The Kafka output.
   */
  boolean kafkaOutput;

  /**
   * The Kafka input.
   */
  boolean kafkaInput;

  /**
   * The Std type.
   */
  boolean isRest;

  /**
   * The Is kafka.
   */
  boolean isKafka;

  /**
   * The Is Database.
   */
  boolean isDatabase;

  /**
   * The Std type.
   */
  String stdType;
  /**
   * The Std input.
   */
  String stdInput;
  /**
   * The Std output.
   */
  String stdOutput;

  /**
   * The Std output.
   */
  String skipScenario;

  /**
   * The Csvson.
   */
  String hasCsvson;

  /**
   * The Csvson.
   */
  String csvsonPath;

  /**
   * The Csvson.
   */
  List<String> csvson;

  /**
   * The Output file by path.
   */
  List<String> multiRun;

  /**
   * is Multi run enabled.
   */
  boolean hasMultiRun;

  /**
   * The Has input json map.
   */
  boolean hasInputJsonMap;

  /**
   * The Has output file by path.
   */
  boolean hasOutputFileByPath;

  /**
   * The Output file by path.
   */
  List<String> outputFileByPath;
  /**
   * The Response by field.
   */
  Set<Map.Entry<String, String>> responseByField;

  /**
   * The Has response by field.
   */
  boolean hasResponseByField;
  /**
   * The Has output json map.
   */
  boolean hasOutputJsonMap;
  /**
   * The Input json map.
   */
  Set<Map.Entry<String, String>> inputJsonMap;
  /**
   * The Output json map.
   */
  Set<Map.Entry<String, String>> outputJsonMap;
  /**
   * The Available params.
   */
  List<AvailableParam> availableParams;
  /**
   * The Path params.
   */
  List<AvailableParam> pathParams;

  /**
   * The Path params.
   */
  List<AvailableParam> formParams;

  /**
   * The Path params.
   */
  List<AvailableParam> createParams;

  /**
   * The Path params.
   */
  List<AvailableParam> evaluateParams;


  /**
   * The Path params.
   */
  List<AvailableParam> storageParams;

  /**
   * The Cookie params.
   */
  List<AvailableParam> cookieParams;

  /**
   * The Has cookie params.
   */
  boolean hasCookieParams;


  /**
   * The Step info.
   */
  String stepInfo;

  /**
   * Gets step info.
   *
   * @return the step info
   */
  public String getStepInfo() {
    return stepInfo;
  }

  /**
   * Sets step info.
   *
   * @param stepInfo the step info
   */
  public void setStepInfo(String stepInfo) {
    this.stepInfo = stepInfo;
  }

  /**
   * Is db output boolean.
   *
   * @return the boolean
   */
  public boolean isDbOutput() {
    return dbOutput;
  }

  /**
   * Sets db output.
   *
   * @param dbOutput the db output
   */
  public void setDbOutput(boolean dbOutput) {
    this.dbOutput = dbOutput;
  }

  /**
   * Is db input boolean.
   *
   * @return the boolean
   */
  public boolean isDbInput() {
    return dbInput;
  }

  /**
   * Sets db input.
   *
   * @param dbInput the db input
   */
  public void setDbInput(boolean dbInput) {
    this.dbInput = dbInput;
  }

  /**
   * Gets event.
   *
   * @return the event
   */
  public String getEvent() {
    return event;
  }

  /**
   * Sets event.
   *
   * @param event the event
   */
  public void setEvent(String event) {
    this.event = event;
  }

  /**
   * Gets message type.
   *
   * @return the message type
   */
  public String getMessageType() {
    return messageType;
  }

  /**
   * Sets message type.
   *
   * @param messageType the message type
   */
  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  /**
   * Gets identifier.
   *
   * @return the identifier
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * Sets identifier.
   *
   * @param identifier the identifier
   */
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  /**
   * Gets kafka output.
   *
   * @return the kafka output
   */
  public boolean isKafkaOutput() {
    return kafkaOutput;
  }

  /**
   * Sets kafka output.
   *
   * @param kafkaOutput the kafka output
   */
  public void setKafkaOutput(boolean kafkaOutput) {
    this.kafkaOutput = kafkaOutput;
  }

  /**
   * Gets kafka input.
   *
   * @return the kafka input
   */
  public boolean isKafkaInput() {
    return kafkaInput;
  }

  /**
   * Sets kafka input.
   *
   * @param kafkaInput the kafka input
   */
  public void setKafkaInput(boolean kafkaInput) {
    this.kafkaInput = kafkaInput;
  }

  /**
   * Is rest boolean.
   *
   * @return the boolean
   */
  public boolean isRest() {
    return isRest;
  }

  /**
   * Sets rest.
   *
   * @param rest the rest
   */
  public void setRest(boolean rest) {
    isRest = rest;
  }

  /**
   * Is kafka boolean.
   *
   * @return the boolean
   */
  public boolean isKafka() {
    return isKafka;
  }

  /**
   * Sets kafka.
   *
   * @param kafka the kafka
   */
  public void setKafka(boolean kafka) {
    isKafka = kafka;
  }

  /**
   * Is database boolean.
   *
   * @return the boolean
   */
  public boolean isDatabase() {
    return isDatabase;
  }

  /**
   * Sets database.
   *
   * @param database the database
   */
  public void setDatabase(boolean database) {
    isDatabase = database;
  }

  /**
   * Gets multi run.
   *
   * @return the multi run
   */
  public List<String> getMultiRun() {
    return multiRun;
  }

  /**
   * Sets multi run.
   *
   * @param multiRun the multi run
   */
  public void setMultiRun(List<String> multiRun) {
    this.multiRun = multiRun;
  }

  /**
   * gets csvsonPath run.
   *
   * get csvsonPath
   */
  public String getCsvsonPath() {
    return csvsonPath;
  }
  /**
   * Sets csvsonPath .
   *
   * @param csvsonPath 
   */
  public void setCsvsonPath(String csvsonPath) {
    this.csvsonPath = csvsonPath;
  }

  /**
   * The Query params.
   */
  List<AvailableParam> queryParams;
  /**
   * The Header params.
   */
  List<AvailableParam> headerParams;
  /**
   * The Has path params.
   */
  boolean hasPathParams;
  /**
   * The Has path params.
   */
  boolean hasFormParams;
  /**
   * The Has query params.
   */
  boolean hasQueryParams;
  /**
   * The Has header params.
   */
  boolean hasHeaderParams;
  /**
   * The Has header params.
   */
  boolean hasStorageParams;
  /**
   * The Has header params.
   */
  boolean hasCreateParams;

  /**
   * The Has evaluate params.
   */
  boolean hasEvaluateParams;

  /**
   * The Is put.
   */
  boolean isPut;
  /**
   * The Is get.
   */
  boolean isGet;
  /**
   * The Is post.
   */
  boolean isPost;
  /**
   * The Is delete.
   */
  boolean isDelete;
  /**
   * The Is patch.
   */
  boolean isPatch;
  /**
   * The Url.
   */
  String url;

  /**
   * The Output inline xml.
   */
  List<String> outputInline;

  /**
   * The Output inline xml.
   */
  String hasOutputInline;


  /**
   * The No skip output.
   */
  boolean noSkipOutput;

  /**
   * The Output file xml.
   */
  String outputFile;

  /**
   * The Input inline xml.
   */
  List<String> inputInline;

  /**
   * The Input inline xml.
   */
  String hasInputInline;

  /**
   * The Input file xml.
   */
  String inputFile;


  /**
   * Is no skip output boolean.
   *
   * @return the boolean
   */
  public boolean isNoSkipOutput() {
    return noSkipOutput;
  }

  /**
   * Gets evaluate params.
   *
   * @return the evaluate params
   */
  public List<AvailableParam> getEvaluateParams() {
    return evaluateParams;
  }

  /**
   * Sets evaluate params.
   *
   * @param evaluateParams the evaluate params
   */
  public void setEvaluateParams(
      List<AvailableParam> evaluateParams) {
    this.evaluateParams = evaluateParams;
  }

  /**
   * Is has multi run boolean.
   *
   * @return the boolean
   */
  public boolean isHasMultiRun() {
    return hasMultiRun;
  }

  /**
   * Sets has multi run.
   *
   * @param hasMultiRun the has multi run
   */
  public void setHasMultiRun(boolean hasMultiRun) {
    this.hasMultiRun = hasMultiRun;
  }

  /**
   * Gets okta.
   *
   * @return the okta
   */
  public String getOkta() {
    return okta;
  }

  /**
   * Sets okta.
   *
   * @param okta the okta
   */
  public void setOkta(String okta) {
    this.okta = okta;
  }

  /**
   * Gets basic auth.
   *
   * @return the basic auth
   */
  public String getBasicAuth() {
    return basicAuth;
  }

  /**
   * Sets basic auth.
   *
   * @param basicAuth the basic auth
   */
  public void setBasicAuth(String basicAuth) {
    this.basicAuth = basicAuth;
  }

  /**
   * Gets response by field.
   *
   * @return the response by field
   */
  public Set<Entry<String, String>> getResponseByField() {
    return responseByField;
  }

  /**
   * Sets response by field.
   *
   * @param responseByField the response by field
   */
  public void setResponseByField(
      Map<String, String> responseByField) {
    this.responseByField = responseByField.entrySet();
  }

  /**
   * Sets response by field.
   *
   * @param responseByField the response by field
   */
  public void setResponseByField(
      Set<Entry<String, String>> responseByField) {
    this.responseByField = responseByField;
  }

  /**
   * Is has response by field boolean.
   *
   * @return the boolean
   */
  public boolean isHasResponseByField() {
    return hasResponseByField;
  }

  /**
   * Sets has response by field.
   *
   * @param hasResponseByField the has response by field
   */
  public void setHasResponseByField(boolean hasResponseByField) {
    this.hasResponseByField = hasResponseByField;
  }


  /**
   * Is has output file by path boolean.
   *
   * @return the boolean
   */
  public boolean isHasOutputFileByPath() {
    return hasOutputFileByPath;
  }

  /**
   * Sets has output file by path.
   *
   * @param hasOutputFileByPath the has output file by path
   */
  public void setHasOutputFileByPath(boolean hasOutputFileByPath) {
    this.hasOutputFileByPath = hasOutputFileByPath;
  }

  /**
   * Gets output file by path.
   *
   * @return the output file by path
   */
  public List<String> getOutputFileByPath() {
    return outputFileByPath;
  }

  /**
   * Sets output file by path.
   *
   * @param outputFileByPath the output file by path
   */
  public void setOutputFileByPath(List<String> outputFileByPath) {
    this.outputFileByPath = outputFileByPath;
  }

  /**
   * Gets has output inline.
   *
   * @return the has output inline
   */
  public String getHasOutputInline() {
    return hasOutputInline;
  }

  /**
   * Sets has output inline.
   *
   * @param hasOutputInline the has output inline
   */
  public void setHasOutputInline(String hasOutputInline) {
    this.hasOutputInline = hasOutputInline;
  }


  /**
   * Gets create params.
   *
   * @return the create params
   */
  public List<AvailableParam> getCreateParams() {
    return createParams;
  }

  /**
   * Sets create params.
   *
   * @param createParams the create params
   */
  public void setCreateParams(
      List<AvailableParam> createParams) {
    this.createParams = createParams;
  }

  /**
   * Is has cookie params boolean.
   *
   * @return the boolean
   */
  public boolean isHasCookieParams() {
    return hasCookieParams;
  }

  /**
   * Sets has cookie params.
   *
   * @param hasCookieParams the has cookie params
   */
  public void setHasCookieParams(boolean hasCookieParams) {
    this.hasCookieParams = hasCookieParams;
  }

  /**
   * Gets has input inline.
   *
   * @return the has input inline
   */
  public String getHasInputInline() {
    return hasInputInline;
  }

  /**
   * Sets has input inline.
   *
   * @param hasInputInline the has input inline
   */
  public void setHasInputInline(String hasInputInline) {
    this.hasInputInline = hasInputInline;
  }


  /**
   * Gets csvson.
   *
   * @return the csvson
   */
  public List<String> getCsvson() {
    return csvson;
  }

  /**
   * Sets csvson.
   *
   * @param csvson the csvson
   */
  public void setCsvson(List<String> csvson) {
    this.csvson = csvson;
  }

  /**
   * Gets has csvson.
   *
   * @return the has csvson
   */
  public String getHasCsvson() {
    return hasCsvson;
  }

  /**
   * Sets has csvson.
   *
   * @param hasCsvson the has csvson
   */
  public void setHasCsvson(String hasCsvson) {
    this.hasCsvson = hasCsvson;
  }



  /**
   * Gets std type.
   *
   * @return the std type
   */
  public String getStdType() {
    return stdType;
  }

  /**
   * Sets std type.
   *
   * @param stdType the std type
   */
  public void setStdType(String stdType) {
    this.stdType = stdType;
  }


  /**
   * Gets content type.
   *
   * @return the content type
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * Sets content type.
   *
   * @param contentType the content type
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Gets std input.
   *
   * @return the std input
   */
  public String getStdInput() {
    return stdInput;
  }

  /**
   * Sets std input.
   *
   * @param stdInput the std input
   */
  public void setStdInput(String stdInput) {
    this.stdInput = stdInput;
  }

  /**
   * Gets std output.
   *
   * @return the std output
   */
  public String getStdOutput() {
    return stdOutput;
  }

  /**
   * Gets skip scenario.
   *
   * @return the skip scenario
   */
  public String getSkipScenario() {
    return skipScenario;
  }

  /**
   * Sets skip scenario.
   *
   * @param skipScenario the skip scenario
   */
  public void setSkipScenario(String skipScenario) {
    this.skipScenario = skipScenario;
  }

  /**
   * Sets std output.
   *
   * @param stdOutput the std output
   */
  public void setStdOutput(String stdOutput) {
    this.stdOutput = stdOutput;
  }

  /**
   * Gets scenario.
   *
   * @return the scenario
   */
  public String getScenario() {
    return scenario;
  }

  /**
   * Sets scenario.
   *
   * @param scenario the scenario
   */
  public void setScenario(String scenario) {
    this.scenario = scenario;
  }

  /**
   * Gets form params.
   *
   * @return the form params
   */
  public List<AvailableParam> getFormParams() {
    return formParams;
  }

  /**
   * Sets form params.
   *
   * @param formParams the form params
   */
  public void setFormParams(List<AvailableParam> formParams) {
    this.formParams = formParams;
  }

  /**
   * Is has form params boolean.
   *
   * @return the boolean
   */
  public boolean isHasFormParams() {
    return hasFormParams;
  }

  /**
   * Sets has form params.
   *
   * @param hasFormParams the has form params
   */
  public void setHasFormParams(boolean hasFormParams) {
    this.hasFormParams = hasFormParams;
  }

  /**
   * Sets has path params.
   *
   * @param hasPathParams the has path params
   */
  public void setHasPathParams(boolean hasPathParams) {
    this.hasPathParams = hasPathParams;
  }


  /**
   * Sets has query params.
   *
   * @param hasQueryParams the has query params
   */
  public void setHasQueryParams(boolean hasQueryParams) {
    this.hasQueryParams = hasQueryParams;
  }

  /**
   * Sets has header params.
   *
   * @param hasHeaderParams the has header params
   */
  public void setHasHeaderParams(boolean hasHeaderParams) {
    this.hasHeaderParams = hasHeaderParams;
  }

  /**
   * Gets has input json map.
   *
   * @return the has input json map
   */
  public boolean getHasInputJsonMap() {
    return hasInputJsonMap;
  }

  /**
   * Sets has input json map.
   *
   * @param hasInputJsonMap the has input json map
   */
  public void setHasInputJsonMap(boolean hasInputJsonMap) {
    this.hasInputJsonMap = hasInputJsonMap;
  }

  /**
   * Gets has output json map.
   *
   * @return the has output json map
   */
  public boolean getHasOutputJsonMap() {
    return hasOutputJsonMap;
  }

  /**
   * Sets has output json map.
   *
   * @param hasOutputJsonMap the has output json map
   */
  public void setHasOutputJsonMap(boolean hasOutputJsonMap) {
    this.hasOutputJsonMap = hasOutputJsonMap;
  }

  /**
   * Gets input json map.
   *
   * @return the input json map
   */
  public Set<Map.Entry<String, String>> getInputJsonMap() {
    return inputJsonMap;
  }

  /**
   * Sets input json map.
   *
   * @param inputJsonMap the input json map
   */
  public void setInputJsonMap(Map<String, String> inputJsonMap) {
    this.inputJsonMap = inputJsonMap.entrySet();
  }

  /**
   * Sets input json map.
   *
   * @param inputJsonMap the input json map
   */
  public void setInputJsonMap(
      Set<Map.Entry<String, String>> inputJsonMap) {
    this.inputJsonMap = inputJsonMap;
  }

  /**
   * Gets output json map.
   *
   * @return the output json map
   */
  public Set<Map.Entry<String, String>> getOutputJsonMap() {
    return outputJsonMap;
  }

  /**
   * Sets output json map.
   *
   * @param outputJsonMap the output json map
   */
  public void setOutputJsonMap(Map<String, String> outputJsonMap) {
    this.outputJsonMap = outputJsonMap.entrySet();
  }

  /**
   * Sets output json map.
   *
   * @param outputJsonMap the output json map
   */
  public void setOutputJsonMap(
      Set<Map.Entry<String, String>> outputJsonMap) {
    this.outputJsonMap = outputJsonMap;
  }

  /**
   * Gets path params.
   *
   * @return the path params
   */
  public List<AvailableParam> getPathParams() {
    return pathParams;
  }

  /**
   * Sets path params.
   *
   * @param pathParams the path params
   */
  public void setPathParams(
      List<AvailableParam> pathParams) {
    this.pathParams = pathParams;
  }

  /**
   * Gets query params.
   *
   * @return the query params
   */
  public List<AvailableParam> getQueryParams() {
    return queryParams;
  }

  /**
   * Sets query params.
   *
   * @param queryParams the query params
   */
  public void setQueryParams(
      List<AvailableParam> queryParams) {
    this.queryParams = queryParams;
  }

  /**
   * Gets header params.
   *
   * @return the header params
   */
  public List<AvailableParam> getHeaderParams() {
    return headerParams;
  }

  /**
   * Sets header params.
   *
   * @param headerParams the header params
   */
  public void setHeaderParams(
      List<AvailableParam> headerParams) {
    this.headerParams = headerParams;
  }

  /**
   * Gets url.
   *
   * @return the url
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets url.
   *
   * @param url the url
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Gets http status code.
   *
   * @return the http status code
   */
  public String getHttpStatusCode() {
    return httpStatusCode;
  }

  /**
   * Sets http status code.
   *
   * @param httpStatusCode the http status code
   */
  public void setHttpStatusCode(String httpStatusCode) {
    this.httpStatusCode = httpStatusCode;
  }

  /**
   * Gets resource.
   *
   * @return the resource
   */
  public String getResource() {
    return resource;
  }

  /**
   * Sets resource.
   *
   * @param resource the resource
   */
  public void setResource(String resource) {
    this.resource = resource;
  }

  /**
   * Gets method.
   *
   * @return the method
   */
  public String getMethod() {
    return method;
  }

  /**
   * Sets method.
   *
   * @param method the method
   */
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

  /**
   * Gets output inline xml.
   *
   * @return the output inline xml
   */
  public List<String> getOutputInline() {
    return outputInline;
  }

  /**
   * Sets output inline xml.
   *
   * @param outputInline the output inline xml
   */
  public void setOutputInline(List<String> outputInline) {
    this.outputInline = outputInline;
  }

  /**
   * Gets output file xml.
   *
   * @return the output file xml
   */
  public String getOutputFile() {
    return outputFile;
  }

  /**
   * Sets output file xml.
   *
   * @param outputFile the output file xml
   */
  public void setOutputFile(String outputFile) {
    this.outputFile = outputFile;
  }

  /**
   * Gets input inline xml.
   *
   * @return the input inline xml
   */
  public List<String> getInputInline() {
    return inputInline;
  }

  /**
   * Sets input inline xml.
   *
   * @param inputInline the input inline xml
   */
  public void setInputInline(List<String> inputInline) {
    this.inputInline = inputInline;
  }

  /**
   * Gets input file xml.
   *
   * @return the input file xml
   */
  public String getInputFile() {
    return inputFile;
  }

  /**
   * Sets input file xml.
   *
   * @param inputFile the input file xml
   */
  public void setInputFile(String inputFile) {
    this.inputFile = inputFile;
  }

  /**
   * Gets input.
   *
   * @return the input
   */
  public String getInput() {
    return input;
  }

  /**
   * Sets input.
   *
   * @param input the input
   */
  public void setInput(String input) {
    this.input = input;
  }

  /**
   * Gets output.
   *
   * @return the output
   */
  public String getOutput() {
    return output;
  }

  /**
   * Sets output.
   *
   * @param output the output
   */
  public void setOutput(String output) {
    this.output = output;
  }

  /**
   * Gets available params.
   *
   * @return the available params
   */
  public List<AvailableParam> getAvailableParams() {
    return availableParams;
  }


  /**
   * Sets available params.
   *
   * @param availableParams the available params
   */
  public void setAvailableParams(
      List<AvailableParam> availableParams) {
    this.availableParams = availableParams;
    Map<String, List<AvailableParam>> stringListMap = availableParams.stream()
        .collect(Collectors.groupingBy(AvailableParam::getParameterType));
    if (!stringListMap.isEmpty()) {
      formParams = stringListMap.get("FORM_PARAM");
      hasFormParams = formParams != null && !formParams.isEmpty();
      pathParams = stringListMap.get("PATH_PARAM");
      hasPathParams = pathParams != null && !pathParams.isEmpty();
      queryParams = stringListMap.get("QUERY_PARAM");
      hasQueryParams = queryParams != null && !queryParams.isEmpty();
      headerParams = stringListMap.get("HEADER_PARAM");
      hasHeaderParams = headerParams != null && !headerParams.isEmpty();
      storageParams = stringListMap.get("STORAGE_PARAM");
      hasStorageParams = storageParams != null && !storageParams.isEmpty();
      createParams = stringListMap.get("ADDIFY_PARAM");
      hasCreateParams = createParams != null && !createParams.isEmpty();
      evaluateParams = stringListMap.get("EVAL_PARAM");
      hasEvaluateParams = evaluateParams != null && !evaluateParams.isEmpty();
      cookieParams = stringListMap.get("COOKIE_PARAM");
      hasCookieParams = cookieParams != null && !cookieParams.isEmpty();
    }
  }

  /**
   * Gets action.
   *
   * @return the action
   */
  public String getAction() {
    return action;
  }

  /**
   * Sets action.
   *
   * @param action the action
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * Gets tags.
   *
   * @return the tags
   */
  public String getTags() {
    return tags;
  }

  /**
   * Sets tags.
   *
   * @param tags the tags
   */
  public void setTags(String tags) {
    this.tags = tags;
  }

  /**
   * Sets no skip output.
   *
   * @param noSkipOutput the no skip output
   */
  public void setNoSkipOutput(boolean noSkipOutput) {
      this.noSkipOutput = noSkipOutput;
  }
}
