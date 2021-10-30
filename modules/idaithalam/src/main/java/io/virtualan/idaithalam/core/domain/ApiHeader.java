package io.virtualan.idaithalam.core.domain;

import java.util.List;
import java.util.Map;

public class ApiHeader {
    private List<Map<String, Object>> headerList;
    private String overwrite;

    public String getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(String overwrite) {
        this.overwrite = overwrite;
    }

    public List<Map<String, Object>> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<Map<String, Object>> headerList) {
        this.headerList = headerList;
    }
}
