package io.virtualan.idaithalam.core.generator;

import java.util.Map;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * The type Sheet object.
 */
public class SheetObject {

  private  String basePath;
  private  Map<String, String> excludeResponseMap;
  private  Map<String, String> cucumblanMap;
  private  Sheet firstSheet;

  /**
   * Sets base path.
   *
   * @param basePath the base path
   */
  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  /**
   * Sets exclude response map.
   *
   * @param excludeResponseMap the exclude response map
   */
  public void setExcludeResponseMap(Map<String, String> excludeResponseMap) {
    this.excludeResponseMap = excludeResponseMap;
  }

  /**
   * Sets cucumblan map.
   *
   * @param cucumblanMap the cucumblan map
   */
  public void setCucumblanMap(Map<String, String> cucumblanMap) {
    this.cucumblanMap = cucumblanMap;
  }

  /**
   * Sets first sheet.
   *
   * @param firstSheet the first sheet
   */
  public void setFirstSheet(Sheet firstSheet) {
    this.firstSheet = firstSheet;
  }

  /**
   * Gets base path.
   *
   * @return the base path
   */
  public String getBasePath() {
    return basePath;
  }

  /**
   * Gets exclude response map.
   *
   * @return the exclude response map
   */
  public Map<String, String> getExcludeResponseMap() {
    return excludeResponseMap;
  }

  /**
   * Gets cucumblan map.
   *
   * @return the cucumblan map
   */
  public Map<String, String> getCucumblanMap() {
    return cucumblanMap;
  }

  /**
   * Gets first sheet.
   *
   * @return the first sheet
   */
  public Sheet getFirstSheet() {
    return firstSheet;
  }


}
