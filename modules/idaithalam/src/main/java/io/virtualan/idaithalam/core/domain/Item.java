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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Item.
 */
public class Item {

    /**
     * The Scenario.
     */
    String contentType = "application/json";
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
     * The Has input json map.
     */
    boolean hasInputJsonMap;
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
    List<AvailableParam> createParams;

    /**
     * The Path params.
     */
    List<AvailableParam> storageParams;

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
    String  hasInputInline;

    /**
     * The Input file xml.
     */
    String inputFile;

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
            pathParams = stringListMap.get("PATH_PARAM");
            hasPathParams = pathParams != null && !pathParams.isEmpty();
            queryParams = stringListMap.get("QUERY_PARAM");
            hasQueryParams = queryParams != null && !queryParams.isEmpty();
            headerParams = stringListMap.get("HEADER_PARAM");
            hasHeaderParams = headerParams != null && !headerParams.isEmpty();
            storageParams  = stringListMap.get("STORAGE_PARAM");
            hasStorageParams = storageParams != null && !storageParams.isEmpty();
            createParams  = stringListMap.get("ADDIFY_PARAM");
            hasCreateParams = createParams != null && !createParams.isEmpty();
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
}
