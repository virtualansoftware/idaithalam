package io.virtualan.idaithalam.core.generator;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.oas.inflector.examples.ExampleBuilder;
import io.swagger.oas.inflector.examples.models.Example;
import io.swagger.oas.inflector.processors.JsonNodeExampleSerializer;
import io.swagger.parser.Swagger20Parser;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.domain.OperationBuilder;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * The type Open api feature file generator.
 */
@Slf4j
public class OpenApiFeatureFileGenerator {

    /**
     * Generate open api contract for virtualan json array.
     *
     * @param contractFileName the contract file name
     * @return the json array
     */
    public static JSONArray generateOpenApiContractForVirtualan(String contractFileName, ApiExecutorParam apiExecutorParam) {
        JSONArray openApiContractArray = new JSONArray();
        OpenAPI swagger = new OpenAPIV3Parser().read(contractFileName);
        if (swagger == null) {
            try {
                Swagger swagger2 = (new Swagger20Parser()).read(contractFileName, null);
                Map<String, Path> path2s = swagger2.getPaths();
                if (path2s != null && path2s.size() > 0) {
                    log.warn("Swagger 2 not supported");
                }
            } catch (Exception e) {
            }
        }
        if (swagger != null) {
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
                    JSONObject virtualanObj = getOperationDelete(new OperationBuilder(url, definitions, mapPath.getValue(), operationDelete));
                    openApiContractArray.put(virtualanObj);
                }
                Operation operationPut = mapPath.getValue().getPut();
                if (operationPut != null) {
                    JSONObject virtualanObj = getOperationPut(new OperationBuilder(url, definitions, mapPath.getValue(), operationPut));
                    openApiContractArray.put(virtualanObj);
                }
                Operation operationPatch = mapPath.getValue().getPatch();
                if (operationPatch != null) {
                    JSONObject virtualanObj = getOperationPatch(new OperationBuilder(url, definitions, mapPath.getValue(), operationPatch));
                    openApiContractArray.put(virtualanObj);
                }
            }
        }
        generateProviderJson(openApiContractArray, contractFileName.substring(0, contractFileName.lastIndexOf(".")), apiExecutorParam);
        return openApiContractArray;
    }

    private static void generateProviderJson(JSONArray openApiContractArray, String contractFileName, ApiExecutorParam apiExecutorParam) {
        try (FileOutputStream outputStream = new FileOutputStream(apiExecutorParam.getOutputDir() + File.separator + contractFileName + ".json")) {
            Writer writer = new OutputStreamWriter(outputStream);
            CharSequence cs = openApiContractArray.toString(2);
            writer.append(cs);
            writer.close();
        } catch (IOException e) {
            log.warn(" Unable to generate Virtualan Provider JSON : " + e.getMessage());
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

    private static void buildRequest(OperationBuilder operationBuilder, JSONObject virtualanObj, JSONArray paramsArray) {
        if (operationBuilder.getOperation().getRequestBody() != null) {
            if (operationBuilder.getOperation().getRequestBody().getContent() != null) {
                String contentType = operationBuilder.getOperation().getRequestBody().getContent().keySet().stream().findFirst().get();
                virtualanObj.put("contentType", contentType);
                paramsArray.put(buildParam("contentType", contentType, "HEADER_PARAM"));
                if ("application/x-www-form-urlencoded".equalsIgnoreCase(contentType)) {
                    Map.Entry<String, MediaType> entryMedia = operationBuilder.getOperation().getRequestBody().getContent().entrySet().iterator().next();
                    if ("object".equalsIgnoreCase(entryMedia.getValue().getSchema().getType())) {
                        Map<String, Schema> properties = entryMedia.getValue().getSchema().getProperties();
                        properties.forEach((x, y) ->
                                paramsArray.put(buildParam(x, y.getExample() != null ? y.getExample().toString() : "MISSING", "FORM_PARAM")));
                    }
                    paramsArray.put(buildParam("Accept", "*/*", "HEADER_PARAM"));
                } else {
                    virtualanObj.put("input", getJsonForSuccessCase(operationBuilder, operationBuilder.getOperation().getRequestBody().getContent()));
                }
            } else if (operationBuilder.getOperation().getRequestBody().get$ref() != null) {
                String schema = operationBuilder.getOperation().getRequestBody().get$ref();
                if (schema != null) {
                    schema = schema.substring(schema.lastIndexOf("/") + 1);
                    Schema model = operationBuilder.getDefinitions().get(schema);
                    virtualanObj.put("input", buildJson(operationBuilder.getDefinitions(), model));
                }
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
        if (operationBuilder.getOperation().getDescription() != null) {
            virtualanObj.put("scenario", operationBuilder.getOperation().getDescription());
        } else {
            virtualanObj.put("scenario", operationBuilder.getOperation().getOperationId());
        }
        virtualanObj.put("scenarioId", operationBuilder.getOperation().getOperationId());
        virtualanObj.put("method", action);
        virtualanObj.put("type", "Response");
        virtualanObj.put("httpStatusCode", statusCode);
        String url = operationBuilder.getUrl();
        if (operationBuilder.getOperation().getParameters() != null) {
            url = getUrl(operationBuilder);
        }
        virtualanObj.put("url", url);
        virtualanObj.put("resource", getResource(url));
        JSONArray paramsArray = new JSONArray();
        buildRequest(operationBuilder, virtualanObj, paramsArray);
        getSuccessResponse(operationBuilder, virtualanObj, "201");
        extractedParams(operationBuilder, paramsArray);
        virtualanObj.put("availableParams", paramsArray);
        return virtualanObj;
    }

    private static JSONObject buildParam(String key, String value, String type) {
        JSONObject virtualanParamObj = new JSONObject();
        virtualanParamObj.put("key", key);
        virtualanParamObj.put("value", value);
        virtualanParamObj.put("parameterType", type);
        return virtualanParamObj;
    }


    // TODO wrong code
    private static String getJsonForSuccessCase(OperationBuilder operationBuilder, Content content) {
        for (Map.Entry<String, MediaType> entryMedia : content.entrySet()) {
            log.info("Key = " + entryMedia.getKey() +
                    ", Value = " + entryMedia.getValue().getSchema().get$ref());

//            String schema = entryMedia.getValue().getSchema().get$ref();
//            if (schema != null) {
//                schema = schema.substring(schema.lastIndexOf("/") + 1);
//                Schema model = operationBuilder.getDefinitions().get(schema);
//                return buildJson(operationBuilder.getDefinitions(), model);
//            } else  {
            Schema model = entryMedia.getValue().getSchema();
            return buildJson(operationBuilder.getDefinitions(), model);
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
        if (resource.split("/").length > 0) {
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
        if (operationBuilder.getOperation().getDescription() != null) {
            virtualanObj.put("scenario", operationBuilder.getOperation().getDescription());
        } else {
            virtualanObj.put("scenario", operationBuilder.getOperation().getOperationId());
        }
        virtualanObj.put("scenarioId", operationBuilder.getOperation().getOperationId());
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
        virtualanObj.put("availableParams", paramsArray);
        return virtualanObj;
    }

    private static void extractedParams(OperationBuilder operationBuilder, JSONArray paramsArray) {
        if (operationBuilder.getOperation().getParameters() != null) {
            for (Parameter parameter : operationBuilder.getOperation().getParameters()) {
                if (parameter.getName() != null) {
                    JSONObject virtualanParamObj = new JSONObject();
                    virtualanParamObj.put("key", parameter.getName());
                    buildWithDataType(parameter, virtualanParamObj);
                    virtualanParamObj.put("parameterType", getType(parameter.getIn()));
                    paramsArray.put(virtualanParamObj);
                }
            }
        }
    }

    private static void buildWithDataType(Parameter parameter, JSONObject virtualanParamObj) {
        String value = "MISSING";
        if (parameter.getExample() != null) {
            value = parameter.getExample().toString();
        } else if (parameter.getSchema() instanceof IntegerSchema) {
            value = "9991";
        } else if (parameter.getSchema() instanceof NumberSchema) {
            value = "9991.1";
        } else if (parameter.getSchema() instanceof BooleanSchema) {
            value = "false";
        }

        virtualanParamObj.put("value", value);
    }

    private static String getType(String type) {
        if ("path".equalsIgnoreCase(type)) {
            return "PATH_PARAM";
        } else if ("query".equalsIgnoreCase(type)) {
            return "QUERY_PARAM";
        } else if ("header".equalsIgnoreCase(type)) {
            return "HEADER_PARAM";
        }
        return null;
    }

    private static void getSuccessResponse(OperationBuilder operationBuilder, JSONObject virtualanObj, String statusCode) {
        if (operationBuilder.getOperation().getResponses() != null) {
            ApiResponses responses = operationBuilder.getOperation().getResponses();
            Content content = null;
            for (Map.Entry<String, ApiResponse> entry : responses.entrySet()) {
                if (statusCode.equalsIgnoreCase(entry.getKey())) {
                    if (entry.getValue().getContent() != null) {
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
            if (parameter.getExample() != null) {
                url = operationBuilder.getUrl()
                        .replaceAll(parameter.getName(), parameter.getExample().toString());
            }
        }
        return url;
    }


    private static String buildJson(Map<String, Schema> definitions, Schema model) {

        Example example = ExampleBuilder.fromSchema(model, definitions);
        SimpleModule simpleModule = new SimpleModule().addSerializer(
                new JsonNodeExampleSerializer());
        Json.mapper().registerModule(simpleModule);
        String jsonExample = Json.pretty(example);
        log.info(jsonExample);
        return jsonExample;
    }
}
