package io.virtualan.idaithalam.config;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * The type Application configuration.
 *
 * @author Elan Thangamani
 */
public class IdaithalamConfiguration {
  private static Properties properties = new Properties();
  static {
    reload();
  }

  public static  void reload(){
    try {
      InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("idaithalam.properties");
      if(stream == null) {
        stream = IdaithalamConfiguration.class.getClassLoader().getResourceAsStream("idaithalam.properties");
      }
      if(stream != null) {
        properties.load(stream);
      } else {
        properties.put("workflow", "Enabled");
      }
    } catch (Exception e) {

    }
  }
  /**
   * Gets properties.
   *
   * @return the properties
   */
  public static Map<String, String> getProperties() {
    return (Map)properties;
  }


  /**
   * Gets properties.
   *
   */
  public static void setProperty(String key, String value) {
    properties.put(key, value);
  }


  /**
   * Gets property.
   *
   * @param keyName the key name
   * @return the property
   */
  public static String getProperty(String keyName) {
    return properties.getProperty(keyName);
  }

  public static boolean isWorkFlow() {
    return  properties.getProperty("workflow") != null ?   properties.getProperty("workflow").equalsIgnoreCase("Enabled") : false;
  }

}
