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

package io.virtualan.idaithalan.core.contract.validator;

import io.virtualan.cucumblan.props.ApplicationConfiguration;
import io.virtualan.idaithalan.core.domain.ConversionType;
import io.virtualan.idaithalan.core.domain.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 *   Cucumber feature file generator
 *
 */

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
        } else if (ConversionType.POSTMAN.name().equalsIgnoreCase(contractFileType)) {
            jsonArray = FeatureGenerationHelper.createPosManToVirtualan(getJSONObject(contractFileName));
        } else {
            jsonArray = getJSONArray(contractFileName);
        }
        List<Item> result = FeatureGenerationHelper.createFeatureFile(jsonArray);
        return result;
    }

    public static JSONObject getJSONObject(String contractFileName) {
        JSONObject jsonObject = null;
        try {
            String objectStr = readString(FeatureFileGenerator.class.getClassLoader().getResourceAsStream(contractFileName));
            jsonObject = new JSONObject(objectStr);
        } catch (IOException e) {
            LOGGER
                    .warning("Unable to process the input file(" + contractFileName + ")" + e.getMessage());
            System.exit(-1);
        }
        return jsonObject;
    }

    public static String readString(InputStream inputStream) throws IOException {

        ByteArrayOutputStream into = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        for (int n; 0 < (n = inputStream.read(buf));) {
            into.write(buf, 0, n);
        }
        into.close();
        return new String(into.toByteArray(), "UTF-8"); // Or whatever encoding
    }

    public static JSONArray getJSONArray(String contractFileName) {
        JSONArray jsonArray = null;
        try {
            String jsonArrayStr = readString(FeatureFileGenerator.class.getClassLoader().getResourceAsStream(contractFileName));
            jsonArray = new JSONArray(jsonArrayStr);
        } catch (IOException e) {
            LOGGER
                    .warning("Unable to process the input file(" + contractFileName + ")" + e.getMessage());
            System.exit(-1);
        }
        return jsonArray;
    }
}