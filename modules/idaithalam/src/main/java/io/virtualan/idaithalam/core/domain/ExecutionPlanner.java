package io.virtualan.idaithalam.core.domain;

import java.util.List;

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
    private int timeout;

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