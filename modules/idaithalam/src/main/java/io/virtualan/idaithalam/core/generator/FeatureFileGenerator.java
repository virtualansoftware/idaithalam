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
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.domain.ConversionType;
import io.virtualan.idaithalam.core.domain.Item;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

/**
 * Cucumber feature file generator
 */
public class FeatureFileGenerator {

    final private static Logger LOGGER = Logger.getLogger(FeatureFileGenerator.class.getName());

    private FeatureFileGenerator() {

    }


    private static ExecutionClassloader addConfToClasspath(VirtualanClassLoader classLoader, String path) throws MalformedURLException {
        path = path == null ? "conf" : path;
        ExecutionClassloader cl = new ExecutionClassloader(new URL[] {new File(path).toURI().toURL()}, classLoader);
        Thread.currentThread().setContextClassLoader(cl);
        return cl;
    }

    public static Properties readPropsFromClasspath(ClassLoader classLoader, String fileName) {
        Properties propertiesForInstance = new Properties();

        try {
            InputStream stream = classLoader.getResourceAsStream(fileName);
            if (stream != null) {
                propertiesForInstance.load(stream);
            } else {
                LOGGER.warning("unable to load "+ fileName);
            }
        } catch (Exception var3) {
            LOGGER.warning(fileName +" not found");
        }

        return propertiesForInstance;
    }


    /**
     * Generate feature file list.
     *
     * @return the list
     * @throws UnableToProcessException the unable to process exception
     * @throws IOException              the io exception
     */
    public static List<List<Item>> generateFeatureFile(Properties properties, ApiExecutorParam apiExecutorParam)
        throws UnableToProcessException, IOException {
        List<List<Item>> items = new ArrayList<>();
        VirtualanClassLoader classLoaderParnet = new VirtualanClassLoader(
            IdaithalamExecutor.class.getClassLoader());
        ExecutionClassloader classLoader = addConfToClasspath(classLoaderParnet, apiExecutorParam.getOutputDir());
        Map<String, String> excludeConfiguration = (Map)readPropsFromClasspath(classLoader, "exclude-response.properties");
        String contractFileName = properties.getProperty("virtualan.data.load");
        String contractFileType = properties.getProperty("virtualan.data.type");
        JSONArray jsonArray = null;
        if (contractFileType == null) {
            LOGGER.severe("provide appropriate virtualan.data.type for the input data?");
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
          items.add(result);
        }
        return items;
    }


    /**
     * Gets json object.
     *
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
            LOGGER
                .warning("Unable to process the input file(" + contractFileName + ")" + e.getMessage());
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
            LOGGER
                    .warning("Unable to process the input file(" + contractFileName + ")" + e.getMessage());
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
