package io.virtualan.idaithalam.core.generator;

import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;

import io.virtualan.idaithalam.config.IdaithalamConfiguration;
import io.virtualan.idaithalam.core.IdaithalamConstants;
import io.virtualan.idaithalam.core.UnableToProcessException;
import io.virtualan.idaithalam.core.domain.ApiExecutorParam;
import io.virtualan.idaithalam.core.domain.CreateFileInfo;
import io.virtualan.idaithalam.core.domain.SheetObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import io.virtualan.idaithalam.exception.IdaithalamException;
import io.virtualan.idaithalam.exception.MandatoryFieldMissingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;


/**
 * The type Excel to collection generator.
 */
@Slf4j
public class ExcelToCollectionGenerator {

   private static final List<String> REST_MANDATORY_FIELDS = new ArrayList<>(Arrays.asList(IdaithalamConstants.TESTCASE_NAME_HEADER,
          IdaithalamConstants.TESTCASE_NAME_DESC_HEADER,
          IdaithalamConstants.ACTION_HEADER,
          IdaithalamConstants.STATUS_CODE_HEADER,
          IdaithalamConstants.URL_HEADER,
          IdaithalamConstants.CONTENT_TYPE_HEADER,
          IdaithalamConstants.RESOURCE_HEADER));
  private static final List<String> DB_MANDATORY_FIELDS = new ArrayList<>(Arrays.asList(IdaithalamConstants.TESTCASE_NAME_HEADER,
          IdaithalamConstants.TESTCASE_NAME_DESC_HEADER, IdaithalamConstants.RESOURCE_HEADER));
  private static final List<String> KAFKA_MANDATORY_FIELDS = new ArrayList<>(Arrays.asList(IdaithalamConstants.TESTCASE_NAME_HEADER,
          IdaithalamConstants.TESTCASE_NAME_DESC_HEADER));




  /**
   * Convert stream to string string.
   *
   * @param is the is
   * @return the string
   * @throws IOException the io exception
   */
  public static String convertStreamToString(InputStream is) throws IOException {
    if (is != null) {
      StringBuilder sb = new StringBuilder();

      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        String line;
        while ((line = reader.readLine()) != null) {
          if (!line.trim().equalsIgnoreCase("")) {
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


  private static Map<Integer, String> getHeader(Row nextRow) {
    Map<Integer, String> headers = new HashMap<>();
    int headerIndex = 0;
    for (Cell cell : nextRow) {
      headers.put(headerIndex++, getCellValue(cell));

    }
    return headers;
  }

  private static boolean isEmptyRow(Row row) {
    if (row == null) {
      return true;
    }
    if (row.getLastCellNum() <= 0) {
      return true;
    }
    for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
      Cell cell = row.getCell(cellNum);
      if (cell != null && cell.getCellType() != BLANK && StringUtils
          .isNotBlank(cell.toString())) {
        return false;
      }
    }
    return true;
  }


  private static Map<String, String> getRow(Row nextRow, Map<Integer, String> headers) {
    Map<String, String> dataMap = new HashMap<>();
    if (nextRow != null && !isEmptyRow(nextRow)) {
      for (Cell cell : nextRow) {
        String key = headers.get(cell.getColumnIndex());
        dataMap.put(key, getCellValue(cell));
      }
    }
    return dataMap;
  }

  /**
   * Create collection.
   *
   * @param apiExecutorParam the api executor param
   * @return the boolean
   * @throws IOException              the io exception
   * @throws UnableToProcessException the unable to process exception
   */
  public static boolean createCollection(ApiExecutorParam apiExecutorParam)
          throws IOException, UnableToProcessException, MandatoryFieldMissingException, IdaithalamException {
      Map<String, Map<String, String>> buildCollections = new BuildCollections()
          .createCollection(apiExecutorParam);
      Map<String, String> excludeResponseMap = buildCollections.get("exclude");
      Map<String, String> cucumblanMap = buildCollections.get("cucumblan");
      createProps(apiExecutorParam.getOutputDir(), cucumblanMap, "cucumblan.properties");
      InputStream streamEnv = Thread.currentThread().getContextClassLoader()
          .getResourceAsStream("cucumblan-env.properties");
      if (streamEnv != null) {
        createPrpos(apiExecutorParam.getOutputDir(), streamEnv, "cucumblan-env.properties");
      }
      if (!excludeResponseMap.isEmpty()) {
        createProps(apiExecutorParam.getOutputDir(), excludeResponseMap,
            "exclude-response.properties");
      }
    return true;
  }

  private static void getAsSingleFile(ApiExecutorParam apiExecutorParam, int sheet,
      Map<String, String> cucumblanMap, JSONArray virtualanArray) throws UnableToProcessException {
    Map<String, JSONArray> arrayMap = buildByMiniCategory(virtualanArray);
    for (Map.Entry<String, JSONArray> entry : arrayMap.entrySet()) {
      JSONArray virtualanSingle = entry.getValue();
      String scenarioId = virtualanSingle.getJSONObject(0).getString("scenarioId").split("-")[0];
      if (byEachTestCase(apiExecutorParam.getGeneratedTestList(), scenarioId)) {
        CreateFileInfo createFileInfo = new CreateFileInfo();
        createFileInfo.setGeneratedPath(apiExecutorParam);
        createFileInfo.setCucumblanMap(cucumblanMap);
        createFileInfo.setVirtualanArray(virtualanSingle);
        createFileInfo
            .setTestcaseName(
                scenarioId+"-"+sheet);
        createFileInfo.setScenario(scenarioId);
        createIdaithalamProcessingFile(createFileInfo);
      }
    }
  }

  private static Map<String, JSONArray> buildByMiniCategory(JSONArray virtualanArray) {
    Map<String, JSONArray> arrayMap = new HashMap<>();
    for (int i = 0; i < virtualanArray.length(); i++) {
      String scenarioId = virtualanArray.getJSONObject(i).getString("scenarioId").split("-")[0];
      JSONArray array = null;
      if (arrayMap.containsKey(scenarioId)) {
        array = arrayMap.get(scenarioId);
      } else {
        array = new JSONArray();
      }
      array.put(virtualanArray.getJSONObject(i));
      arrayMap.put(scenarioId, array);
    }
    return arrayMap;
  }


  private static JSONArray getObjectSheet(int sheet, List<String> generatedTestCaseList,
      SheetObject sheetObject,  List<String> missingFieldsLogMessages ) throws UnableToProcessException {
    Map<Integer, String> headers = new HashMap<>();
    JSONArray virtualanArray = new JSONArray();
    for (int i = 0; i <= sheetObject.getFirstSheet().getLastRowNum(); i++) {
      Row nextRow = sheetObject.getFirstSheet().getRow(i);
      if (headers.isEmpty()) {
        headers = getHeader(nextRow);
      } else {
        String testcaseName = null;
        try {
          Map<String, String> finalRow = getRow(nextRow, headers);
          testcaseName = finalRow.get("TestCaseName");
          if (!finalRow.isEmpty() && (generatedTestCaseList == null || generatedTestCaseList
              .isEmpty()
              || generatedTestCaseList.contains(testcaseName))) {
            if (finalRow.get("Type") == null || "REST".equalsIgnoreCase(finalRow.get("Type")) ||
                    "GRAPHQL".equalsIgnoreCase(finalRow.get("Type"))) {
             List<String> missingData =  restTypeInputDataValidation(finalRow);
             if(!missingData.isEmpty()){
               missingFieldsLogMessages.add(String.format("Sheet Name : %s - Row : %s - Type : REST - %s mandatory fields are missing",sheetObject.getFirstSheet().getSheetName(),(i+1),missingData.toString()));
               continue;
             }
              JSONObject object = buildRESTVirtualanCollection(sheetObject.getBasePath(),
                  finalRow);
              populateConfigMaps(finalRow, sheetObject.getCucumblanMap(),
                  sheetObject.getExcludeResponseMap());
              virtualanArray.put(object);
            } else if ("KAFKA".equalsIgnoreCase(finalRow.get("Type"))) {
              List<String> missingData =  kafkaTypeInputDataValidation(finalRow);
              if(!missingData.isEmpty()){
                missingFieldsLogMessages.add(String.format("Sheet Name : %s - Row : %s - Type : KAFKA - %s mandatory fields are missing",sheetObject.getFirstSheet().getSheetName(),(i+1),missingData.toString()));
                continue;
              }
              JSONObject object = buildKAFKAVirtualanCollection(sheetObject.getBasePath(),
                  finalRow);
              virtualanArray.put(object);

            } else if ("DB".equalsIgnoreCase(finalRow.get("Type"))) {
              List<String> missingData =  dbTypeInputDataValidation(finalRow);
              if(!missingData.isEmpty()){
                missingFieldsLogMessages.add(String.format("Sheet Name : %s - Row : %s - Type : DB - %s mandatory fields are missing",sheetObject.getFirstSheet().getSheetName(),(i+1),missingData.toString()));
                continue;
              }
              JSONObject object = buildDataBaseVirtualanCollection(sheetObject.getBasePath(),
                  finalRow);
              virtualanArray.put(object);
            }
          }
        } catch (Exception e) {

          log.warn(
              "Spread sheet (" + testcaseName + ") " + (sheet + 1) + " with row number " + (i + 1)
                  + " is unable to process for the reason >> "
                  + e.getMessage());
          throw new UnableToProcessException("Spread sheet (" + testcaseName + ") " + (sheet + 1) + " with row number " + (i + 1)
                  + " is unable to process for the reason >> "
                  + e.getMessage());
        }
      }
    }
    return virtualanArray;
  }


  private static boolean byEachTestCase(List<String> generatedTestCaseList,
      String scenarioId) {
    return (!IdaithalamConfiguration.isWorkFlow()
        && (generatedTestCaseList == null || generatedTestCaseList.isEmpty()
        || generatedTestCaseList.stream().anyMatch(x -> scenarioId.contains(x))));

  }

  /**
   * Gets input stream.
   *
   * @param basePath                the base path
   * @param fileNameWithSubCategory the file name with sub category
   * @return the input stream
   * @throws FileNotFoundException    the file not found exception
   * @throws UnableToProcessException the unable to process exception
   */
  public static InputStream getInputStream(String basePath, String fileNameWithSubCategory)
      throws FileNotFoundException, UnableToProcessException {
    InputStream stream = null;
    String filePath = basePath + File.separator + fileNameWithSubCategory;
    File file = new File(filePath);
    File fileSub = new File(fileNameWithSubCategory);
    if (file.exists()) {
      stream = new FileInputStream(file);
    } else if (fileSub.exists()) {
      stream = new FileInputStream(fileSub);
    }
    if (stream == null) {
      stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
      if (stream == null) {
        stream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(fileNameWithSubCategory);
      }
    }
    if (stream == null) {
      stream = ExcelToCollectionGenerator.class.getClassLoader().getResourceAsStream(filePath);
      if (stream == null) {
        stream = ExcelToCollectionGenerator.class.getClassLoader()
            .getResourceAsStream(fileNameWithSubCategory);
      }
    }
    if (stream == null) {
      log.error(" File is missing(" + basePath + ") : " + fileNameWithSubCategory);
      throw new UnableToProcessException(
          " File is missing(" + basePath + ") : " + fileNameWithSubCategory);
    }
    return stream;
  }

  /**
   * Gets file as string.
   *
   * @param basePath                the base path
   * @param fileNameWithSubCategory the file name with sub category
   * @return the file as string
   * @throws Exception the exception
   */
  public static String getFileAsString(String basePath, String fileNameWithSubCategory)
      throws Exception {
    InputStream stream = null;
    String filePath = basePath + File.separator + fileNameWithSubCategory;
    File file = new File(filePath);
    File fileSub = new File(fileNameWithSubCategory);
    if (file.exists()) {
      stream = new FileInputStream(file);
    } else if (fileSub.exists()) {
      stream = new FileInputStream(fileSub);
    }
    stream = getInputStream(fileNameWithSubCategory, stream, filePath);
    if (stream == null) {
      stream = ExcelToCollectionGenerator.class.getClassLoader().getResourceAsStream(filePath);
      if (stream == null) {
        stream = ExcelToCollectionGenerator.class.getClassLoader()
            .getResourceAsStream(fileNameWithSubCategory);
      }
    }
    if (stream == null) {
      log.error(" File is missing(" + basePath + ") : " + fileNameWithSubCategory);
      throw new Exception(" File is missing(" + basePath + ") : " + fileNameWithSubCategory);
    }
    return convertStreamToString(stream);
  }

  private static InputStream getInputStream(String fileNameWithSubCategory,
      InputStream stream,
      String filePath) {
    if (stream == null) {
      stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
      if (stream == null) {
        stream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream(fileNameWithSubCategory);
      }
    }
    return stream;
  }

  private static Map<String, String> getCucumblan() {
    Map<String, String> cucumblanMap = new HashMap<>();
    cucumblanMap.put("virtualan.data.load", "");
    cucumblanMap.put("virtualan.data.heading", "");
    cucumblanMap.put("virtualan.data.type", "VIRTUALAN");
    return cucumblanMap;
  }

  private static JSONObject buildDataBaseVirtualanCollection(String basePath,
      Map<String, String> dataMap)
      throws UnableToProcessException {
    JSONObject virtualanObj = new JSONObject();
    try {
      JSONArray paramsArray = new JSONArray();
      virtualanObj.put("requestType", "DB");
      virtualanObj.put("scenarioId", dataMap.get("TestCaseName"));
      virtualanObj.put("scenario", dataMap.get("TestCaseNameDesc"));
      virtualanObj.put("resource", dataMap.get("Resource"));
      createProcessingType(dataMap, paramsArray, "StoreResponseVariables", "STORAGE_PARAM");
      createProcessingType(dataMap, paramsArray, "AddifyVariables", "ADDIFY_PARAM");
      if (dataMap.get("SkipScenario") != null) {
        virtualanObj.put("skipScenario", dataMap.get("SkipScenario"));
      }

      if (dataMap.get("Tags") != null) {
        virtualanObj.put("tags", dataMap.get("Tags"));
      }

      if (dataMap.get("StepInfo") != null && !dataMap.get("StepInfo").isEmpty()) {
          virtualanObj.put("stepInfo", Arrays.asList(dataMap.get("StepInfo").split("(?<!\\\\);")));
      }

      if(Objects.nonNull(dataMap.get("Action"))) {
        virtualanObj.put("method", dataMap.get("Action").toUpperCase());
      }
      if (dataMap.get("RequestFile") != null || dataMap.get("RequestContent") != null) {
        virtualanObj.put("input", buildObjectRequest(basePath, dataMap));
      }
      if (dataMap.get("ResponseByFields") != null) {
        buildOutputByFields(virtualanObj, dataMap.get("ResponseByFields"));
      }
      if (dataMap.get("Csvson") != null) {
        buildCSVSON(dataMap, virtualanObj);
      }


      if (dataMap.get("ResponseFile") != null || dataMap.get("ResponseContent") != null) {
        if (dataMap.get("IncludesByPath") != null) {
          virtualanObj
              .put("outputPaths", Arrays.asList(dataMap.get("IncludesByPath").split("(?<!\\\\);")));
        }
        virtualanObj.put("output", buildObjectResponse(basePath, dataMap));
      }

      if (paramsArray.length() > 0) {
        virtualanObj.put("availableParams", paramsArray);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new UnableToProcessException(
          "Unable to build collection object for Kafka " + dataMap.get("TestCaseName") + " :: " + e
              .getMessage());

    }
    return virtualanObj;
  }

  private static void buildOutputByFields(JSONObject virtualanObj, String outputByField ) {
      String[] outputFields = outputByField.split("(?<!\\\\);");
      if (outputFields.length > 0) {
          Map<String, String> outputFieldMap = new HashMap<>();
          for (String outputJsonUnEscaped : outputFields) {
              String outputJson = FeatureGenerationHelper
                  .removeVirtualanSemicolonEscape(outputJsonUnEscaped);
              if (outputJson.split("=").length == 2) {
                  outputFieldMap.put(outputJson.split("=")[0], outputJson.split("=")[1]);
              } else if (outputJson.split("(?<!\\\\)=").length == 2) {
                  outputFieldMap.put(FeatureGenerationHelper
                          .removeVirtualanEqualsEscape(outputJson.split("(?<!\\\\)=")[0]),
                      FeatureGenerationHelper
                          .removeVirtualanEqualsEscape(outputJson.split("(?<!\\\\)=")[1]));
              } else {
                  log.warn(" Does not seems like Kep Value Pair - {}", outputJson);
              }
          }
          virtualanObj.put("outputFields", new JSONObject(outputFieldMap));
      }
  }

  private static JSONObject buildKAFKAVirtualanCollection(String basePath,
      Map<String, String> dataMap)
      throws UnableToProcessException {
    JSONObject virtualanObj = new JSONObject();
    try {
      JSONArray paramsArray = new JSONArray();
      virtualanObj.put("requestType", "KAFKA");
      virtualanObj.put("scenarioId", dataMap.get("TestCaseName"));
      virtualanObj.put("scenario", dataMap.get("TestCaseNameDesc"));
      virtualanObj.put("resource", dataMap.get("Resource"));
      virtualanObj.put("event", dataMap.get("Event"));
      virtualanObj.put("identifier", dataMap.get("Identifier"));
      virtualanObj.put("messageType", dataMap.get("MessageType"));
      if (dataMap.get("StepInfo") != null && !dataMap.get("StepInfo").isEmpty()) {
        virtualanObj.put("stepInfo", Arrays.asList(dataMap.get("StepInfo").split("(?<!\\\\);")));
      }
      createProcessingType(dataMap, paramsArray, "StoreResponseVariables", "STORAGE_PARAM");
      createProcessingType(dataMap, paramsArray, "AddifyVariables", "ADDIFY_PARAM");
      if (dataMap.get("SkipScenario") != null) {
        virtualanObj.put("skipScenario", dataMap.get("SkipScenario"));
      }

      if (dataMap.get("Tags") != null) {
        virtualanObj.put("tags", dataMap.get("Tags"));
      }

      if(dataMap.get("ResponseProcessingType") != null && dataMap.get("ResponseProcessingType").split("=").length ==2 ){
        if("aggregatedStdType".equalsIgnoreCase(dataMap.get("ResponseProcessingType").split("=")[0])) {
          virtualanObj.put("aggregatedStdType", dataMap.get("ResponseProcessingType").split("=")[1]);
        }
      }

      //getSecurityValue(dataMap, virtualanObj);
      if (dataMap.get("RequestFile") != null || dataMap.get("RequestContent") != null) {
        virtualanObj.put("input", buildObjectRequest(basePath, dataMap));
      }
      if (dataMap.get("ResponseByFields") != null) {
          buildOutputByFields(virtualanObj, dataMap.get("ResponseByFields"));
      }
      if (dataMap.get("Csvson") != null) {
        buildCSVSON(dataMap, virtualanObj);
      }
      if (dataMap.get("ResponseFile") != null || dataMap.get("ResponseContent") != null) {
        if (dataMap.get("IncludesByPath") != null) {
          virtualanObj
              .put("outputPaths", Arrays.asList(dataMap.get("IncludesByPath").split("(?<!\\\\);")));
        }
        virtualanObj.put("output", buildObjectResponse(basePath, dataMap));
      }
    } catch (Exception e) {
      throw new UnableToProcessException(
          "Unable to build collection object for Kafka " + dataMap.get("TestCaseName") + " :: " + e
              .getMessage());
    }
    return virtualanObj;
  }

  private static JSONObject buildRESTVirtualanCollection(String basePath,
      Map<String, String> dataMap)
      throws UnableToProcessException {
    JSONObject virtualanObj = new JSONObject();

    try {
      virtualanObj.put("requestType", "REST");
      JSONArray paramsArray = new JSONArray();
      getMultiRunValue(dataMap, virtualanObj, paramsArray);
      virtualanObj.put("contentType", dataMap.get(IdaithalamConstants.CONTENT_TYPE_HEADER));
      buildParam("contentType", dataMap.get(IdaithalamConstants.CONTENT_TYPE_HEADER), paramsArray, "HEADER_PARAM");
      createProcessingType(dataMap, paramsArray, "FormParams", "FORM_PARAM");
      createProcessingType(dataMap, paramsArray, "MultiFormParams", "MULTI_FORM_PARAM");
      createProcessingType(dataMap, paramsArray, "RequestHeaders", "HEADER_PARAM");
      createProcessingType(dataMap, paramsArray, "ResponseProcessingType", "HEADER_PARAM");
      virtualanObj.put("scenarioId", dataMap.get(IdaithalamConstants.TESTCASE_NAME_HEADER));
      virtualanObj.put("scenario", dataMap.get(IdaithalamConstants.TESTCASE_NAME_DESC_HEADER));
      createProcessingType(dataMap, paramsArray, "StoreResponseVariables", "STORAGE_PARAM");
      createProcessingType(dataMap, paramsArray, "EvaluateFunctionVariables", "EVAL_PARAM");
      createProcessingType(dataMap, paramsArray, "AddifyVariables", "ADDIFY_PARAM");
      createProcessingType(dataMap, paramsArray, "CookieVariables", "COOKIE_PARAM");
      String svFileName = dataMap.get(io.virtualan.idaithalam.core.IdaithalamConstants.SCHEMA_VALIDATION);
      if (svFileName != null) {
        String[] svFileNameAndCheck = svFileName.split("(?<!\\\\)=");
        JSONObject schema = new org.json.JSONObject();
        schema.put("schemaFile", svFileNameAndCheck[0]);
        schema.put("validation", "true");
        if(svFileNameAndCheck.length == 2) {
          schema.put("validation",  svFileNameAndCheck[1]);
        }
        virtualanObj.put("schemaValidation", schema);
      }

      if (dataMap.get("Tags") != null) {
        virtualanObj.put("tags", dataMap.get("Tags"));
      }
      if (dataMap.get("SkipScenario") != null) {
        virtualanObj.put("skipScenario", dataMap.get("SkipScenario"));
      }
      if (dataMap.get("StepInfo") != null && !dataMap.get("StepInfo").isEmpty()) {
          virtualanObj.put("stepInfo", Arrays.asList(dataMap.get("StepInfo").split("(?<!\\\\);")));
      }
      getSecurityValue(dataMap, virtualanObj);
      if (dataMap.get("Action") != null) {
        virtualanObj.put("method",
            dataMap.get("Action").toUpperCase());
      } else {
        log.error("HTTP ACTION IS MANDATORY!!! " + dataMap.get("TestCaseNameDesc"));
      }
      if (dataMap.get("URL") != null) {
        URL aURL = new URL(dataMap.get("URL"));
        String resource = getResource(aURL.getPath());
        virtualanObj.put("url", aURL.getPath());
        virtualanObj.put("resource", resource);
        createQueryParam(aURL.getQuery(), paramsArray);
      } else {
        log.error("URL IS MANDATORY!!! for " + dataMap.get("TestCaseName"));
      }
      if (dataMap.get("Resource") != null) {
        virtualanObj.put("resource", dataMap.get("Resource"));
      }
      if (dataMap.get("RequestFile") != null || dataMap.get("RequestContent") != null) {
        virtualanObj.put("input", buildObjectRequest(basePath, dataMap));
      }
      if (dataMap.get("ResponseByFields") != null) {
        buildOutputByFields(virtualanObj, dataMap.get("ResponseByFields"));
      }
      if (dataMap.get("Csvson") != null) {
        buildCSVSON(dataMap, virtualanObj);
      } else if (dataMap.get("ResponseFile") != null || dataMap.get("ResponseContent") != null) {
        if (dataMap.get("IncludesByPath") != null) {
          virtualanObj
              .put("outputPaths", Arrays.asList(dataMap.get("IncludesByPath").split("(?<!\\\\);")));
        }
        virtualanObj.put("output", buildObjectResponse(basePath, dataMap));
      }
      if(dataMap.get("ResponseProcessingType") != null && dataMap.get("ResponseProcessingType").split("=").length ==2 ){
        if("aggregatedStdType".equalsIgnoreCase(dataMap.get("ResponseProcessingType").split("=")[0])) {
          virtualanObj.put("aggregatedStdType", dataMap.get("ResponseProcessingType").split("=")[1]);
        }
      }
      builHttpStausCode(dataMap, virtualanObj);
      if (paramsArray.length() > 0) {
        virtualanObj.put("availableParams", paramsArray);
      }
    } catch (Exception e) {
      throw new UnableToProcessException(
          "Unable to build collection object for " + dataMap.get("TestCaseName") + " :: " + e
              .getMessage());
    }
    return virtualanObj;
  }

  private static List<String> restTypeInputDataValidation(Map<String, String> dataMap){
   return REST_MANDATORY_FIELDS.stream().filter(field->!dataMap.containsKey(field) || Objects.isNull(dataMap.get(field))).collect(Collectors.toList());
  }

  private static List<String> dbTypeInputDataValidation(Map<String, String> dataMap){
    return DB_MANDATORY_FIELDS.stream().filter(field->!dataMap.containsKey(field) || Objects.isNull(dataMap.get(field))).collect(Collectors.toList());
  }

  private static List<String> kafkaTypeInputDataValidation(Map<String, String> dataMap){
    return KAFKA_MANDATORY_FIELDS.stream().filter(field->!dataMap.containsKey(field) || Objects.isNull(dataMap.get(field))).collect(Collectors.toList());
  }



  private static void buildCSVSON(Map<String, String> dataMap, JSONObject virtualanObj) {
    List<String> cvson = new ArrayList<String>(Arrays.asList(dataMap.get("Csvson").split("\n")));
    JSONArray jsArray = new JSONArray(cvson);
    virtualanObj.put("csvson", jsArray);
  }

  private static void getSecurityValue(Map<String, String> dataMap, JSONObject virtualanObj) {
    String security = dataMap.get("Security");
    if (security != null && !security.isEmpty() && security.split("(?<!\\\\)=").length == 2) {
      virtualanObj.put("security", "okta");
    } else if (dataMap.get("Security") != null) {
      virtualanObj.put("security", security);
    }

  }


  private static void getMultiRunValue(Map<String, String> dataMap, JSONObject virtualanObj,
      JSONArray paramsArray) {
    if (dataMap.get("MultiRun") != null) {
      List<String> rules = new ArrayList<String>(Arrays.asList(dataMap.get("MultiRun").split(";")));
      if (!rules.isEmpty()) {
        JSONArray jsArray = new JSONArray();
        String[] headers = rules.get(0).split("\\|");

        for (int i = 1; i < rules.size(); i++) {
          String[] values = rules.get(i).split("\\|");
          JSONObject object = new JSONObject();
          for (int j = 0; j < values.length; j++) {
            object.put(headers[j], values[j]);
          }
          jsArray.put(object);
        }

        virtualanObj.put("rule", jsArray);
        virtualanObj.put("type", "PARAMS");
        for (String param : headers) {
          buildParam(param, "<" + param + ">", paramsArray, "ADDIFY_PARAM");
        }
      }
    }
  }


  private static void builHttpStausCode(Map<String, String> dataMap, JSONObject virtualanObj) {
    if (dataMap.get("StatusCode") != null) {
      virtualanObj.put("httpStatusCode", dataMap.get("StatusCode"));
    } else {
      log.error("HTTP STATUS CODE IS MANDATORY!!! " + dataMap.get("TestCaseNameDesc"));
    }
  }

  private static String buildObjectResponse(String basePath, Map<String, String> responseFile)
      throws Exception {
    try {
      String body = null;
      if (responseFile.get("ResponseContent") != null) {
        body = responseFile.get("ResponseContent");
      } else if (responseFile.get("ResponseFile") != null) {
        body = getFileAsString(basePath, responseFile.get("ResponseFile"));
      }
      if (body != null) {
        return body.trim();
      } else {
        log.warn("Unable to load " + responseFile.get("ResponseFile") + " file or content > "
            + responseFile.get("ResponseContent"));
      }
    } catch (IOException e) {
      log.warn("Unable to load " + responseFile.get("ResponseFile") + " file or content > "
          + responseFile.get("ResponseContent"));
    }
    return null;
  }

  private static String buildObjectRequest(String basePath, Map<String, String> requestFile)
      throws Exception {
    try {
      String body = null;
      if (requestFile.get("RequestContent") != null) {
        body = requestFile.get("RequestContent");
      } else if (requestFile.get("RequestFile") != null) {
        body = getFileAsString(basePath, requestFile.get("RequestFile"));
      }
      if (body != null) {
        return body.trim();
      } else {
        log.warn("Unable to load " + requestFile + " file or content > " + requestFile);
      }
    } catch (IOException e) {
      log.warn("Unable to load " + requestFile + " file or conten> " + requestFile);
    }
    return null;
  }

  private static void createProcessingType(Map<String, String> dataMap,
      JSONArray paramsArray, String requestProcessingType, String param) {
    if (dataMap.get(requestProcessingType) != null) {
      String[] processTypes = dataMap.get(requestProcessingType).split(";");
      for (String keyValue : processTypes) {
        String key = keyValue.substring(0, keyValue.indexOf("="));
        String value = keyValue.substring(keyValue.indexOf("=") + 1);
        buildParam(key, value, paramsArray, param);
      }
    }
  }

  private static void createIdaithalamProcessingFile(
      CreateFileInfo createFileInfo) throws UnableToProcessException {
    String fileCreated = generateExcelJson(createFileInfo.getGeneratedPath(),
        createFileInfo.getVirtualanArray(),
        createFileInfo.getTestcaseName());
    if (fileCreated != null) {
      String filesCreated = createFileInfo.getCucumblanMap().get("virtualan.data.load");
      createFileInfo.getCucumblanMap().put("virtualan.data.load", filesCreated + fileCreated + ";");
      String headings = createFileInfo.getCucumblanMap().get("virtualan.data.heading");
      createFileInfo.getCucumblanMap()
          .put("virtualan.data.heading", headings + createFileInfo.getScenario() + ";");
    }
  }

  private static String getResource(String resource) {
    if (resource.split("/").length > 0) {
      return resource.split("/")[1];
    }
    return "default";
  }

  private static void createPrpos(String path, InputStream stream, String fileName) throws UnableToProcessException {
    try {
      Properties props = new Properties();
      //Populating the properties file
      props.load(stream);
      //Instantiating the FileInputStream for output file
      try (FileOutputStream outputStrem = new FileOutputStream(
          path + File.separator + fileName)) {
        //Storing the properties file
        props.store(outputStrem, "This is a " + fileName + " properties file");
        log.debug(fileName + " Properties file created......");
      }

    } catch (IOException e) {

      log.warn(" Unable to generate " + fileName + " properties  " + e.getMessage());
      throw new UnableToProcessException(" Unable to generate " + fileName + " properties  " + e.getMessage());
    }
  }

  /**
   * Create prpos.
   *
   * @param path     the path
   * @param propsMap the props map
   * @param fileName the file name
   */
  public static void createProps(String path, Map<String, String> propsMap, String fileName) throws UnableToProcessException {
    try {
      Properties props = new Properties();
      //Populating the properties file
      props.putAll(propsMap);
      //Instantiating the FileInputStream for output file
      try (FileOutputStream outputStrem = new FileOutputStream(
          path + File.separator + fileName)) {
        //Storing the properties file
        props.store(outputStrem, "This is a " + fileName + " properties file");
        log.debug(fileName + " file created");
      }

    } catch (IOException e) {
      if(!"exclude-response.properties".equalsIgnoreCase(fileName)) {
        log.warn(" Unable to generate " + fileName + " properties  " + e.getMessage());
        throw new UnableToProcessException(" Unable to generate " + fileName + " properties  " + e.getMessage());
      }
    }
  }

  private static String generateExcelJson(ApiExecutorParam apiExecutorParam, JSONArray excelArray,
      String fileName) throws UnableToProcessException {
    String fileCreated = null;
    try {
      FileOutputStream outputStream = null;
      if (apiExecutorParam.getVirtualanSpecPath() != null) {
        outputStream = new FileOutputStream(
            apiExecutorParam.getVirtualanSpecPath() + File.separator + fileName + ".json");
      } else {
        outputStream = new FileOutputStream(
            apiExecutorParam.getOutputDir() + File.separator + fileName + ".json");

      }
      Writer writer = new OutputStreamWriter(outputStream);
      CharSequence cs = excelArray.toString(4);
      writer.append(cs);
      writer.close();
      fileCreated = fileName + ".json";
    } catch (IOException e) {
      log.warn(" Unable to generate Virtualan  JSON  " + fileName + " : " + e.getMessage());
      throw new UnableToProcessException(" Unable to generate Virtualan  JSON  " + fileName + " : " + e.getMessage());
    }
    return fileCreated;
  }

  private static void buildParam(String key, String value, JSONArray paramsArray, String param) {
    JSONObject virtualanObjParam = new JSONObject();
    virtualanObjParam.put("key", key);
    virtualanObjParam.put("value", value);
    virtualanObjParam.put("parameterType", param);
    paramsArray.put(virtualanObjParam);
  }

  private static void createQueryParam(String queryString, JSONArray paramsArray) {
    if (queryString != null) {
      String[] querys = queryString.split("&");
      for (String query : querys) {
        JSONObject virtualanObjParam = new JSONObject();
        String[] queryPart = query.split("=");
        virtualanObjParam.put("key", queryPart[0]);
        if (queryPart.length == 2) {
          virtualanObjParam.put("value", queryPart[1]);
        } else {
          virtualanObjParam.put("value", "");
        }
        virtualanObjParam.put("parameterType", "QUERY_PARAM");
        paramsArray.put(virtualanObjParam);
      }
    }
  }

  private static void populateConfigMaps(
      Map<String, String> dataMap, Map<String, String> cucumblanMap,
      Map<String, String> excludeResponseMap) throws MalformedURLException {
    URL aURL = new URL(dataMap.get(IdaithalamConstants.URL_HEADER));
    String resource = getResource(aURL.getPath());
    if (dataMap.get(IdaithalamConstants.RESOURCE_HEADER) != null) {
      cucumblanMap.put("service.api." + dataMap.get(IdaithalamConstants.RESOURCE_HEADER),
          aURL.getProtocol() + "://" + aURL.getAuthority());
    } else {
      cucumblanMap.put("service.api." + resource,
          aURL.getProtocol() + "://" + aURL.getAuthority());
    }
    String okta = dataMap.get("Security");
    if (okta != null && !okta.isEmpty() && okta.split("=").length == 2) {
      cucumblanMap.put("service.api.okta_token." + resource, okta.split("=")[1]);
    }
    if (dataMap.get("ExcludeFields") != null) {
      excludeResponseMap.put(aURL.getPath(), dataMap.get("ExcludeFields"));
    }
  }

  private static String getCellValue(Cell cell) {
    if (cell.getCellType() != FORMULA) {
      switch (cell.getCellType()) {
        case STRING:
          return cell.getStringCellValue().trim();
        case BOOLEAN:
          return String.valueOf(cell.getBooleanCellValue());
        case NUMERIC:
          return String.valueOf((int) cell.getNumericCellValue());
        default:
          return null;
      }
    }
    return null;
  }

  private static class BuildCollections {

    /**
     * Instantiates a new Build collections.
     */
    BuildCollections() {
    }

    /**
     * Create collection map.
     *
     * @param apiExecutorParam the api executor param
     * @return the map
     * @throws IOException              the io exception
     * @throws UnableToProcessException the unable to process exception
     */
    Map<String, Map<String, String>> createCollection(ApiExecutorParam apiExecutorParam)
            throws IOException, UnableToProcessException, IdaithalamException {
      List<String> missingFieldsLogMessages = new ArrayList<>();
      Map<String, String> excludeResponseMap = new HashMap<>();
      Map<String, String> cucumblanMap= getCucumblan();

     try( InputStream stream = getInputStream(apiExecutorParam.getBasePath(),
          apiExecutorParam.getInputExcel())) {
       try (Workbook workbook = new XSSFWorkbook(stream)) {
         for (int sheet = 0; sheet < workbook.getNumberOfSheets(); sheet++) {
           Sheet firstSheet = workbook.getSheetAt(sheet);
           SheetObject sheetObject = new SheetObject();
           sheetObject.setBasePath(apiExecutorParam.getBasePath());
           sheetObject.setExcludeResponseMap(excludeResponseMap);
           sheetObject.setCucumblanMap(cucumblanMap);
           sheetObject.setFirstSheet(firstSheet);
           createCollections(apiExecutorParam, sheet, firstSheet, sheetObject, missingFieldsLogMessages);
         }
       }catch (UnableToProcessException unableToProcessException){
         throw unableToProcessException;
       }
       catch (Exception e) {
         log.error(
                 "Unable to create collection for the given excel file " + apiExecutorParam
                         .getInputExcel() + " <<< " + e
                         .getMessage(), e);
         throw new IdaithalamException("Unable to create collection for the given excel file " + apiExecutorParam
                 .getInputExcel() + " <<< " + e
                 .getMessage());
       }
     }
     if(!missingFieldsLogMessages.isEmpty()){
       log.error(missingFieldsLogMessages.toString());
       throw new MandatoryFieldMissingException(missingFieldsLogMessages.toString());
     }
      Map<String, Map<String, String>> response = new HashMap<>();
      response.put("cucumblan", cucumblanMap);
      response.put("exclude", excludeResponseMap);
      return response;
    }

    private void createCollections(ApiExecutorParam apiExecutorParam, int sheet, Sheet firstSheet,
        SheetObject sheetObject,  List<String> missingFieldsLogMessages )
            throws MalformedURLException, UnableToProcessException {
      JSONArray virtualanArray = getObjectSheet(sheet, apiExecutorParam.getGeneratedTestList(),
          sheetObject, missingFieldsLogMessages);
        log.debug("Sheet no " + sheet + " build out >>" + virtualanArray.toString());
        if (IdaithalamConfiguration.isWorkFlow()) {
          CreateFileInfo createFileInfo = new CreateFileInfo();
          createFileInfo.setGeneratedPath(apiExecutorParam);
          createFileInfo.setCucumblanMap(sheetObject.getCucumblanMap());
          createFileInfo.setVirtualanArray(virtualanArray);
          createFileInfo.setTestcaseName(
               firstSheet.getSheetName().replaceAll(" ", "_")+"-"+sheet);
          createFileInfo.setScenario(firstSheet.getSheetName() + " - Workflow");
          createIdaithalamProcessingFile(createFileInfo);
        } else {
          getAsSingleFile(apiExecutorParam, sheet, sheetObject.getCucumblanMap(),
              virtualanArray);
        }
    }
  }
}
