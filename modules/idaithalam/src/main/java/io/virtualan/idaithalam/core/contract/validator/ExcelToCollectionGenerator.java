package io.virtualan.idaithalam.core.contract.validator;

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
        Workbook workbook = new XSSFWorkbook(stream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Map<Integer, String> headerMap = new HashMap<>();
        Iterator<Row> iterator = firstSheet.iterator();
        int rowCount = 0;
        Map<String, String> excludeResponseMap = new HashMap<>();
        Map<String, String> cucumblanMap = getCucumblan();
        while (iterator.hasNext()) {
          JSONArray virtualanArray = new JSONArray();
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
          if (rowCount > 0 && (generatedTestCaseList == null || generatedTestCaseList
              .contains(dataMap.get("TestCaseName")))) {
            buildVirtualanCollection(basePath, generatedPath, rowCount, cucumblanMap,
                excludeResponseMap,
                virtualanArray,
                dataMap);
          }
          rowCount++;
        }
        createPrpos(generatedPath, cucumblanMap, "cucumblan.properties");
        if (!excludeResponseMap.isEmpty()) {
          createPrpos(generatedPath, excludeResponseMap, "exclude-response.properties");
        }
        workbook.close();
        stream.close();
      } else {
        log.error(
            "Unable to create collection for the given excel file " + excelFilePath + " <<< ");
      }
    } catch (Exception e) {
      log.error(
          "Unable to create collection for the given excel file " + excelFilePath + " >>> " + e
              .getMessage());
    }
  }

  private static InputStream getInputStream(String basePath, String fileNameWithSubCategory)
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
    return stream;
  }

  private static String getFileAsString(String basePath, String fileNameWithSubCategory)
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
    return convertStreamToString(stream);
  }


  private static Map<String, String> getCucumblan() {
    Map<String, String> cucumblanMap = new HashMap<>();
    cucumblanMap.put("virtualan.data.load", "");
    cucumblanMap.put("virtualan.data.heading", "");
    cucumblanMap.put("virtualan.data.type", "VIRTUALAN");
    return cucumblanMap;
  }

  private static void buildVirtualanCollection(String basePath, String generatedPath, int rowCount,
      Map<String, String> cucumblanMap, Map<String, String> excludeResponseMap,
      JSONArray virtualanArray, Map<String, String> dataMap) throws MalformedURLException {
    JSONObject virtualanObj = new JSONObject();
    JSONArray paramsArray = new JSONArray();
    virtualanObj.put("contentType", getContentType(dataMap.get("ContentType")));
    buildHeaderParam("contentType", dataMap.get("ContentType"), paramsArray);
    createProcessingType(dataMap, paramsArray, "RequestProcessingType");
    createProcessingType(dataMap, paramsArray, "ResponseProcessingType");
    virtualanObj.put("scenario", dataMap.get("TestCaseNameDesc"));
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

    virtualanArray.put(virtualanObj);
    createIdaithalamProcessingFile(generatedPath, rowCount, cucumblanMap, virtualanArray, dataMap,
        virtualanObj);
    log.info(virtualanArray.toString());
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
      JSONArray paramsArray, String requestProcessingType) {
    if (dataMap.get(requestProcessingType) != null) {
      String[] processType = dataMap.get(requestProcessingType).split("=");
      if (processType.length == 2) {
        buildHeaderParam(processType[0], processType[1], paramsArray);
      }
    }
  }

  private static void createIdaithalamProcessingFile(String generatedPath, int rowCount,
      Map<String, String> cucumblanMap, JSONArray virtualanArray, Map<String, String> dataMap,
      JSONObject virtualanObj) {
    String fileCreated = generateExcelJson(generatedPath, virtualanArray,
        "Virtualan_" + dataMap.get("TestCaseName") + "_" + rowCount);
    if (fileCreated != null) {
      String filesCreated = cucumblanMap.get("virtualan.data.load");
      cucumblanMap.put("virtualan.data.load", filesCreated + fileCreated + ";");
      String headings = cucumblanMap.get("virtualan.data.heading");
      cucumblanMap
          .put("virtualan.data.heading", headings + virtualanObj.get("scenario") + ";");
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

  private static void buildHeaderParam(String key, String value, JSONArray paramsArray) {
    JSONObject virtualanObjParam = new JSONObject();
    virtualanObjParam.put("key", key);
    virtualanObjParam.put("value", value);
    virtualanObjParam.put("parameterType", "HEADER_PARAM");
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
