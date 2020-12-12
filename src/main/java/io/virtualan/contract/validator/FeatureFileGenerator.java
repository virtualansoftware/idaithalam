package io.virtualan.contract.validator;

import io.virtualan.contract.validator.FeatureFileProcessor.AvailableParam;
import io.virtualan.contract.validator.FeatureFileProcessor.Item;
import io.virtualan.cucumblan.props.ApplicationConfiguration;
import io.virtualan.mapson.Mapson;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class FeatureFileGenerator {

  private final static Logger LOGGER = Logger.getLogger(FeatureFileGenerator.class.getName());

  public FeatureFileGenerator() {
  }

  public static List<Item> generateFeatureFile() {
    String contractFileName = ApplicationConfiguration.getProperty("virtualan.data.load");
    String contractFileType = ApplicationConfiguration.getProperty("virtualan.data.type");
    JSONArray jsonArray = null;
    if (contractFileType == null) {
      LOGGER.severe("provide appropriate virtualan.data.type for the input data?");
      System.exit(0);
    } else if ("POSTMAN".equalsIgnoreCase(contractFileType)) {
      jsonArray = createPosManToVirtualan(getJSONObject(contractFileName));
    } else {
      jsonArray = getJSONArray(contractFileName);
    }
    List<Item> result = createFeatureFile(jsonArray);
    return result;
  }

  private static JSONArray createPosManToVirtualan(JSONObject object) {
    JSONArray virtualanArry = new JSONArray();
    if (object != null) {
      JSONArray arr = object.getJSONArray("item");
      if (arr != null && !arr.isEmpty()) {
        for (int i = 0; i < arr.length(); i++) {
          JSONArray responseArray = arr.getJSONObject(i).getJSONArray("response");
          if (responseArray != null && !responseArray.isEmpty()) {
            for (int j = 0; j < responseArray.length(); j++) {
              JSONObject virtualanObj = new JSONObject();
              virtualanObj.put("scenario", responseArray.optJSONObject(j).optString("name"));
              virtualanObj.put("method",
                  responseArray.optJSONObject(j).getJSONObject("originalRequest")
                      .optString("method"));
              virtualanObj.put("url",
                  buildEndPointURL(responseArray.optJSONObject(j).getJSONObject("originalRequest")
                      .getJSONObject("url").optJSONArray("path")));
              virtualanObj.put("input",
                  responseArray.optJSONObject(j).getJSONObject("originalRequest")
                      .optJSONObject("body").optString("raw"));
              virtualanObj.put("output", responseArray.optJSONObject(j).optString("body"));
              virtualanObj.put("httpStatusCode", responseArray.optJSONObject(j).optString("code"));
              JSONArray paramsArray = new JSONArray();
              addParams(responseArray.optJSONObject(j).getJSONObject("originalRequest")
                  .optJSONArray("header"), paramsArray, "HEADER_PARAM");
              addParams(responseArray.optJSONObject(j).getJSONObject("originalRequest")
                  .optJSONObject("url").optJSONArray("query"), paramsArray, "QUERY_PARAM");
              virtualanObj.put("resource",  getResource(responseArray.optJSONObject(j).getJSONObject("originalRequest")
                  .getJSONObject("url").optJSONArray("path")));
              if (paramsArray.length() > 0) {
                virtualanObj.put("availableParams", paramsArray);
              }
              virtualanArry.put(virtualanObj);
            }
          }
        }
      }
    }
    return virtualanArry;
  }

  public static String getResource( JSONArray inputJsonArray) {
    StringBuilder builder = new StringBuilder();

    if (inputJsonArray != null && !inputJsonArray.isEmpty()) {
      for(int i=0; i < inputJsonArray.length(); i++) {
        return inputJsonArray.getString(i);
      }
    } else {
      builder.append("/");
    }
    return "default";
  }

  public static String buildEndPointURL( JSONArray inputJsonArray) {
    StringBuilder builder = new StringBuilder();

    if (inputJsonArray != null && !inputJsonArray.isEmpty()) {
      for(int i=0; i < inputJsonArray.length(); i++) {
        builder.append("/");
        builder.append(inputJsonArray.getString(i));
      }
    } else {
      builder.append("/");
    }
    return builder.toString();
  }

 public static void addParams(JSONArray inputJsonArray, JSONArray outputJsonArray, String param) {
    if (inputJsonArray != null && !inputJsonArray.isEmpty()) {
      for (int j = 0; j < inputJsonArray.length(); j++) {
        JSONObject virtualanObjParam = new JSONObject();
        virtualanObjParam.put("key", inputJsonArray.getJSONObject(j).optString("key"));
        virtualanObjParam.put("value", inputJsonArray.getJSONObject(j).optString("value"));
        virtualanObjParam.put("parameterType", param);
        outputJsonArray.put(virtualanObjParam);
      }
    }

  }

  private static List<Item> createFeatureFile(JSONArray arr) {
    List<Item> result = new ArrayList<>();
    if (arr != null && !arr.isEmpty()) {
      for (int i = 0; i < arr.length(); i++) {
        Item item = new Item();
        item.setInput(arr.optJSONObject(i).optString("input"));
        if (item.getInput() != null && !"".equalsIgnoreCase(item.getInput())) {
          item.setInputJsonMap(Mapson.buildMAPsonFromJson(item.getInput()));
        }

        item.setOutput(arr.optJSONObject(i).optString("output"));
        if (item.getOutput() != null && !"".equalsIgnoreCase(item.getOutput())) {
          item.setOutputJsonMap(Mapson.buildMAPsonFromJson(item.getOutput()));
        }

        item.setHttpStatusCode(arr.optJSONObject(i).optString("httpStatusCode"));
        item.setMethod(arr.optJSONObject(i).optString("method"));
        item.setAction(arr.optJSONObject(i).optString("method").toLowerCase());
        item.setUrl(arr.optJSONObject(i).optString("url"));
        item.setResource(arr.optJSONObject(i).optString("resource"));
        item.setScenario(arr.optJSONObject(i).optString("scenario"));
        List<AvailableParam> availableParams = new ArrayList<>();
        JSONArray params = arr.optJSONObject(i).optJSONArray("availableParams");
        if (params != null && !params.isEmpty()) {
          for (int j = 0; j < params.length(); j++) {
            availableParams.add(new AvailableParam(params.optJSONObject(j).optString("key"),
                params.optJSONObject(j).optString("value"),
                params.optJSONObject(j).optString("parameterType")));
          }
        }
        item.setAvailableParams(availableParams);
        result.add(item);
      }
    }
    return result;
  }

  public static JSONObject getJSONObject(String contractFileName) {
    JSONObject jsonObject = null;
    try {
      String objectStr = new String(Files.readAllBytes(Paths.get(contractFileName)),
          StandardCharsets.UTF_8);
      jsonObject = new JSONObject(objectStr);
    } catch (IOException e)  {
      LOGGER
          .warning("Unable to process the input file(" + contractFileName + ")" + e.getMessage());
      System.exit(1);
    }
    return  jsonObject;
  }

  public static JSONArray getJSONArray(String contractFileName) {
    JSONArray jsonArray = null;
    try {
      String jsonArrayStr = new String(Files.readAllBytes(Paths.get(contractFileName)),
          StandardCharsets.UTF_8);
      jsonArray = new JSONArray(jsonArrayStr);
    } catch (IOException e)  {
      LOGGER
          .warning("Unable to process the input file(" + contractFileName + ")" + e.getMessage());
      System.exit(1);
    }
    return  jsonArray;
  }
}
