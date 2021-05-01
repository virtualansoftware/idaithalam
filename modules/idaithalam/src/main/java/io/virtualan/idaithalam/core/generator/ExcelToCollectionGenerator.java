package io.virtualan.idaithalam.core.generator;

import io.virtualan.idaithalam.config.IdaithalamConfiguration;
import io.virtualan.idaithalam.core.UnableToProcessException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * The type Excel to collection generator.
 */
@Slf4j
public class ExcelToCollectionGenerator {

  private ExcelToCollectionGenerator() {
  }


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

  /**
   * Create collection.
   *
   * @param generatedTestCaseList the generated test case list
   * @param excelFilePath         the excel file path
   * @param generatedPath         the generated path
   * @throws IOException the io exception
   */
  public static void createCollection(List<String> generatedTestCaseList,
      String excelFilePath,
      String generatedPath)
      throws IOException, UnableToProcessException {
    createCollection(null, generatedTestCaseList,
        excelFilePath,
        generatedPath);

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
      if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && StringUtils
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
   * @param basePath              the base path
   * @param generatedTestCaseList the generated test case list
   * @param excelFilePath         the excel file path
   * @param generatedPath         the generated path
   * @throws IOException the io exception
   */
  public static void createCollection(String basePath, List<String> generatedTestCaseList,
      String excelFilePath,
      String generatedPath)
      throws IOException, UnableToProcessException {
    try {
      BuildCollections buildCollections = new BuildCollections(basePath, generatedTestCaseList,
          generatedPath, excelFilePath).createCollection();
      Map<String, String> excludeResponseMap = buildCollections.getExcludeResponseMap();
      Map<String, String> cucumblanMap = buildCollections.getCucumblanMap();
      createPrpos(generatedPath, cucumblanMap, "cucumblan.properties");
      InputStream streamEnv = Thread.currentThread().getContextClassLoader()
          .getResourceAsStream("cucumblan-env.properties");
      if (streamEnv != null) {
        createPrpos(generatedPath, streamEnv, "cucumblan-env.properties");
      }
      if (!excludeResponseMap.isEmpty()) {
        createPrpos(generatedPath, excludeResponseMap, "exclude-response.properties");
      }
    } catch (Exception e) {
      log.error(
          "Unable to create collection for the given excel file " + excelFilePath + " >>> " + e
              .getMessage());
    }

  }

  private static void getAsSingleFile(int sheet, List<String> generatedTestCaseList,
      String generatedPath,
      Map<String, String> cucumblanMap, JSONArray virtualanArray) {
    Map<String, JSONArray> arrayMap = buildByMiniCategory(virtualanArray);
    for (Map.Entry<String, JSONArray> entry : arrayMap.entrySet()) {
      JSONArray virtualanSingle = entry.getValue();
      String scenarioId = virtualanSingle.getJSONObject(0).getString("scenarioId").split("-")[0];
      if (byEachTestCase(generatedTestCaseList, scenarioId)) {
        CreateFileInfo createFileInfo = new CreateFileInfo();
        createFileInfo.setGeneratedPath(generatedPath);
        createFileInfo.setCucumblanMap(cucumblanMap);
        createFileInfo.setVirtualanArray(virtualanSingle);
        createFileInfo
            .setTestcaseName(
                "Virtualan_" + sheet + "_" + scenarioId);
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
      SheetObject sheetObject) {
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
              .isEmpty() ||
              generatedTestCaseList.stream()
                  .anyMatch(x -> finalRow.get("TestCaseName").contains(x)))) {
            if (finalRow.get("Type") == null || "REST".equalsIgnoreCase(finalRow.get("Type"))) {
              JSONObject object = buildRESTVirtualanCollection(sheetObject.getBasePath(),
                  finalRow);
              populateConfigMaps(finalRow, sheetObject.getCucumblanMap(),
                  sheetObject.getExcludeResponseMap());
              virtualanArray.put(object);
            } else if ("KAFKA".equalsIgnoreCase(finalRow.get("Type"))) {
              JSONObject object = buildKAFKAVirtualanCollection(sheetObject.getBasePath(),
                  finalRow);
              virtualanArray.put(object);

            } else if ("DB".equalsIgnoreCase(finalRow.get("Type"))) {
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
   * @throws FileNotFoundException the file not found exception
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
   * @throws IOException the io exception
   */
  public static String getFileAsString(String basePath, String fileNameWithSubCategory)
      throws IOException {
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
      System.exit(-1);
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
      virtualanObj.put("type", "DB");
      virtualanObj.put("scenarioId", dataMap.get("TestCaseName"));
      virtualanObj.put("scenario", dataMap.get("TestCaseNameDesc"));
      virtualanObj.put("resource", dataMap.get("Resource"));
      //createProcessingType(dataMap, paramsArray, "StoreResponseVariables", "STORAGE_PARAM");
      //createProcessingType(dataMap, paramsArray, "AddifyVariables", "ADDIFY_PARAM");
      //createProcessingType(dataMap, paramsArray, "CookieVariables", "COOKIE_PARAM");
      getValue("Tags", dataMap, virtualanObj);
      getValue("SkipScenario", dataMap, virtualanObj);
      virtualanObj.put("method", dataMap.get("Action").toUpperCase());

      //getSecurityValue(dataMap, virtualanObj);
      if (dataMap.get("RequestFile") != null || dataMap.get("RequestContent") != null) {
        virtualanObj.put("input", buildObjectRequest(basePath, dataMap));
      }
      if (dataMap.get("ResponseByFields") != null) {
        virtualanObj.put("outputFields", dataMap.get("ResponseByFields"));
      }
      if (dataMap.get("Csvson") != null) {
        virtualanObj.put("csvson", dataMap.get("Csvson"));
      }
      if (dataMap.get("ResponseFile") != null || dataMap.get("ResponseContent") != null) {
        if (dataMap.get("IncludesByPath") != null) {
          virtualanObj.put("outputPaths", dataMap.get("IncludesByPath"));
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



  private static JSONObject buildKAFKAVirtualanCollection(String basePath,
      Map<String, String> dataMap)
      throws UnableToProcessException {
    JSONObject virtualanObj = new JSONObject();
    try {
      JSONArray paramsArray = new JSONArray();
      virtualanObj.put("type", "KAFKA");
      virtualanObj.put("scenarioId", dataMap.get("TestCaseName"));
      virtualanObj.put("scenario", dataMap.get("TestCaseNameDesc"));
      virtualanObj.put("resource", dataMap.get("Resource"));
      virtualanObj.put("event", dataMap.get("Event"));
      virtualanObj.put("identifier", dataMap.get("Identifier"));
      virtualanObj.put("messageType", dataMap.get("MessageType"));
      //createProcessingType(dataMap, paramsArray, "StoreResponseVariables", "STORAGE_PARAM");
      //createProcessingType(dataMap, paramsArray, "AddifyVariables", "ADDIFY_PARAM");
      //createProcessingType(dataMap, paramsArray, "CookieVariables", "COOKIE_PARAM");
      getValue("Tags", dataMap, virtualanObj);
      getValue("SkipScenario", dataMap, virtualanObj);
      //getSecurityValue(dataMap, virtualanObj);
      if (dataMap.get("RequestFile") != null || dataMap.get("RequestContent") != null) {
        virtualanObj.put("input", buildObjectRequest(basePath, dataMap));
      }
      if (dataMap.get("ResponseByFields") != null) {
        virtualanObj.put("outputFields", dataMap.get("ResponseByFields"));
      }
      if (dataMap.get("Csvson") != null) {
        virtualanObj.put("csvson", dataMap.get("Csvson"));
      }
      if (dataMap.get("ResponseFile") != null || dataMap.get("ResponseContent") != null) {
        if (dataMap.get("IncludesByPath") != null) {
          virtualanObj.put("outputPaths", dataMap.get("IncludesByPath"));
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
      virtualanObj.put("type", "REST");
      JSONArray paramsArray = new JSONArray();
      getMultiRunValue(dataMap, virtualanObj, paramsArray);
      virtualanObj.put("contentType", dataMap.get("ContentType"));
      buildParam("contentType", dataMap.get("ContentType"), paramsArray, "HEADER_PARAM");
      createProcessingType(dataMap, paramsArray, "FormParams", "FORM_PARAM");
      createProcessingType(dataMap, paramsArray, "RequestHeaders", "HEADER_PARAM");
      createProcessingType(dataMap, paramsArray, "ResponseProcessingType", "HEADER_PARAM");
      virtualanObj.put("scenarioId", dataMap.get("TestCaseName"));
      virtualanObj.put("scenario", dataMap.get("TestCaseNameDesc"));
      createProcessingType(dataMap, paramsArray, "StoreResponseVariables", "STORAGE_PARAM");
      createProcessingType(dataMap, paramsArray, "AddifyVariables", "ADDIFY_PARAM");
      createProcessingType(dataMap, paramsArray, "CookieVariables", "COOKIE_PARAM");
      getValue("tags", dataMap, virtualanObj);
      getValue("skipScenario", dataMap, virtualanObj);
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
      getValue("resource", dataMap, virtualanObj);
      if (dataMap.get("RequestFile") != null || dataMap.get("RequestContent") != null) {
        virtualanObj.put("input", buildObjectRequest(basePath, dataMap));
      }
      if (dataMap.get("ResponseByFields") != null) {
        virtualanObj.put("outputFields", dataMap.get("ResponseByFields"));
      } else if (dataMap.get("Csvson") != null) {
        virtualanObj.put("csvson", dataMap.get("Csvson"));
      } else if (dataMap.get("ResponseFile") != null || dataMap.get("ResponseContent") != null) {
        if (dataMap.get("IncludesByPath") != null) {
          virtualanObj.put("outputPaths", dataMap.get("IncludesByPath"));
        }
        virtualanObj.put("output", buildObjectResponse(basePath, dataMap));
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

  private static void getSecurityValue(Map<String, String> dataMap, JSONObject virtualanObj) {
    String security = dataMap.get("Security");
    if (security != null && !security.isEmpty() && security.split("=").length == 2) {
      virtualanObj.put("security", "okta");
    } else if (dataMap.get("Security") != null) {
      virtualanObj.put("security", security);
    }

  }

  private static void getValue(String key, Map<String, String> dataMap, JSONObject virtualanObj) {
    if (dataMap.get(key) != null) {
      virtualanObj.put(key, dataMap.get(key));
    }
  }

  private static void getMultiRunValue(Map<String, String> dataMap, JSONObject virtualanObj,
      JSONArray paramsArray) {
    if (dataMap.get("MultiRun") != null) {
      virtualanObj.put("MultiRun", dataMap.get("MultiRun"));
      String row = dataMap.get("MultiRun").split(";")[0];
      for (String param : row.split("\\|")) {
        buildParam(param, "<" + param + ">", paramsArray, "ADDIFY_PARAM");
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

  private static String buildObjectResponse(String basePath, Map<String, String> responseFile) {
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

  private static String buildObjectRequest(String basePath, Map<String, String> requestFile) {
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
        String[] processType = keyValue.split("=");
        if (processType.length == 2) {
          buildParam(processType[0], processType[1], paramsArray, param);
        }
      }
    }
  }

  private static void createIdaithalamProcessingFile(
      CreateFileInfo createFileInfo) {
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

  private static void createPrpos(String path, InputStream stream, String fileName) {
    try {
      Properties props = new Properties();
      //Populating the properties file
      props.load(stream);
      //Instantiating the FileInputStream for output file
      FileOutputStream outputStrem = new FileOutputStream(
          path + File.separator + fileName);
      //Storing the properties file
      props.store(outputStrem, "This is a " + fileName + " properties file");
      log.info(fileName + " Properties file created......");
    } catch (IOException e) {
      log.warn(" Unable to generate " + fileName + " properties  " + e.getMessage());
    }
  }

  public static void createPrpos(String path, Map<String, String> propsMap, String fileName) {
    try {
      Properties props = new Properties();
      //Populating the properties file
      props.putAll(propsMap);
      //Instantiating the FileInputStream for output file
      FileOutputStream outputStrem = new FileOutputStream(
          path + File.separator + fileName);
      //Storing the properties file
      props.store(outputStrem, "This is a " + fileName + " properties file");
      log.info(fileName + " Properties file created......");
    } catch (IOException e) {
      log.warn(" Unable to generate " + fileName + " properties  " + e.getMessage());
    }
  }

  private static String generateExcelJson(String path, JSONArray excelArray, String fileName) {
    String fileCreated = null;
    try {
      FileOutputStream outputStream = new FileOutputStream(
          path + File.separator + fileName + ".json");
      Writer writer = new OutputStreamWriter(outputStream);
      CharSequence cs = excelArray.toString();
      writer.append(cs);
      writer.close();
      fileCreated = fileName + ".json";
    } catch (IOException e) {
      log.warn(" Unable to generate Virtualan  JSON  " + fileName + " : " + e.getMessage());
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
    URL aURL = new URL(dataMap.get("URL"));
    String resource = getResource(aURL.getPath());
    cucumblanMap.put("service.api." + resource,
        aURL.getProtocol() + "://" + aURL.getAuthority());
    String okta = dataMap.get("Security");
    if (okta != null && !okta.isEmpty() && okta.split("=").length == 2) {
      cucumblanMap.put("service.api.okta_token." + resource, okta.split("=")[1]);
    }
    if (dataMap.get("ExcludeFields") != null) {
      excludeResponseMap.put(aURL.getPath(), dataMap.get("ExcludeFields"));
    }
  }

  private static String getCellValue(Cell cell) {
    if (cell.getCellTypeEnum() != CellType.FORMULA) {
      switch (cell.getCellTypeEnum()) {
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

    private final String basePath;
    private final List<String> generatedTestCaseList;
    private final String generatedPath;
    private final String inputExcel;
    private Map<String, String> excludeResponseMap;
    private Map<String, String> cucumblanMap;

    BuildCollections(String basePath, List<String> generatedTestCaseList,
        String generatedPath,
        String inputExcel) {
      this.basePath = basePath;
      this.generatedTestCaseList = generatedTestCaseList;
      this.generatedPath = generatedPath;
      this.inputExcel = inputExcel;
    }

    Map<String, String> getExcludeResponseMap() {
      return excludeResponseMap;
    }

    Map<String, String> getCucumblanMap() {
      return cucumblanMap;
    }


    BuildCollections createCollection() throws IOException, UnableToProcessException {
      excludeResponseMap = new HashMap<>();
      cucumblanMap = getCucumblan();
      InputStream stream = getInputStream(basePath, inputExcel);
      Workbook workbook = null;
      try {
        workbook = new XSSFWorkbook(stream);
        for (int sheet = 0; sheet < workbook.getNumberOfSheets(); sheet++) {
          Sheet firstSheet = workbook.getSheetAt(sheet);
          SheetObject sheetObject = new SheetObject();
          sheetObject.setBasePath(basePath);
          sheetObject.setExcludeResponseMap(excludeResponseMap);
          sheetObject.setCucumblanMap(cucumblanMap);
          sheetObject.setFirstSheet(firstSheet);
          createCollections(generatedTestCaseList, sheet, firstSheet, sheetObject);
        }
      } catch (Exception e) {
        log.error(
            "Unable to create collection for the given excel file " + inputExcel + " <<< " + e
                .getMessage());
      } finally {
        if (workbook != null) {
          workbook.close();
        }
        if (stream != null) {
          stream.close();
        }
      }
      return this;
    }

    private void createCollections(List<String> generatedTestCaseList, int sheet, Sheet firstSheet,
        SheetObject sheetObject)
        throws MalformedURLException {
      JSONArray virtualanArray = getObjectSheet(sheet, generatedTestCaseList, sheetObject);
      log.info("Sheet no " + sheet + " build out" + virtualanArray.toString());
      if (virtualanArray.length() > 0) {
        if (IdaithalamConfiguration.isWorkFlow()) {
          CreateFileInfo createFileInfo = new CreateFileInfo();
          createFileInfo.setGeneratedPath(generatedPath);
          createFileInfo.setCucumblanMap(cucumblanMap);
          createFileInfo.setVirtualanArray(virtualanArray);
          createFileInfo.setTestcaseName(
              "Virtualan_" + sheet + "_" + firstSheet.getSheetName().replaceAll(" ", "_")
                  + "_WORKFLOW_" + sheet);
          createFileInfo.setScenario("WORKFLOW:" + firstSheet.getSheetName());
          createIdaithalamProcessingFile(createFileInfo);
        } else {
          getAsSingleFile(sheet, generatedTestCaseList, generatedPath, cucumblanMap,
              virtualanArray);
        }
      }
    }
  }
}
