package io.virtualan.idaithalam.core.api;

import io.virtualan.idaithalam.config.IdaithalamConfiguration;
import io.virtualan.idaithalam.core.domain.ExecutionPlanner;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class VirtualanTestPlanExecutor {

    private static final int NTHREDS = 1;

    public static boolean invoke(String configMapper) throws InterruptedException {
        Yaml yaml = new Yaml(new Constructor(ExecutionPlanner.class));
        InputStream inputStream = VirtualanTestPlanExecutor.class.getClassLoader()
                .getResourceAsStream(configMapper);
        if (inputStream != null) {
            ExecutionPlanner executionPlanner = yaml.load(inputStream);
            ExecutorService executor = Executors
                    .newFixedThreadPool(executionPlanner.getParallelExecution());
            if (executionPlanner.getIdaithalamProperties() != null
                    && !executionPlanner.getIdaithalamProperties().isEmpty()) {
                IdaithalamConfiguration.setProperties(executionPlanner.getIdaithalamProperties());
            }
            List<Future<Integer>> futures = new ArrayList<>();
            executionPlanner.getApiExecutor().stream().forEach(
                    x -> {
                        Callable worker = new ParallelExecutor(x);
                        Future future = executor.submit(worker);
                        futures.add(future);
                    });

            // This will make the executor accept no new threads
            // and finish all existing threads in the queue
            executor.shutdown();
            while (!executor.isTerminated()) {
            }
            // Wait until all threads are finish
            executor.awaitTermination(executionPlanner.getTimeout(), TimeUnit.MINUTES);
            boolean bool = futures.stream().allMatch(x -> {
                try {
                    return x.get() == 0;
                } catch (ExecutionException e) {
                    return false;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            });

            log.info("Finished all api execution");
            return bool;
        } else {
            log.error(configMapper + " file is missing!");
            return false;
        }
    }

}
