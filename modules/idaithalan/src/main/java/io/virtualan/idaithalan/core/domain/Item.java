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

package io.virtualan.idaithalan.core.domain;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Item {
    String scenario;
    String httpStatusCode;
    String resource;
    String method;
    String action;
    String input;
    String output;
    Map<String, String> hasInputJsonMap;
    Map<String, String> hasOutputJsonMap;
    Set<Map.Entry<String, String>> inputJsonMap;
    Set<Map.Entry<String, String>> outputJsonMap;
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

    public Item() {
    }


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

    public Set<Map.Entry<String, String>> getInputJsonMap() {
        return inputJsonMap;
    }

    public void setInputJsonMap(Map<String, String> inputJsonMap) {
        this.inputJsonMap = inputJsonMap.entrySet();
        this.hasInputJsonMap = inputJsonMap;
    }

    public void setInputJsonMap(
            Set<Map.Entry<String, String>> inputJsonMap) {
        this.inputJsonMap = inputJsonMap;
    }

    public Set<Map.Entry<String, String>> getOutputJsonMap() {
        return outputJsonMap;
    }

    public void setOutputJsonMap(Map<String, String> outputJsonMap) {
        this.outputJsonMap = outputJsonMap.entrySet();
        this.hasOutputJsonMap = outputJsonMap;
    }

    public void setOutputJsonMap(
            Set<Map.Entry<String, String>> outputJsonMap) {
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
        Map<String, List<AvailableParam>> stringListMap = availableParams.stream()
                .collect(Collectors.groupingBy(AvailableParam::getParameterType));
        if (!stringListMap.isEmpty()) {
            pathParams = stringListMap.get("PATH_PARAM");
            hasPathParams = pathParams != null && !pathParams.isEmpty();
            queryParams = stringListMap.get("QUERY_PARAM");
            hasQueryParams = queryParams != null && !queryParams.isEmpty();
            headerParams = stringListMap.get("HEADER_PARAM");
            hasHeaderParams = headerParams != null && !headerParams.isEmpty();
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
