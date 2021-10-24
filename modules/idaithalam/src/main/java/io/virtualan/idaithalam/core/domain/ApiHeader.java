package io.virtualan.idaithalam.core.domain;

import java.util.List;
import java.util.Map;

public class ApiHeader {
    private List<Map<String, String>> headerList;
    private String overwrite;

    public String getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(String overwrite) {
        this.overwrite = overwrite;
    }

    public List<Map<String, String>> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<Map<String, String>> headerList) {
        this.headerList = headerList;
    }
}
