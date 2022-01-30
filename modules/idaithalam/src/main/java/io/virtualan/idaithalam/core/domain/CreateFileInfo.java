package io.virtualan.idaithalam.core.domain;

import org.json.JSONArray;

import java.util.Map;

/**
 * The type Create file info.
 */
public class CreateFileInfo {

    private ApiExecutorParam generatedPath;
    private Map<String, String> cucumblanMap;
    private JSONArray virtualanArray;
    private String testcaseName;
    private String scenario;

    @Override
    public String toString() {
        return "CreateFileInfo{" +
                "generatedPath='" + generatedPath + '\'' +
                ", cucumblanMap=" + cucumblanMap +
                ", virtualanArray=" + virtualanArray +
                ", testcaseName='" + testcaseName + '\'' +
                ", scenario='" + scenario + '\'' +
                '}';
    }

    /**
     * Gets generated path.
     *
     * @return the generated path
     */
    public ApiExecutorParam getGeneratedPath() {
        return generatedPath;
    }

    /**
     * Sets generated path.
     *
     * @param generatedPath the generated path
     */
    public void setGeneratedPath(ApiExecutorParam generatedPath) {
        this.generatedPath = generatedPath;
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
     * Sets cucumblan map.
     *
     * @param cucumblanMap the cucumblan map
     */
    public void setCucumblanMap(Map<String, String> cucumblanMap) {
        this.cucumblanMap = cucumblanMap;
    }

    /**
     * Gets virtualan array.
     *
     * @return the virtualan array
     */
    public JSONArray getVirtualanArray() {
        return virtualanArray;
    }

    /**
     * Sets virtualan array.
     *
     * @param virtualanArray the virtualan array
     */
    public void setVirtualanArray(JSONArray virtualanArray) {
        this.virtualanArray = virtualanArray;
    }

    /**
     * Gets testcase name.
     *
     * @return the testcase name
     */
    public String getTestcaseName() {
        return testcaseName;
    }

    /**
     * Sets testcase name.
     *
     * @param testcaseName the testcase name
     */
    public void setTestcaseName(String testcaseName) {
        this.testcaseName = testcaseName;
    }

    /**
     * Gets scenario.
     *
     * @return the scenario
     */
    public String getScenario() {
        return scenario;
    }

    /**
     * Sets scenario.
     *
     * @param scenario the scenario
     */
    public void setScenario(String scenario) {
        this.scenario = scenario;
    }
}
