package io.virtualan.idaithalam.core.contract.validator;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.MediaType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.logging.Logger;

import io.swagger.v3.oas.models.parameters.Parameter;
import io.virtualan.idaithalam.core.domain.OperationBuilder;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The type Open api feature file generator.
 */
@Slf4j
public class OpenApiFeatureFileGenerator {
    private final static Logger LOGGER = Logger.getLogger(OpenApiFeatureFileGenerator.class.getName());

    /**
     * Generate open api contract for virtualan json array.
     *
     * @param contractFileName the contract file name
     * @return the json array
     */
    public static JSONArray generateOpenApiContractForVirtualan(String contractFileName) {
        JSONArray openApiContractArray = new JSONArray();
        OpenAPI swagger = new OpenAPIV3Parser().read(contractFileName);
        if(swagger != null) {
            Map<String, Schema> definitions = swagger.getComponents().getSchemas();
            Paths paths = swagger.getPaths();
            for (Map.Entry<String, PathItem> mapPath : paths.entrySet()) {
                String url = mapPath.getKey();
                Operation operationGet = mapPath.getValue().getGet();
                if (operationGet != null) {
                    JSONObject virtualanObj = getOperationGet(new OperationBuilder(url, definitions, mapPath.getValue(), operationGet));
                    openApiContractArray.put(virtualanObj);
                }
                Operation operationPost = mapPath.getValue().getPost();
                if (operationPost != null) {
                    JSONObject virtualanObj = getOperationPost(new OperationBuilder(url, definitions, mapPath.getValue(), operationPost));
                    openApiContractArray.put(virtualanObj);
                }
                Operation operationDelete = mapPath.getValue().getDelete();
                if (operationDelete != null) {
                    JSONObject virtualanObj = getOperationDelete(new OperationBuilder(url, definitions, mapPath.getValue(), operationPost));
                    openApiContractArray.put(virtualanObj);
                }
                Operation operationPut = mapPath.getValue().getPut();
                if (operationPut != null) {
                    JSONObject virtualanObj = getOperationPut(new OperationBuilder(url, definitions, mapPath.getValue(), operationPost));
                    openApiContractArray.put(virtualanObj);
                }
                Operation operationPatch = mapPath.getValue().getPatch();
                if (operationPatch != null) {
                    JSONObject virtualanObj = getOperationPatch(new OperationBuilder(url, definitions, mapPath.getValue(), operationPost));
                    openApiContractArray.put(virtualanObj);
                }
            }
        }
        generateProviderJson(openApiContractArray);
        return openApiContractArray;
    }

    private static void generateProviderJson(JSONArray openApiContractArray)  {
        try {
            FileOutputStream outputStream = new FileOutputStream("conf/virtualan-provider.json");
            Writer writer = new OutputStreamWriter(outputStream);
            CharSequence cs = openApiContractArray.toString();
            writer.append(cs);
            writer.close();
        }catch (IOException e){
            LOGGER.warning(" Unable to generate Virtualan Provider JSON : "+e.getMessage());
        }
    }


    /**
     * Gets operation put.
     *
     * @param operationBuilder the operation builder
     * @return the operation put
     */
    static JSONObject getOperationPut(OperationBuilder operationBuilder) {
        return getOperationChange(operationBuilder, "PUT", "200");
    }

    /**
     * Gets operation patch.
     *
     * @param operationBuilder the operation builder
     * @return the operation patch
     */
    static JSONObject getOperationPatch(OperationBuilder operationBuilder) {
        return getOperationChange(operationBuilder, "PATCH", "200");
    }

    private static void buildRequest(OperationBuilder operationBuilder, JSONObject virtualanObj) {
        if (operationBuilder.getOperation().getRequestBody() != null) {
            if (operationBuilder.getOperation().getRequestBody().getContent() != null) {
                virtualanObj.put("input", getJsonForSuccessCase(operationBuilder, operationBuilder.getOperation().getRequestBody().getContent()));
            }
        }
    }

    /**
     * Gets operation post.
     *
     * @param operationBuilder the operation builder
     * @return the operation post
     */
    static JSONObject getOperationPost(OperationBuilder operationBuilder) {
        return getOperationChange(operationBuilder, "POST", "201");
    }

    /**
     * Gets operation change.
     *
     * @param operationBuilder the operation builder
     * @param action           the action
     * @param statusCode       the status code
     * @return the operation change
     */
    static JSONObject getOperationChange(OperationBuilder operationBuilder, String action, String statusCode) {
        JSONObject virtualanObj = new JSONObject();
        virtualanObj.put("scenario", operationBuilder.getOperation().getDescription());
        virtualanObj.put("method", action);
        virtualanObj.put("type", "Response");
        virtualanObj.put("httpStatusCode", statusCode);
        String url = operationBuilder.getUrl();
        if (operationBuilder.getOperation().getParameters() != null) {
            url = getUrl(operationBuilder);
        }
        virtualanObj.put("url", url);
        virtualanObj.put("resource", getResource(url));
        buildRequest(operationBuilder, virtualanObj);
        getSuccessResponse(operationBuilder, virtualanObj, "201");
        JSONArray paramsArray = new JSONArray();
        extractedParams(operationBuilder, paramsArray);
        virtualanObj.put("availableParams",paramsArray);
        return virtualanObj;
    }

    private static String getJsonForSuccessCase(OperationBuilder operationBuilder, Content content) {
        Map.Entry<String, MediaType> entryMediaDefault = null;
        for (Map.Entry<String, MediaType> entryMedia : content.entrySet()) {
            System.out.println("Key = " + entryMedia.getKey() +
                ", Value = " + entryMedia.getValue().getSchema().get$ref());
            String schema = entryMedia.getValue().getSchema().get$ref();
            schema = schema.substring(schema.lastIndexOf("/") + 1);
            return buildJson(operationBuilder.getDefinitions(), schema);
        }
        return null;
    }

    /**
     * Gets operation delete.
     *
     * @param operationBuilder the operation builder
     * @return the operation delete
     */
    static JSONObject getOperationDelete(OperationBuilder operationBuilder) {
        return getOperation(operationBuilder, "DELETE");
    }

    /**
     * Gets operation get.
     *
     * @param operationBuilder the operation builder
     * @return the operation get
     */
    static JSONObject getOperationGet(OperationBuilder operationBuilder) {
        return getOperation(operationBuilder, "GET");
    }

    private static String getResource(String resource) {
        if (resource.split("/").length >0) {
            return resource.split("/")[1];
        }
        return "default";
    }

    /**
     * Gets operation.
     *
     * @param operationBuilder the operation builder
     * @param action           the action
     * @return the operation
     */
    static JSONObject getOperation(OperationBuilder operationBuilder, String action) {
        JSONObject virtualanObj = new JSONObject();
        virtualanObj.put("scenario", operationBuilder.getOperation().getDescription());
        virtualanObj.put("method", action);
        virtualanObj.put("type", "Response");
        virtualanObj.put("httpStatusCode", "200");
        String url = operationBuilder.getUrl();
        if (operationBuilder.getOperation().getParameters() != null) {
            url = getUrl(operationBuilder);
        }
        virtualanObj.put("url", url);
        virtualanObj.put("resource", getResource(url));
        getSuccessResponse(operationBuilder, virtualanObj, "200");
        JSONArray paramsArray = new JSONArray();
        extractedParams(operationBuilder, paramsArray);
        virtualanObj.put("availableParams",paramsArray);
        return virtualanObj;
    }

    private static void extractedParams(OperationBuilder operationBuilder,  JSONArray paramsArray) {
        if(operationBuilder.getOperation().getParameters() != null) {
            for (Parameter parameter : operationBuilder.getOperation().getParameters()) {
                if (parameter.getExample() != null && parameter.getName() != null) {
                    JSONObject virtualanParamObj = new JSONObject();
                    virtualanParamObj.put("key", parameter.getName());
                    virtualanParamObj.put("value", parameter.getExample().toString());
                    virtualanParamObj.put("parameterType", getType(parameter.getIn()));
                    paramsArray.put(virtualanParamObj);
                }
            }
        }
    }

    private static String getType(String type) {
        if ("path".equalsIgnoreCase(type)) {
            return "PATH_PARAM";
        } else if ("query".equalsIgnoreCase(type)) {
            return "QUERY_PARAM";
        }else if ("header".equalsIgnoreCase(type)){
            return "HEADER_PARAM";
        }
        return null;
    }

    private static void getSuccessResponse(OperationBuilder operationBuilder, JSONObject virtualanObj, String statusCode) {
        if (operationBuilder.getOperation().getResponses() != null) {
            ApiResponses responses = operationBuilder.getOperation().getResponses();
            Content content = null;
            for (Map.Entry<String, ApiResponse> entry : responses.entrySet()) {
                System.out.println("Key = " + entry.getKey());
                if (statusCode.equalsIgnoreCase(entry.getKey())) {
                    if(entry.getValue().getContent() != null) {
                        virtualanObj.put("output", getJsonForSuccessCase(operationBuilder, entry.getValue().getContent()));
                    }
                    return;
                } else if ("Default".equalsIgnoreCase(entry.getKey())) {
                    content = entry.getValue().getContent();
                }
            }
            if (content != null) {
                virtualanObj.put("output", getJsonForSuccessCase(operationBuilder, content));

            }
        }
    }

    private static String getUrl(OperationBuilder operationBuilder) {
        String url = operationBuilder.getUrl();
        for (Parameter parameter : operationBuilder.getOperation().getParameters()) {
            if(parameter.getExample() != null) {
                url = operationBuilder.getUrl()
                    .replaceAll(parameter.getName(), parameter.getExample().toString());
            }
        }
        return url;
    }



    private static String buildJson(Map<String, Schema> definitions, String schema) {
//        Schema model = definitions.get(schema);
//        Example example = ExampleBuilder.fromSchema(model, definitions);
//        SimpleModule simpleModule = new SimpleModule().addSerializer(
//            new JsonNodeExampleSerializer());
//        Json.mapper().registerModule(simpleModule);
//        String jsonExample = Json.pretty(example);
//       log.info(jsonExample);
        return "";
    }
}
