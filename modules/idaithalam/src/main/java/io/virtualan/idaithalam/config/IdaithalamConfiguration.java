package io.virtualan.idaithalam.config;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * The type IdaithalamConfiguration configuration.
 *
 * @author Elan Thangamani
 */
public class IdaithalamConfiguration {
    private static final Properties properties = new Properties();

    static {
        reload();
    }

    /**
     * Reload.
     */
    public static void reload() {
        try {
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("idaithalam.properties");
            if (stream == null) {
                stream = IdaithalamConfiguration.class.getClassLoader().getResourceAsStream("idaithalam.properties");
            }
            if (stream != null) {
                properties.load(stream);
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
        return (Map) properties;
    }

    /**
     * Sets property.
     *
     * @param idaithalamProperies the idaithalam properies
     */
    public static void setProperties(Map<String, String> idaithalamProperies) {
        properties.putAll(idaithalamProperies);
    }

    /**
     * Gets properties.
     *
     * @param key   the key
     * @param value the value
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

    /**
     * Is work flow boolean.
     *
     * @return the boolean
     */
    public static boolean isWorkFlow() {
        return properties.getProperty("workflow") == null || !properties.getProperty("workflow").equalsIgnoreCase("Enabled");
    }

    public static boolean isReportEnabled() {
        return properties.getProperty("generateReport") == null || !properties.getProperty("generateReport").equalsIgnoreCase("Disabled");
    }
}
