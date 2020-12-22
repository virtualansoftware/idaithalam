package io.virtualan.idaithalam.core.domain;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Map;

public class OperationBuilder {
    private final String url;
    private final Map<String, Schema> definitions;
    private final PathItem mapPath;
    private final Operation operation;

    public OperationBuilder(String url, Map<String, Schema> definitions, PathItem mapPath, Operation operation) {
        this.url = url;
        this.definitions = definitions;
        this.mapPath = mapPath;
        this.operation = operation;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Schema> getDefinitions() {
        return definitions;
    }

    public PathItem getMapPath() {
        return mapPath;
    }

    public Operation getOperation() {
        return operation;
    }
}
