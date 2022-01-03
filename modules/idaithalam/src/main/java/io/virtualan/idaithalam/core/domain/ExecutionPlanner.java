package io.virtualan.idaithalam.core.domain;

import java.util.List;
import java.util.Map;

/**
 * The type Execution planner.
 */
public class ExecutionPlanner {

    @Override
    public String toString() {
        return "ExecutionPlanner{" +
            "parallelExecution=" + parallelExecution +
            ", apiExecutor=" + apiExecutor +
            '}';
    }

    private int parallelExecution;

    private List<ApiExecutorParam> apiExecutor;

    private List<UIExecutorParam> uiExecutor;

    private int timeout;

    private Map<String,String> idaithalamProperties;

    public List<UIExecutorParam> getUiExecutor() {
        return uiExecutor;
    }

    public void setUiExecutor(List<UIExecutorParam> uiExecutor) {
        this.uiExecutor = uiExecutor;
    }

    /**
     * Gets idaithalam properies.
     *
     * @return the idaithalam properies
     */
    public Map<String, String> getIdaithalamProperties() {
        return idaithalamProperties;
    }

    /**
     * Sets idaithalam properies.
     *
     * @param idaithalamProperties the idaithalam properies
     */
    public void setIdaithalamProperties(Map<String, String> idaithalamProperties) {
        this.idaithalamProperties = idaithalamProperties;
    }

    /**
     * Gets timeout.
     *
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets timeout.
     *
     * @param timeout the timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Gets parallel execution.
     *
     * @return the parallel execution
     */
    public int getParallelExecution() {
        return parallelExecution;
    }

    /**
     * Sets parallel execution.
     *
     * @param parallelExecution the parallel execution
     */
    public void setParallelExecution(int parallelExecution) {
        this.parallelExecution = parallelExecution;
    }

    /**
     * Gets api executor.
     *
     * @return the api executor
     */
    public List<ApiExecutorParam> getApiExecutor() {
        return apiExecutor;
    }

    /**
     * Sets api executor.
     *
     * @param apiExecutor the api executor
     */
    public void setApiExecutor(
        List<ApiExecutorParam> apiExecutor) {
        this.apiExecutor = apiExecutor;
    }
}