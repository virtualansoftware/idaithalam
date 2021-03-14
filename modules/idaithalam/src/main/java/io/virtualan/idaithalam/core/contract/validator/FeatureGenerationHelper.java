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

package io.virtualan.idaithalam.core.contract.validator;

import io.virtualan.cucumblan.props.ApplicationConfiguration;
import io.virtualan.cucumblan.props.ExcludeConfiguration;
import io.virtualan.idaithalam.core.domain.AvailableParam;
import io.virtualan.idaithalam.core.domain.Item;
import io.virtualan.mapson.Mapson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
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


  final private static Logger LOGGER = Logger.getLogger(FeatureGenerationHelper.class.getName());

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
    JSONArray virtualanArry = new JSONArray();
    if (object != null) {
      JSONArray arr = checkIfItemsOfItem(object.getJSONArray("item"));
      if (arr != null && arr.length() > 0) {
        for (int i = 0; i < arr.length(); i++) {
          buildVirtualanFromPostMan(virtualanArry, arr, i);
        }
        return virtualanArry;
      }
    }
    LOGGER.warning("Not a valid POSTMAN Collection? check the file");
    return virtualanArry;
  }

  private static JSONArray checkIfItemsOfItem(JSONArray arr) {
    if (arr != null && arr.length() > 0) {
      JSONArray array = arr.optJSONObject(0).optJSONArray("item");
      if (array != null && array.length() > 0) {
        return checkIfItemsOfItem(array);
      }
    }
    return arr;
  }


  private static void buildVirtualanFromPostMan(JSONArray virtualanArry, JSONArray arr, int i) {
    if (arr.optJSONObject(i) instanceof JSONObject) {
      if (arr.optJSONObject(i).optJSONArray("response") instanceof JSONArray) {
        JSONArray responseArray = arr.getJSONObject(i).getJSONArray("response");
        if (responseArray != null && responseArray.length() > 0) {
          for (int j = 0; j < responseArray.length(); j++) {
            if (responseArray.optJSONObject(j) instanceof JSONObject) {
              JSONObject virtualanObj = buildVirtualanObject(responseArray, j);
              virtualanArry.put(virtualanObj);
            }
          }
        }
      }
    }
  }


  private static ContentType getContentType(JSONObject jsonObject) {
    JSONArray arrayObject = jsonObject.optJSONArray("header");
    if (arrayObject != null) {
      for (int i = 0; i < arrayObject.length(); i++) {
        JSONObject object = arrayObject.optJSONObject(i);
        if ("Content-Type".equalsIgnoreCase(object.optString("name")) &&
            "text/xml".equalsIgnoreCase(object.optString("value"))
            || (object.optString("value").contains("xml"))) {
          return ContentType.XML;
        }
      }
    }
    return ContentType.JSON;
  }

  private static JSONObject buildVirtualanObject(JSONArray responseArray, int j) {
    JSONObject virtualanObj = new JSONObject();
    ContentType contentType = getContentType(
        responseArray.optJSONObject(j).optJSONObject("originalRequest"));
    virtualanObj.put("contentType", contentType.name());
    virtualanObj.put("scenario", responseArray.optJSONObject(j).optString("name"));
    virtualanObj.put("method",
        responseArray.optJSONObject(j).optJSONObject("originalRequest")
            .optString("method"));
    virtualanObj.put("url",
        buildEndPointURL(responseArray.optJSONObject(j).optJSONObject("originalRequest")
            .optJSONObject("url").optJSONArray("path")));
    extracted(responseArray, j, virtualanObj);
    virtualanObj.put("output", responseArray.optJSONObject(j).optString("body"));
    virtualanObj.put("httpStatusCode", responseArray.optJSONObject(j).optString("code"));
    JSONArray paramsArray = new JSONArray();
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
   * @param arr  the arr
   * @param path the path
   * @return the list
   * @throws IOException the io exception
   */
  public static List<Item> createFeatureFile(JSONArray arr, String path) throws IOException {
    List<Item> result = new ArrayList<>();
    if (arr != null && arr.length() > 0) {
      for (int i = 0; i < arr.length(); i++) {
        Item item = getItem(arr.optJSONObject(i), path);
        result.add(item);
      }
    }
    return result;
  }

  private static Item getItem(JSONObject object, String path) throws IOException {
    Item item = new Item();
    extractedInput(object, item, path);
    item.setHttpStatusCode(object.optString("httpStatusCode"));
    item.setMethod(object.optString("method"));
    item.setAction(object.optString("method").toLowerCase());
    item.setResource(object.optString("resource"));
    if(object.optString("security") != null && !object.optString("security").isEmpty()){
      if("okta".equalsIgnoreCase(object.optString("security"))) {
        item.setOkta(object.optString("security"));
      }else if("basicAuth".equalsIgnoreCase(object.optString("security"))) {
        item.setBasicAuth(object.optString("security"));
      }else {
        log.warn("Unknown security setup");
      }
    }
    extractedScenario(object, item);
    List<AvailableParam> availableParams = getAvailableParamList(object);
    item.setAvailableParams(availableParams);
    item.setUrl(object.optString("url"));
    extractedOutput(object, item, path);
    item.setStdType(getStandardType(availableParams));
    return item;
  }

  private static String getStandardType(List<AvailableParam> availableParams) {
    Optional<AvailableParam> availableParam = availableParams.stream().filter( x -> "VirtualanStdType".equalsIgnoreCase(x.getKey())).findAny();
    if(availableParam.isPresent()) return availableParam.get().getValue();
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

  private static void extractedOutput( JSONObject object, Item item, String path)
      throws IOException {
    item.setOutput(object.optString("output"));
    if (item.getOutput() != null && !"".equalsIgnoreCase(item.getOutput())
        && "XML".equalsIgnoreCase(object.optString("contentType"))) {
      if( !ApplicationConfiguration.getInline() && item.getInput().length() > 700){
        String fileName = object.optString("scenario").replaceAll("[^a-zA-Z0-9.-]",  "-")+"_response.xml";
        createFile(item.getOutput(), path + "/"+ fileName);
        item.setOutputFile(fileName);
      } else {
        item.setOutputInline(getStringAsList(item.getOutput()));
        item.setHasOutputInline(item.getOutput());
      }
    } else if (item.getOutput() != null && !"".equalsIgnoreCase(item.getOutput())) {
      try {
        Object jsonObject = getJSON(item.getOutput());
        if(jsonObject instanceof  JSONArray || jsonObject instanceof  JSONObject) {
          item.setInputJsonMap(Mapson.buildMAPsonFromJson(item.getInput()));
          item.setHasInputJsonMap(true);
          item.setOutputJsonMap(Mapson.buildMAPsonFromJson(item.getOutput()));
          if (!ExcludeConfiguration.shouldSkip(item.getUrl(), null)) {
            item.setHasOutputJsonMap(true);
          }
        } else {
          item.setStdOutput(item.getOutput());
        }
      } catch (JSONException e) {
        if(item.getOutput().contains("{") && item.getOutput().contains("}")) {
          log.warn(" Check Invalid JSON >> " + item.getOutput());
        } else {
          item.setStdOutput(item.getOutput());
        }
      }

    }
  }

  private static void createFile(String content, String path) throws IOException {
    Files.write(Paths.get(path), content.getBytes());
  }

  private static void extractedInput(JSONObject object, Item item, String path) throws IOException {
    item.setInput(object.optString("input"));
    if (item.getInput() != null && !"".equalsIgnoreCase(item.getInput())
      && "XML".equalsIgnoreCase(object.optString("contentType"))) {
      if(!ApplicationConfiguration.getInline() && item.getInput().length() > 700){
        String fileName =  object.optString("scenario").replaceAll("[^a-zA-Z0-9.-]",  "-")+"_request.xml";
         createFile(item.getInput(), path + "/"+ fileName);
        item.setInputFile(fileName);
      } else {
        item.setInputInline(getStringAsList(item.getInput()));
        item.setHasInputInline(item.getInput());
      }
      item.setContentType("text/xml");
    } else if (item.getInput() != null && !"".equalsIgnoreCase(item.getInput())) {
      try {
        JSONTokener jsonTokener = new JSONTokener(item.getInput());
        Object jsonObject = getJSON(item.getInput());
        if(jsonObject instanceof  JSONArray || jsonObject instanceof  JSONObject) {
          item.setInputJsonMap(Mapson.buildMAPsonFromJson(item.getInput()));
          item.setHasInputJsonMap(true);
        } else {
          item.setStdInput(item.getInput());
        }
      } catch (JSONException e) {
        if(item.getInput().contains("{") && item.getInput().contains("}")) {
          log.warn(" Check Invalid JSON >> " + item.getInput());
        } else {
          item.setStdInput(item.getInput());
        }
      }
    }
  }

  private static Object getJSON(String json){
    try {
      return new JSONObject(json);
    }catch (JSONException err){
      try{
        return new JSONArray(json);
      }catch (Exception e){
        return json;
      }
    }
  }

  private static List<String> getStringAsList(String item) {
    String lines[] = item.split("\\n");
    List<String> lists =  new ArrayList<>();
    for(String line: lines) {
      lists.add(line.trim());
    }
    return lists;
  }


  private static List<AvailableParam> getAvailableParamList(JSONObject object) {
    List<AvailableParam> availableParams = new ArrayList<>();
    JSONArray params = object.optJSONArray("availableParams");
    if (params != null && params.length() > 0) {
      for (int j = 0; j < params.length(); j++) {
        availableParams.add(new AvailableParam(params.optJSONObject(j).optString("key"),
            params.optJSONObject(j).optString("value"),
            params.optJSONObject(j).optString("parameterType")));
      }
    }
    return availableParams;
  }
}
