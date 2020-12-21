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

import io.virtualan.cucumblan.props.ExcludeConfiguration;
import io.virtualan.idaithalam.core.domain.AvailableParam;
import io.virtualan.idaithalam.core.domain.Item;
import io.virtualan.mapson.Mapson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONTokener;

public class FeatureGenerationHelper {

    private FeatureGenerationHelper(){}

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
                if (inputJsonArray.getJSONObject(j) instanceof  JSONObject &&
                                !"".equalsIgnoreCase(inputJsonArray.getJSONObject(j).optString("key"))) {
                    virtualanObjParam.put("key", inputJsonArray.getJSONObject(j).optString("key"));
                    virtualanObjParam.put("value", inputJsonArray.getJSONObject(j).optString("value"));
                    virtualanObjParam.put("parameterType", param);
                    outputJsonArray.put(virtualanObjParam);
                }
            }
        }

    }

    public static JSONArray createPostManToVirtualan(JSONObject object) {
        JSONArray virtualanArry = new JSONArray();
        if (object != null) {
            JSONArray arr = object.getJSONArray("item");
            if (arr != null && arr.length() > 0) {
                for (int i = 0; i < arr.length(); i++) {
                    buildVirtualanFromPostMan(virtualanArry, arr, i);
                }
            }
        }
        return virtualanArry;
    }

    private static void buildVirtualanFromPostMan(JSONArray virtualanArry, JSONArray arr, int i) {
        if(arr.optJSONObject(i) instanceof JSONObject) {
            if(arr.optJSONObject(i).optJSONArray("response") instanceof  JSONArray) {
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

    private static JSONObject buildVirtualanObject(JSONArray responseArray, int j) {
        JSONObject virtualanObj = new JSONObject();
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

    private static void extractedParams(JSONArray responseArray, int j, JSONObject virtualanObj, JSONArray paramsArray) {
        addParams(responseArray.optJSONObject(j).optJSONObject("originalRequest")
                .optJSONArray("header"), paramsArray, "HEADER_PARAM");
        addParams(responseArray.optJSONObject(j).optJSONObject("originalRequest")
                .optJSONObject("url").optJSONArray("query"), paramsArray, "QUERY_PARAM");
        virtualanObj.put("resource", getResource(responseArray.optJSONObject(j).optJSONObject("originalRequest")
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


    public static List<Item> createFeatureFile(JSONArray arr) {
        List<Item> result = new ArrayList<>();
        if (arr != null && arr.length() > 0) {
            for (int i = 0; i < arr.length(); i++) {
                Item item = getItem(arr, i);
                result.add(item);
            }
        }
        return result;
    }

    private static Item getItem(JSONArray arr, int i) {
        Item item = new Item();
        extractedInput(arr, i, item);
        item.setUrl(arr.optJSONObject(i).optString("url"));
        extractedOutput(item.getUrl(), arr, i, item);
        item.setHttpStatusCode(arr.optJSONObject(i).optString("httpStatusCode"));
        item.setMethod(arr.optJSONObject(i).optString("method"));
        item.setAction(arr.optJSONObject(i).optString("method").toLowerCase());
        item.setResource(arr.optJSONObject(i).optString("resource"));
        extractedScenario(arr, i, item);
        List<AvailableParam> availableParams = getAvailableParamList(arr, i);
        item.setAvailableParams(availableParams);
        return item;
    }

    private static void extractedScenario(JSONArray arr, int i, Item item) {
        if ("".equalsIgnoreCase(arr.optJSONObject(i).optString("scenario"))) {
            item.setScenario(arr.optJSONObject(i).optString("operationId"));
        } else {
            item.setScenario(arr.optJSONObject(i).optString("scenario"));
        }
    }

    private static void extractedOutput(String url, JSONArray arr, int i, Item item) {
        item.setOutput(arr.optJSONObject(i).optString("output"));
        if (item.getOutput() != null && !"".equalsIgnoreCase(item.getOutput())) {
            try {
                JSONTokener jsonTokener = new JSONTokener(item.getOutput());
                new JSONObject(jsonTokener);
                item.setOutputJsonMap(Mapson.buildMAPsonFromJson(item.getOutput()));
                if(!ExcludeConfiguration.shouldSkip(url, null)) {
                    item.setHasOutputJsonMap(true);
                }
            } catch (JSONException e) {
                item.setStdOutput(item.getOutput());
            }
        }
    }


    private static void extractedInput(JSONArray arr, int i, Item item) {
        item.setInput(arr.optJSONObject(i).optString("input"));
        if (item.getInput() != null && !"".equalsIgnoreCase(item.getInput())) {
            try {
                JSONTokener jsonTokener = new JSONTokener(item.getInput());
                new JSONObject(jsonTokener);
                item.setInputJsonMap(Mapson.buildMAPsonFromJson(item.getInput()));
                item.setHasInputJsonMap(true);
            } catch (JSONException e) {
                item.setStdInput(item.getInput());
            }
        }
    }


    private static List<AvailableParam> getAvailableParamList(JSONArray arr, int i) {
        List<AvailableParam> availableParams = new ArrayList<>();
        JSONArray params = arr.optJSONObject(i).optJSONArray("availableParams");
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
