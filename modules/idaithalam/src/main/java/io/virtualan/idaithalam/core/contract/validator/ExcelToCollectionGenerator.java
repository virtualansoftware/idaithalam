package io.virtualan.idaithalam.core.contract.validator;

import io.virtualan.idaithalam.config.IdaithalamConfiguration;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
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


  private static String convertStreamToString(InputStream is) throws IOException {
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
      throws IOException {
    createCollection(null, generatedTestCaseList,
        excelFilePath,
        generatedPath);

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
      throws IOException {
    InputStream stream = getInputStream(basePath, excelFilePath);
    try {
      if (stream != null) {
        Map<String, String> excludeResponseMap = new HashMap<>();
        Map<String, String> cucumblanMap = getCucumblan();
        Workbook workbook = new XSSFWorkbook(stream);
        for (int sheet =0; sheet < workbook.getNumberOfSheets(); sheet++) {
          Sheet firstSheet = workbook.getSheetAt(sheet);
          JSONArray virtualanArray = new JSONArray();
          Map<Integer, String> headerMap = new HashMap<>();
          int rowCount = 0;
          for (Iterator<Row> iterator = firstSheet.iterator(); iterator.hasNext(); ) {
            int count = 0;
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            Map<String, String> dataMap = new HashMap<>();
            while (cellIterator.hasNext()) {
              Cell cell = cellIterator.next();
              if (rowCount == 0) {
                headerMap.put(count++, cell.getStringCellValue());
              } else {
                String key = headerMap.get(cell.getColumnIndex());
                if ("HttpStatusCode".equalsIgnoreCase(key)) {
                  dataMap.put(key, String.valueOf((int) cell.getNumericCellValue()));
                } else {
                  dataMap.put(key, cell.getStringCellValue());
                }
              }
            }
            if (rowCount > 0 && (generatedTestCaseList == null || generatedTestCaseList.isEmpty()
                || generatedTestCaseList
                .contains(dataMap.get("TestCaseName")))) {
              JSONObject object = buildVirtualanCollection(basePath, generatedPath, rowCount,
                  cucumblanMap,
                  excludeResponseMap,
                  dataMap);
              virtualanArray.put(object);
            }
            rowCount++;
          }
          if (IdaithalamConfiguration.isWorkFlow()) {
            createIdaithalamProcessingFile(generatedPath, rowCount, cucumblanMap, virtualanArray,
                firstSheet.getSheetName() + "_WORKFLOW",
                "WORKFLOW:" + firstSheet.getSheetName());
          }
          log.info(virtualanArray.toString());
        }
          createPrpos(generatedPath, cucumblanMap, "cucumblan.properties");
          if (!excludeResponseMap.isEmpty()) {
            createPrpos(generatedPath, excludeResponseMap, "exclude-response.properties");
          }
          workbook.close();
          stream.close();
        } else{
          log.error(
              "Unable to create collection for the given excel file " + excelFilePath + " <<< ");
        }
    } catch (Exception e) {
      log.error(
          "Unable to create collection for the given excel file " + excelFilePath + " >>> " + e
              .getMessage());
    }

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
      throws FileNotFoundException {
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
    if(stream == null) {
      log.error(" File is missing("+basePath+") : " + fileNameWithSubCategory);
      System.exit(-1);
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
    if(stream == null) {
      log.error(" File is missing("+basePath+") : " + fileNameWithSubCategory);
      System.exit(-1);
    }
    return convertStreamToString(stream);
  }


  private static Map<String, String> getCucumblan() {
    Map<String, String> cucumblanMap = new HashMap<>();
    cucumblanMap.put("virtualan.data.load", "");
    cucumblanMap.put("virtualan.data.heading", "");
    cucumblanMap.put("virtualan.data.type", "VIRTUALAN");
    return cucumblanMap;
  }

  private static JSONObject buildVirtualanCollection(String basePath, String generatedPath,
      int rowCount,
      Map<String, String> cucumblanMap, Map<String, String> excludeResponseMap,
      Map<String, String> dataMap) throws MalformedURLException {
    JSONObject virtualanObj = new JSONObject();
    JSONArray paramsArray = new JSONArray();
    virtualanObj.put("contentType", getContentType(dataMap.get("ContentType")));
    buildParam("contentType", dataMap.get("ContentType"), paramsArray, "HEADER_PARAM");
    createProcessingType(dataMap, paramsArray, "RequestProcessingType", "HEADER_PARAM");
    createProcessingType(dataMap, paramsArray, "ResponseProcessingType", "HEADER_PARAM");
    virtualanObj.put("scenario", dataMap.get("TestCaseNameDesc"));
    createProcessingType(dataMap, paramsArray, "StoreResponseVariables", "STORAGE_PARAM");
    createProcessingType(dataMap, paramsArray, "AddifyVariables", "ADDIFY_PARAM");
    getValue("tags", dataMap, virtualanObj);
    getValue("security", dataMap, virtualanObj);
    if (dataMap.get("HTTPAction") != null) {
      virtualanObj.put("method",
          dataMap.get("HTTPAction").toUpperCase());
    } else {
      log.error("HTTP ACTION IS MANDATORY!!! " + dataMap.get("TestCaseNameDesc"));
    }
    if (dataMap.get("URL") != null) {
      buildUrl(cucumblanMap, excludeResponseMap, dataMap, virtualanObj, paramsArray);
    } else {
      log.error("URL IS MANDATORY!!! " + dataMap.get("TestCaseNameDesc"));
    }
    buildObject(basePath, dataMap, virtualanObj, "RequestFile", "input");
    buildObject(basePath, dataMap, virtualanObj, "ResponseFile", "output");
    builHttpStausCode(dataMap, virtualanObj);
    if (paramsArray.length() > 0) {
      virtualanObj.put("availableParams", paramsArray);
    }

    if (!IdaithalamConfiguration.isWorkFlow()) {
      JSONArray virtualanArray = new JSONArray();
      virtualanArray.put(virtualanObj);
      createIdaithalamProcessingFile(generatedPath, rowCount, cucumblanMap, virtualanArray, dataMap.get("TestCaseName"),
          virtualanObj.get("scenario") != null ? virtualanObj.get("scenario").toString()
              : "Not defined");
      log.info(virtualanArray.toString());
    }
    return virtualanObj;
  }

  private static void getValue(String key, Map<String, String> dataMap, JSONObject virtualanObj) {
    if(dataMap.get(key) != null) {
      virtualanObj.put(key, dataMap.get(key));
    }
  }

  private static void builHttpStausCode(Map<String, String> dataMap, JSONObject virtualanObj) {
    if (dataMap.get("HttpStatusCode") != null) {
      virtualanObj.put("httpStatusCode", dataMap.get("HttpStatusCode"));
    } else {
      log.error("HTTP STATUS CODE IS MANDATORY!!! " + dataMap.get("TestCaseNameDesc"));
    }
  }

  private static void buildObject(String basePath, Map<String, String> dataMap,
      JSONObject virtualanObj,
      String requestFile, String input) {
    try {
      if (dataMap.get(requestFile) != null) {
        String body = null;
        body = getFileAsString(basePath, dataMap.get(requestFile));
        if (body != null) {
          virtualanObj.put(input, body);
        } else {
          log.warn("Unable to load " + requestFile + " file > " + dataMap.get(requestFile));
        }
      }
    } catch (IOException e) {
      log.warn("Unable to load " + requestFile + " file > " + dataMap.get(requestFile));
    }
  }

  private static void createProcessingType(Map<String, String> dataMap,
      JSONArray paramsArray, String requestProcessingType, String param) {
    if (dataMap.get(requestProcessingType) != null) {
      String[] processTypes = dataMap.get(requestProcessingType).split(";");
      for(String keyValue : processTypes) {
        String[] processType = keyValue.split("=");
        if (processType.length == 2) {
          buildParam(processType[0], processType[1], paramsArray, param);
        }
      }
    }
  }

  private static JSONObject createStoreProcessingType(Map<String, String> dataMap,
      JSONArray paramsArray, String requestProcessingType) {
    JSONObject virtualanObjParam = new JSONObject();
    if (dataMap.get(requestProcessingType) != null) {
      String[] processType = dataMap.get(requestProcessingType).split(";");
      for( String store : processType) {
        virtualanObjParam.put(store.split("=")[0], store.split("=")[1]);
      }
      return  virtualanObjParam;
    }
    return null;
  }


  private static void createIdaithalamProcessingFile(String generatedPath, int rowCount,
      Map<String, String> cucumblanMap, JSONArray virtualanArray, String  testcaseName,
      String scenario) {
    String fileCreated = generateExcelJson(generatedPath, virtualanArray,
        "Virtualan_" + testcaseName + "_" + rowCount);
    if (fileCreated != null) {
      String filesCreated = cucumblanMap.get("virtualan.data.load");
      cucumblanMap.put("virtualan.data.load", filesCreated + fileCreated + ";");
      String headings = cucumblanMap.get("virtualan.data.heading");
      cucumblanMap
          .put("virtualan.data.heading", headings + scenario + ";");
    }
  }

  private static void buildUrl(Map<String, String> cucumblanMap,
      Map<String, String> excludeResponseMap, Map<String, String> dataMap, JSONObject virtualanObj,
      JSONArray paramsArray) throws MalformedURLException {
    String url = dataMap.get("URL");
    URL aURL = new URL(url);
    String resource = getResource(aURL.getPath());
    virtualanObj.put("url", aURL.getPath());
    cucumblanMap.put("service.api." + resource,
        aURL.getProtocol() + "://" + aURL.getAuthority());
    String okta = virtualanObj.optString("security");
    if (okta != null && !okta.isEmpty() && okta.split("=").length ==2) {
      cucumblanMap.put("service.api.okta_token." + resource, okta.split("=")[1]);
      virtualanObj.put("security", "okta");
    }
    createQueryParam(aURL.getQuery(), paramsArray);
    virtualanObj.put("resource", resource);
    if (dataMap.get("ExcludeField") != null) {
      excludeResponseMap.put(aURL.getPath(), dataMap.get("ExcludeField"));
    }
  }

  private static String getResource(String resource) {
    if (resource.split("/").length > 0) {
      return resource.split("/")[1];
    }
    return "default";
  }

  private static void createPrpos(String path, Map<String, String> propsMap, String fileName) {
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

  private static ContentType getContentType(String contentType) {
    if (contentType.contains("xml")) {
      return ContentType.XML;
    }
    return ContentType.JSON;
  }
}
