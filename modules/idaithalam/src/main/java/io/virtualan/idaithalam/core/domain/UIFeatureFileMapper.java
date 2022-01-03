package io.virtualan.idaithalam.core.domain;

import java.util.List;


public class UIFeatureFileMapper {
    private String jsonFileName;
    private List<UiItem> workflowItems;

    public UIFeatureFileMapper(String jsonFileName, List<UiItem> workflowItems) {
        this.jsonFileName = jsonFileName;
        this.workflowItems = workflowItems;
    }

    public String getJsonFileName() {
        return jsonFileName;
    }

    public void setJsonFileName(String jsonFileName) {
        this.jsonFileName = jsonFileName;
    }

    public List<UiItem> getWorkflowItems() {
        return workflowItems;
    }

    public void setWorkflowItems(List<UiItem> workflowItems) {
        this.workflowItems = workflowItems;
    }
}
