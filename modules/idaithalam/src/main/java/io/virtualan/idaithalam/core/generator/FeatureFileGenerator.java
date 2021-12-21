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

import io.virtualan.idaithalam.contract.ExecutionClassloader;
import io.virtualan.idaithalam.contract.IdaithalamExecutor;
import io.virtualan.idaithalam.contract.VirtualanClassLoader;
import io.virtualan.idaithalam.core.UnableToProcessException;
import io.virtualan.idaithalam.core.domain.*;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Cucumber feature file generator
 */
@Slf4j
public class FeatureFileGenerator {


    private FeatureFileGenerator() {

    }


    private static ExecutionClassloader addConfToClasspath(VirtualanClassLoader classLoader, String path) throws MalformedURLException {
        path = path == null ? "conf" : path;
        ExecutionClassloader cl = new ExecutionClassloader(new URL[] {new File(path).toURI().toURL()}, classLoader);
        Thread.currentThread().setContextClassLoader(cl);
        return cl;
    }

    /**
     * Read props from classpath properties.
     *
     * @param classLoader the class loader
     * @param fileName    the file name
     * @return the properties
     */
    public static Properties readPropsFromClasspath(ClassLoader classLoader, String fileName) {
        Properties propertiesForInstance = new Properties();

        try {
            InputStream stream = classLoader.getResourceAsStream(fileName);
            if (stream != null) {
                propertiesForInstance.load(stream);
            } else {
                log.warn("unable to load "+ fileName);
            }
        } catch (Exception var3) {
            log.warn(fileName +" not found");
        }

        return propertiesForInstance;
    }


    /**
     * Generate feature file list.
     *
     * @param properties       the properties
     * @param apiExecutorParam the api executor param
     * @return the list
     * @throws UnableToProcessException the unable to process exception
     * @throws IOException              the io exception
     */
    public static List<FeatureFileMapper> generateFeatureFile(Properties properties, ApiExecutorParam apiExecutorParam)
        throws UnableToProcessException, IOException {
        List<FeatureFileMapper> items = new ArrayList<>();
        VirtualanClassLoader classLoaderParnet = new VirtualanClassLoader(
            IdaithalamExecutor.class.getClassLoader());
        ExecutionClassloader classLoader = addConfToClasspath(classLoaderParnet, apiExecutorParam.getOutputDir());
        Map<String, String> excludeConfiguration = (Map)readPropsFromClasspath(classLoader, "exclude-response.properties");
        String contractFileName = properties.getProperty("virtualan.data.load");
        String contractFileType = properties.getProperty("virtualan.data.type");
        JSONArray jsonArray = null;
        if (contractFileType == null) {
            log.warn("provide appropriate virtualan.data.type for the input data?");
            throw new UnableToProcessException("provide appropriate virtualan.data.type for the input data?");
        }
        String[] fileNames = contractFileName.split(";");

        for(int i=0; i < fileNames.length; i++) {
          if (ConversionType.POSTMAN.name().equalsIgnoreCase(contractFileType)) {
            jsonArray = FeatureGenerationHelper
                .createPostManToVirtualan(getJSONObject(classLoader, fileNames[i]));
          } else if (ConversionType.OPENAPI.name().equalsIgnoreCase(contractFileType)) {
            jsonArray = OpenApiFeatureFileGenerator
                .generateOpenApiContractForVirtualan(fileNames[i]);
          } else {
            jsonArray = getJSONArray(apiExecutorParam, fileNames[i]);
          }
          List<Item> result = FeatureGenerationHelper.createFeatureFile(excludeConfiguration, jsonArray, apiExecutorParam.getOutputDir());
          /** Author: oglas  Add custom API header from configuration yaml. */
          if (apiExecutorParam.getApiHeader() != null && apiExecutorParam.getApiHeader().getHeaderList().size() > 0) {
              addCustomApiHeader(apiExecutorParam, result);
          }
          FeatureFileMapper featureFileMapper = new  FeatureFileMapper(fileNames[i], result);
          items.add(featureFileMapper);
        }
        return items;
    }

    /** Author: Oliver Glas (inss.ch) 
     * Adding custom API header as defined in the yaml file. */
    private static void addCustomApiHeader(ApiExecutorParam apiExecutorParam, List<Item> result) {
        List<Map<String, Object>> apiHeaderList = apiExecutorParam.getApiHeader().getHeaderList();
        boolean overwrite = true;  // Default value shall be true. 
        if (apiExecutorParam.getApiHeader().getOverwrite() != null) {
            overwrite = Boolean.valueOf(apiExecutorParam.getApiHeader().getOverwrite());
        }
        for (Item item : result) {
            List<AvailableParam> availableParamList = new ArrayList<>();
            if (item.getAvailableParams() == null) {
                item.setAvailableParams(availableParamList);
            }
            if (item.getHeaderParams() == null) {
                item.setHeaderParams(availableParamList);
            }
            for (Map<String, Object> map : apiHeaderList) {
                for (String key : map.keySet()) {
                    AvailableParam newAvailableParam = new AvailableParam(key, map.get(key).toString(), "HEADER_PARAM");
                    if (!item.getAvailableParams().contains(newAvailableParam)) {
                        item.getAvailableParams().add(newAvailableParam);
                        item.getHeaderParams().add(newAvailableParam);
                    } else if (overwrite) { //if it exists already, overwrite it
                        for (AvailableParam availableParam1 : item.getAvailableParams()) {
                            if (availableParam1.getKey().equals(newAvailableParam.getKey())) {
                                availableParam1.setKey(newAvailableParam.getKey());
                                availableParam1.setValue(newAvailableParam.getValue());
                                availableParam1.setParameterType(newAvailableParam.getParameterType());
                                log.warn("Due to issue #121 API header " + key + " is overwritten with value from configuration. To avoid this behavior add 'overwrite: false' (default: true) to the 'apiHeader' section.");
                            }
                        }
                    } else {
                        log.warn("Due to issue #121 adding duplicate api header can cause errors.");
                        //TODO Due to issue #121 there cannot be added a duplicate api header..
                    }
                }
            }
        }
    }


    /**
     * Gets json object.
     *
     * @param classLoader      the class loader
     * @param contractFileName the contract file name
     * @return the json object
     * @throws UnableToProcessException the unable to process exception
     */
    public static JSONObject getJSONObject(ClassLoader classLoader, String contractFileName)
        throws UnableToProcessException {
        JSONObject jsonObject = null;
        try {
            InputStream stream = classLoader.getResourceAsStream(contractFileName);
            if(stream != null){
                String objectStr = readString(stream);
                jsonObject = new JSONObject(objectStr);
            } else if(FeatureFileGenerator.class.getClassLoader().getResourceAsStream(contractFileName) != null) {
                String objectStr = readString(FeatureFileGenerator.class.getClassLoader()
                    .getResourceAsStream(contractFileName));
                jsonObject = new JSONObject(objectStr);
            } else {
                throw new UnableToProcessException("Unable to find/process the input file(" + contractFileName + ") :" );
            }
        } catch (IOException e) {
            log.warn("Unable to process the input file(" + contractFileName + ")" + e.getMessage());
            throw new UnableToProcessException("Unable to process the input file(" + contractFileName + ") :" + e.getMessage());
        }
        return jsonObject;
    }

    /**
     * Read string string.
     *
     * @param inputStream the input stream
     * @return the string
     * @throws IOException the io exception
     */
    public static String readString(InputStream inputStream) throws IOException {

        ByteArrayOutputStream into = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        for (int n; 0 < (n = inputStream.read(buf));) {
            into.write(buf, 0, n);
        }
        into.close();
        return new String(into.toByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * Gets json array.
     *
     * @param apiExecutorParam the api executor param
     * @param contractFileName the contract file name
     * @return the json array
     * @throws UnableToProcessException the unable to process exception
     */
    public static JSONArray getJSONArray(ApiExecutorParam apiExecutorParam, String contractFileName)
        throws UnableToProcessException {
        JSONArray jsonArray = null;
        try {
            String jsonArrayStr = getFileAsString(apiExecutorParam, contractFileName);
            jsonArray = new JSONArray(jsonArrayStr);
        } catch (IOException e) {
            log.warn("Unable to process the input file(" + contractFileName + ")" + e.getMessage());
            throw new UnableToProcessException("Unable to process the input file(" + contractFileName + ")" + e.getMessage());
        }
        return jsonArray;
    }

    private static String getFileAsString(ApiExecutorParam apiExecutorParam, String filePath)
        throws IOException {
        InputStream stream  = null;
        File file = new File(filePath);
        if (apiExecutorParam.getVirtualanSpecPath() != null) {
            stream = new FileInputStream(apiExecutorParam.getVirtualanSpecPath() +File.separator+ filePath);
        }
        if(stream == null && file.exists()){
            stream = new FileInputStream(file);
        }
        if (stream == null) {
            stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        }
        if (stream == null) {
            stream = ExcelToCollectionGenerator.class.getClassLoader().getResourceAsStream(filePath);
        }

        return convertStreamToString(stream);
    }

    private static String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                String line;
                while((line = reader.readLine()) != null) {
                    if(!line.trim().equalsIgnoreCase("")) {
                        sb.append(line).append("\n");
                    }
                }
            } finally {
                is.close();
            }

            return sb.toString();
        } else {
            return null;
        }
    }


}
