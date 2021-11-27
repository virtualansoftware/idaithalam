package io.virtualan.idaithalam.core.domain;

import java.util.List;


public class FeatureFileMapper {
    private String jsonFileName;
    private List<Item> workflowItems;

    public FeatureFileMapper(String jsonFileName, List<Item> workflowItems) {
        this.jsonFileName = jsonFileName;
        this.workflowItems = workflowItems;
    }

    public String getJsonFileName() {
        return jsonFileName;
    }

    public void setJsonFileName(String jsonFileName) {
        this.jsonFileName = jsonFileName;
    }

    public List<Item> getWorkflowItems() {
        return workflowItems;
    }

    public void setWorkflowItems(List<Item> workflowItems) {
        this.workflowItems = workflowItems;
    }
}
