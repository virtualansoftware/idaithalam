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

package io.virtualan.idaithalam.core.generator;

import io.virtualan.cucumblan.props.ApplicationConfiguration;
import io.virtualan.cucumblan.props.ExcludeConfiguration;
import io.virtualan.idaithalam.config.IdaithalamConfiguration;
import io.virtualan.idaithalam.core.domain.AvailableParam;
import io.virtualan.idaithalam.core.domain.Item;
import io.virtualan.mapson.Mapson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * The type Feature generation helper.
 */
@Slf4j
public class FeatureGenerationHelper {

  /** Defines whether header or query parameter will be overwritten in case of a duplicate.
   *  If the header key exists, and overwrite is false (default), it will add the value as a comma separated list. 
   *  If the header key exists, and overwrite is true, it will instead take the new value.
   *    If true and duplicate comes from yaml configuration, yaml wins.
   *    If true and duplicate is within collection like an api header key in authorization and second in request, the request value wins. */
  private static Boolean headerOverwrite = false;

  private FeatureGenerationHelper() {
  }

  private static String getResource(JSONArray inputJsonArray) {
    if (inputJsonArray != null && inputJsonArray.length() > 0) {
      return inputJsonArray.getString(0);
    }
    return "default";
  }

  private static String buildEndPointURL(JSONArray inputJsonArray) {
    StringBuilder builder = new StringBuilder();
    if (inputJsonArray != null && inputJsonArray.length() > 0) {
      for (int i = 0; i < inputJsonArray.length(); i++) {
        builder.append("/");
        builder.append(inputJsonArray.getString(i));
      }
    } else {
      builder.append("/");
    }
    return builder.toString();
  }

  /**
   * Author Oliver Glas fix issues #131 #133.
   * Resolve path parameter
   * Resolve variables in path parameter from collection variables.
   */
  private static String resolvePathVariables(String url, JSONArray pathParameter, JSONArray collectionVariable) {
    if (pathParameter == null || collectionVariable == null) return url;
    for (Object o : pathParameter) {
      try {
        JSONObject pathParameterObject = (JSONObject) o;
        String key = pathParameterObject.getString("key");
        String value = pathParameterObject.getString("value");
        Boolean disabled = false;
        try {
          disabled = pathParameterObject.getBoolean("disabled");
        } catch (JSONException js) {
        }
        if (disabled) continue;
        if (value.startsWith("{{") && value.endsWith("}}")) {
          value = replaceWithCollectionValues(collectionVariable, key, value);
        }
        url = url.replace(":" + key, value);
      } catch (Exception e) {
        log.error("Cannot resolve variables for URL " + url + ", " + e.getLocalizedMessage());
      }
    }
    return url;
  }
  /**
   * Author Oliver Glas. Resolve variables and replace with values from collection defined values.
   */
  private static String replaceWithCollectionValues(JSONArray variable, String key, String value) {
    if (variable == null || key == null) return value;
    Boolean disabled = Boolean.FALSE;
    for (Object ov : variable) {
      JSONObject jsonVariable = (JSONObject) ov;
      try {
        disabled = jsonVariable.getBoolean("disabled");
      } catch (JSONException je) {
      }
      if (disabled) continue;
      if (key.equals(jsonVariable.getString("key"))) {
        return jsonVariable.getString("value");
      }
    }
    return value;
  }
  /**
   * Author Oliver Glas.
   * Replace all variables in the query parameters with values from Collection values.
   */
  private static void replaceQueryVariableValues(JSONArray collectionVariable, JSONArray queryParameterArr) {
    if (queryParameterArr != null) {
      int count = 0;
      for (Object o : queryParameterArr) {
        JSONObject jsonObject = (JSONObject) o;
        String value = jsonObject.getString("value");
        Boolean disabled = false;
        try {
          disabled = jsonObject.getBoolean("disabled");
        } catch (JSONException je) {
        }
        if (disabled) continue;
        if (value.startsWith("{{") && value.endsWith("}}")) {
          String varKey = value.substring(2, value.length() - 2);
          value = replaceWithCollectionValues(collectionVariable, varKey, value);
          queryParameterArr.optJSONObject(count).put("value", value);
        }
        count++;
      }
    }
  }
  /**
   * Author Oliver Glas. Create JSONObject for the Postman collection authorization.
   */
  private static JSONObject getJsonObject(JSONObject authCollection) {
    JSONObject jsonAuth = null;
    if (authCollection != null) {
      try {
        jsonAuth = new JSONObject();
        JSONArray apikey = authCollection.getJSONArray("apikey");
        jsonAuth.put("parameterType", "HEADER_PARAM");
        for (Object o : apikey) {
          JSONObject apikeyObject = (JSONObject) o;
          String type = apikeyObject.getString("key");
          if (type.equals("key")) {
            jsonAuth.put("key", apikeyObject.getString("value"));
          } else if (type.equals("value")) {
            jsonAuth.put("value", apikeyObject.getString("value"));
          }
        }
      } catch (JSONException je) {
      }
    }
    return jsonAuth;
  }

  private static void addParams(JSONArray inputJsonArray, JSONArray outputJsonArray, String param) {
    if (inputJsonArray != null && inputJsonArray.length() > 0) {
      for (int j = 0; j < inputJsonArray.length(); j++) {
        JSONObject virtualanObjParam = new JSONObject();
        if (inputJsonArray.getJSONObject(j) instanceof JSONObject &&
            !"".equalsIgnoreCase(inputJsonArray.getJSONObject(j).optString("key"))) {
          virtualanObjParam.put("key", inputJsonArray.getJSONObject(j).optString("key"));
          virtualanObjParam.put("value", inputJsonArray.getJSONObject(j).optString("value"));
          virtualanObjParam.put("parameterType", param);
          outputJsonArray.put(virtualanObjParam);
        }
      }
    }

  }

  /**
   * Create post man to virtualan json array.
   *
   * @param object the object
   * @return the json array
   */
  public static JSONArray createPostManToVirtualan(JSONObject object) {
//    headerOverwrite = overwrite;
    JSONArray virtualanArry = new JSONArray();
    if (object != null) {
      JSONArray arr = checkIfItemsOfItem(object.getJSONArray("item"));
      /** Fix Oliver Glas issue #131 & #133 */
      JSONArray arrVariable = null;
      JSONObject authCollection = null;
      try {
        arrVariable = object.getJSONArray("variable");
        authCollection = object.getJSONObject("auth");
      } catch (JSONException jsonException) {
        //methods using those objects take care of null.
      }
      if (arr != null && arr.length() > 0) {
        for (int i = 0; i < arr.length(); i++) {
          buildVirtualanFromPostMan(virtualanArry, arr, i, arrVariable, authCollection);
        }
        return virtualanArry;
      }
    }
    log.warn("Not a valid POSTMAN Collection? Check the file.");
    return virtualanArry;
  }


  /** Fix for #124 by @author Oliver Glas */
  private static JSONArray checkIfItemsOfItem(JSONArray arr) {
    if (arr != null && arr.length() > 0) {
      JSONArray newJsonArray = new JSONArray();
      for (int i = 0; i < arr.length(); i++) {
        JSONArray itemArray = arr.optJSONObject(i).optJSONArray("item");
        if (itemArray != null && itemArray.length() > 0) {
          JSONArray jsonArray = checkIfItemsOfItem(itemArray);
          for (int n = 0; n < jsonArray.length(); n++) {
            JSONObject o = jsonArray.optJSONObject(n);
            newJsonArray.put(o);
          }
        } else {
          newJsonArray.put(arr.optJSONObject(i));
        }
      }
      return newJsonArray;
    }
    return arr;
  }

  /** Fix 122,131,133 */
  private static void buildVirtualanFromPostMan(JSONArray virtualanArry, JSONArray arr, int i, JSONArray arrVariable, JSONObject authCollection) {
    if (arr.optJSONObject(i) instanceof JSONObject) {
      if (arr.optJSONObject(i).optJSONArray("response") instanceof JSONArray) {
        JSONArray responseArray = arr.getJSONObject(i).getJSONArray("response");
        if (responseArray != null && responseArray.length() > 0) {
          for (int j = 0; j < responseArray.length(); j++) {
            if (responseArray.optJSONObject(j) instanceof JSONObject) {
              JSONObject virtualanObj = buildVirtualanObject(responseArray, j, arrVariable, authCollection);
              virtualanArry.put(virtualanObj);
            }
          }
        }
      }
    }
  }



  private static String getContentType(JSONObject jsonObject) {
    JSONArray arrayObject = jsonObject.optJSONArray("header");
    if (arrayObject != null) {
      for (int i = 0; i < arrayObject.length(); i++) {
        JSONObject object = arrayObject.optJSONObject(i);
        if ("Content-Type".equalsIgnoreCase(object.optString("name"))) {
          return object.optString("value");
        }
      }
    }
    return "application/json";
  }

  /** Fix 122,131,133 Oliver Glas added some fixes for path parameter, authorization (api key) and variables for both path and query parameter. */
  private static JSONObject buildVirtualanObject(JSONArray responseArray, int j, JSONArray collectionVariable, JSONObject authCollection) {
    JSONObject virtualanObj = new JSONObject();
    String contentType = getContentType(
            responseArray.optJSONObject(j).optJSONObject("originalRequest"));
    virtualanObj.put("contentType", contentType);
    virtualanObj.put("scenario", responseArray.optJSONObject(j).optString("name"));
    virtualanObj.put("method",
            responseArray.optJSONObject(j).optJSONObject("originalRequest")
                    .optString("method"));
    String url = buildEndPointURL(responseArray.optJSONObject(j).optJSONObject("originalRequest").optJSONObject("url").optJSONArray("path"));
    JSONArray pathParameter = responseArray.optJSONObject(j).optJSONObject("originalRequest").optJSONObject("url").optJSONArray("variable");
    url = resolvePathVariables(url, pathParameter, collectionVariable);
    virtualanObj.put("url", url);

    //Take care of the query parameter variables.
    JSONArray queryParameterArr = responseArray.optJSONObject(j).optJSONObject("originalRequest").optJSONObject("url").optJSONArray("query");
    replaceQueryVariableValues(collectionVariable, queryParameterArr);

    //Get Api key from Authorization of the colleciton.
    JSONObject jsonAuth = getJsonObject(authCollection);

    extracted(responseArray, j, virtualanObj);
    virtualanObj.put("output", responseArray.optJSONObject(j).optString("body"));
    virtualanObj.put("httpStatusCode", responseArray.optJSONObject(j).optString("code"));
    JSONArray paramsArray = new JSONArray();
    if (jsonAuth != null && jsonAuth.isEmpty() == false) paramsArray.put(jsonAuth);
    extractedParams(responseArray, j, virtualanObj, paramsArray);
    return virtualanObj;
  }

  private static void extractedParams(JSONArray responseArray, int j, JSONObject virtualanObj,
      JSONArray paramsArray) {
    addParams(responseArray.optJSONObject(j).optJSONObject("originalRequest")
        .optJSONArray("header"), paramsArray, "HEADER_PARAM");
    addParams(responseArray.optJSONObject(j).optJSONObject("originalRequest")
        .optJSONObject("url").optJSONArray("query"), paramsArray, "QUERY_PARAM");
    virtualanObj
        .put("resource", getResource(responseArray.optJSONObject(j).optJSONObject("originalRequest")
            .optJSONObject("url").optJSONArray("path")));
    if (paramsArray.length() > 0) {
      virtualanObj.put("availableParams", paramsArray);
    }
  }

  private static void extracted(JSONArray responseArray, int j, JSONObject virtualanObj) {
    if (responseArray.optJSONObject(j).optJSONObject("originalRequest")
        .optJSONObject("body") != null) {
      String input = responseArray.optJSONObject(j).optJSONObject("originalRequest")
          .optJSONObject("body").optString("raw");
      if (!"".equalsIgnoreCase(input)) {
        virtualanObj.put("input", input);
      }
    }
  }


  /**
   * Create feature file list.
   *
   * @param excludeConfiguration the exclude configuration
   * @param arr                  the arr
   * @param path                 the path
   * @return the list
   * @throws IOException the io exception
   */
  public static List<Item> createFeatureFile(Map<String, String> excludeConfiguration,
      JSONArray arr, String path) throws IOException {
    List<Item> result = new ArrayList<>();
    if (arr != null && arr.length() > 0) {
      for (int i = 0; i < arr.length(); i++) {
        Item item = getItem(excludeConfiguration, arr.optJSONObject(i), path);
        result.add(item);
      }
    }
    return result;
  }

  private static Item getItem(Map<String, String> excludeConfiguration, JSONObject object,
      String path) throws IOException {
    Item item = new Item();
    item.setMessageType(getValueMapping("messageType", object, item));
    item.setIdentifier(getValueMapping("identifier", object, item));
    item.setEvent(getValueMapping("event", object, item));
    item.setStepInfo(getValueMapping("stepInfo", object, item));
    extractedInput(object, item, path);
    item.setSkipScenario(getValueMapping("skipScenario", object, item));
    extractedMultiRun(object, item);
    item.setTags(object.optString("tags"));
    item.setHttpStatusCode(object.optString("httpStatusCode"));
    item.setMethod(object.optString("method"));
    item.setAction(object.optString("method").toLowerCase());
    item.setResource(object.optString("resource"));
    if (object.optString("security") != null && !object.optString("security").isEmpty()) {
      if ("okta".equalsIgnoreCase(object.optString("security"))) {
        item.setOkta(object.optString("security"));
      } else if ("basicAuth".equalsIgnoreCase(object.optString("security"))) {
        item.setBasicAuth(object.optString("security"));
      } else {
        log.warn("Unknown security setup");
      }
    }
    extractedScenario(object, item);
    List<AvailableParam> availableParams = getAvailableParamList(object);
    item.setAvailableParams(availableParams);
    item.setUrl(object.optString("url"));
    extractedOutput(excludeConfiguration, object, item, path);
    item.setStdType(getStandardType(availableParams));
    if ("KAFKA".equalsIgnoreCase(object.optString("requestType"))) {
      item.setKafka(true);
      item.setKafkaInput(item.getInput() != null && !item.getInput().isEmpty());
      item.setKafkaOutput(hasOutput(item));
    } else if ("DB".equalsIgnoreCase(object.optString("requestType"))) {
      item.setDatabase(true);
      item.setDbInput(item.getInput() != null && !item.getInput().isEmpty());
      item.setDbOutput(hasOutput(item));
    } else if ("AMQ".equalsIgnoreCase(object.optString("requestType"))) {
      item.setAMQ(true);
      item.setAmqInput(item.getInput() != null && !item.getInput().isEmpty());
      item.setAmqOutput(hasOutput(item));
    } else {
      item.setRest(true);
    }
    return item;
  }

  private static boolean hasOutput(Item item) {
    return item.getHasCsvson() != null || item.isHasResponseByField() || item
        .isHasOutputFileByPath() || item.getOutput() != null;
  }

  private static void extractedMultiRun(JSONObject object, Item item) {
    List<String> multiRun = new ArrayList();
    if (object.optJSONArray("rule") != null && !object.optJSONArray("rule").isEmpty()) {
      multiRun.add(object.optJSONArray("rule").getJSONObject(0).keySet().stream().map(x -> x).collect(Collectors.joining("|")));
      Set<String> keys = object.optJSONArray("rule").getJSONObject(0).keySet();
      List<String> stringList = IntStream.range(0,object.optJSONArray("rule").length())
          .mapToObj(i-> getPipeSeparatedValue(object.optJSONArray("rule").getJSONObject(i), keys))
          .collect(Collectors.toList());
      multiRun.addAll(stringList);
      item.setMultiRun(multiRun);
      item.setHasMultiRun(true);
    }
  }

  private static String getPipeSeparatedValue(JSONObject rule, Set<String> keys) {
    StringBuffer buffer = new StringBuffer();
    boolean skip = false;
    for (String key: keys) {
      if(skip) {
        buffer.append("|").append(rule.getString(key));
      } else {
        buffer.append(rule.getString(key));
        skip = true;
      }
    }
    return buffer.toString();
  }

  private static String getValueMapping(String mapping, JSONObject object, Item item) {
    if (object.optString(mapping) != null && !object.optString(mapping).isEmpty()) {
      return object.optString(mapping);
    }
    return null;
  }

  private static String getStandardType(List<AvailableParam> availableParams) {
    Optional<AvailableParam> availableParam = availableParams.stream()
        .filter(x -> "VirtualanStdType".equalsIgnoreCase(x.getKey())).findAny();
    if (availableParam.isPresent()) {
      return availableParam.get().getValue();
    }
    return null;
  }


  private static String getUrl(String url, JSONArray array) {
    if (array != null && array.length() > 0) {
      for (int i = 0; i < array.length(); i++) {
        JSONObject object = array.getJSONObject(i);
        String type = object.getString("parameterType");
        if ("PATH_PARAM".equalsIgnoreCase(type)) {
          url = url.replace(object.getString("key"), object.getString("value"));
        }
      }
      url = url.replaceAll("\\{", "").replaceAll("}", "");
    }
    return url;
  }

  private static void extractedScenario(JSONObject object, Item item) {
    if ("".equalsIgnoreCase(object.optString("scenario"))) {
      item.setScenario(object.optString("operationId"));
    } else {
      item.setScenario(object.optString("scenario"));
    }
  }

  private static boolean isExtraCondition(String condition) {
    return condition.contains("jsonpath=") || condition.contains("match");
  }

  private static SimpleEntry<String, String> keyValue(String filters) {
    if (filters.split("=").length == 2) {
      return new SimpleEntry(filters.split("=")[0], filters.split("=")[1]);
    } else if (filters.split("(?<!\\\\)=").length == 2) {
      return new SimpleEntry(removeVirtualanEqualsEscape(filters.split("(?<!\\\\)=")[0]),
          removeVirtualanEqualsEscape(filters.split("(?<!\\\\)=")[1]));
    } else {
      log.warn(" Does not seems like Kep Value Pair - {}", filters);
    }
    return null;
  }

  private static void extractedOutput(Map<String, String> excludeProperties, JSONObject object,
      Item item, String path)
      throws IOException {
    item.setNoSkipOutput(!ExcludeConfiguration.shouldSkip(excludeProperties, item.getUrl(), null));
    if (object.optJSONArray("csvson") != null && object.optJSONArray("csvson").length() > 0) {
      item.setHasCsvson(object.optJSONArray("csvson").toString());
      JSONArray listOFRows = object.optJSONArray("csvson");
      List<String> stringList = IntStream.range(0,object.optJSONArray("csvson").length()).mapToObj(i->object.optJSONArray("csvson").getString(i)).collect(Collectors.toList());
      if (isExtraCondition(listOFRows.optString(0))) {
        String[] filterConditions = listOFRows.optString(0).split("(?<!\\\\);");
        for (String outputJsonUnEscaped : filterConditions) {
          SimpleEntry<String, String> filter = keyValue(
              removeVirtualanSemicolonEscape(outputJsonUnEscaped));
          if (filter == null) {
          } else if (filter.getKey().contains("jsonpath")) {
            item.setCsvsonPath(filter.getValue());
          } else if (filter.getKey().contains("match") &&
              (filter.getValue().contains("exact-match") || filter.getValue().contains("exact-order-match"))) {
              item.setMatchPattern(filter.getValue());
          }
        }
        if(item.getCsvsonPath() == null){
          item.setCsvsonPath("api");
        }
        item.setCsvson(stringList.subList(1, stringList.size()));
      } else {
        item.setCsvsonPath("api");
        item.setCsvson(stringList);
      }
    }
      if (object.optJSONObject("outputFields") != null && object.optJSONObject("outputFields").length() != 0) {
        item.setHasResponseByField(true);
        Map<String, String> outputFieldMap = new HashMap(object.optJSONObject("outputFields").toMap());
        item.setHasResponseByField(true);
        item.setResponseByField(outputFieldMap);
    } else {
      item.setHasOutputFileByPath(object.optJSONArray("outputPaths") != null && !object.optJSONArray("outputPaths").isEmpty());
      item.setOutput(replaceSpecialChar(object.optString("output")));
      if (item.isHasOutputFileByPath()) {
        List<String> stringList = IntStream.range(0,object.optJSONArray("outputPaths").length()).mapToObj(i->object.optJSONArray("outputPaths").getString(i)).collect(Collectors.toList());
        item.setOutputFileByPath(stringList);
        String fileName =
            object.optString("scenario").replaceAll("[^a-zA-Z0-9.-]", "-") + "_response.txt";
        createFile(item.getOutput(), path + "/" + fileName);
        item.setOutputFile(fileName);
      } else if (item.getOutput() != null && !object.optString("output").isEmpty()
          && object.optString("contentType").toLowerCase().contains("xml")) {
        if (!ApplicationConfiguration.getInline() && item.getOutput().length() > 700) {
          String fileName =
              object.optString("scenario").replaceAll("[^a-zA-Z0-9.-]", "-") + "_response.xml";
          createFile(item.getOutput(), path + "/" + fileName);
          item.setOutputFile(fileName);
        } else {
          item.setOutputInline(getStringAsList(replaceSpecialChar(item.getOutput())));
          item.setHasOutputInline(item.getOutput());
        }
      } else if (item.getOutput() != null && !object.optString("output").isEmpty()) {
        try {
          Object jsonObject = getJSON(item.getOutput());
          if (jsonObject instanceof JSONArray || jsonObject instanceof JSONObject) {
            item.setOutputJsonMap(Mapson.buildMAPsonFromJson(item.getOutput()));
            if (!ExcludeConfiguration.shouldSkip(excludeProperties, item.getUrl(), null)) {
              item.setHasOutputJsonMap(true);
            }
          } else {
            item.setStdOutput(item.getOutput());
          }
        } catch (JSONException e) {
          if (item.getOutput().contains("{") && item.getOutput().contains("}")) {
            log.warn(" Check Invalid JSON >> " + item.getOutput());
          } else {
            item.setStdOutput(item.getOutput());
          }
        }
      }
    }
  }

  public static String removeVirtualanSemicolonEscape(String input) {
    return input.replace("\\\\;", ";");
  }

  public static String removeVirtualanEqualsEscape(String input) {
    return input.replace("\\\\=", "=");
  }

  private static void createFile(String content, String path) throws IOException {
    Files.write(Paths.get(path), content.getBytes());
  }

  private static void extractedInput(JSONObject object, Item item, String path) throws
      IOException {
    item.setContentType(object.optString("contentType"));
    item.setInput(replaceSpecialChar(object.optString("input")));
    if ("DB".equalsIgnoreCase(object.optString("type"))
            || "AMQ".equalsIgnoreCase(object.optString("type"))) {
      item.setInputInline(getStringAsList(item.getInput()));
      item.setHasInputInline(item.getInput());
    } else if (item.getInput() != null && !"".equalsIgnoreCase(item.getInput())
        && !object.optString("contentType").toLowerCase().contains("json")) {
      if (!ApplicationConfiguration.getInline() && item.getInput().length() > 700) {
        String fileName =
            object.optString("scenario").replaceAll("[^a-zA-Z0-9.-]", "-") + "_request.idai";
        createFile(item.getInput(), path + "/" + fileName);
        item.setInputFile(fileName);
      } else {
        item.setInputInline(getStringAsList(item.getInput()));
        item.setHasInputInline(item.getInput());
      }
      item.setContentType(object.optString("contentType"));
    } else if (item.getInput() != null && !"".equalsIgnoreCase(item.getInput())) {
      try {
        JSONTokener jsonTokener = new JSONTokener(item.getInput());
        Object jsonObject = getJSON(item.getInput());
        if (jsonObject instanceof JSONArray || jsonObject instanceof JSONObject) {
          item.setInputJsonMap(Mapson.buildMAPsonFromJson(item.getInput()));
          item.setHasInputJsonMap(true);
        } else {
          item.setStdInput(item.getInput());
        }
      } catch (JSONException e) {
        if (item.getInput().contains("{") && item.getInput().contains("}")) {
          log.warn(" Check Invalid JSON >> " + item.getInput());
        } else {
          item.setStdInput(item.getInput());
        }
      }
    }
  }

  private static Object getJSON(String json) {
    try {
      return new JSONObject(json);
    } catch (JSONException err) {
      try {
        return new JSONArray(json);
      } catch (Exception e) {
        if (json.contains("{") && json.contains("}")) {
          log.warn(json + " is not a valid JSON!. Correct the JSON file!");
        }
        return json;
      }
    }
  }

  private static String replaceSpecialChar(String request) {
    String skipChars = IdaithalamConfiguration.getProperty("SPECIAL_SKIP_CHAR");
    skipChars = skipChars == null ? "\\|=\\\\\\\\|;\\\\n=\\\\\\\\n;\\\\r=\\\\\\\\r;" : skipChars;
    String replacedChar = request;
    for (String skipChar : skipChars.split(";")) {
      replacedChar = replacedChar.replaceAll(skipChar.split("=")[0], skipChar.split("=")[1]);
    }
    return replacedChar;
  }

  private static List<String> getStringAsList(String item) {
    String lines[] = item.split("\\n");
    List<String> lists = new ArrayList<>();
    for (String line : lines) {
      lists.add(line.trim());
    }
    return lists;
  }

  private static List<AvailableParam> getAvailableParamList(JSONObject object) {
    List<AvailableParam> availableParams = new ArrayList<>();
    JSONArray params = object.optJSONArray("availableParams");
    if (params != null && params.length() > 0) {
      for (int j = 0; j < params.length(); j++) {
        AvailableParam param = new AvailableParam(params.optJSONObject(j).optString("key"),
            params.optJSONObject(j).optString("value"),
            params.optJSONObject(j).optString("parameterType"));
        if ("EVAL_PARAM".equalsIgnoreCase(param.getParameterType())) {
          if (param.getValue().startsWith("i~")) {
            param.setInteger(true);
            param.setValue(param.getValue().substring(2));
          } else if (param.getValue().startsWith("d~")) {
            param.setDecimal(true);
            param.setValue(param.getValue().substring(2));
          } else if (param.getValue().startsWith("c~")) {
            param.setCondition(true);
            param.setValue(param.getValue().substring(2));
          } else if (param.getValue().startsWith("b~")) {
            param.setBoolean(true);
            param.setValue(param.getValue().substring(2));
          } else {
            param.setString(true);
          }
        } else if ("MULTI_FORM_PARAM".equalsIgnoreCase(param.getParameterType())) {
            param.setMultiPart(true);
        }
        availableParams.add(param);
      }
    }
    return availableParams;
  }
}
