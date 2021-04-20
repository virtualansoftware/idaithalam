package io.virtualan.idaithalam.core.api;

import io.virtualan.idaithalam.core.domain.ExecutionPlanner;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

@Slf4j
public class MassApiExecutor {

  private static final int NTHREDS = 1;

  public static boolean invoke(String configMapper) throws InterruptedException {
    Yaml yaml = new Yaml(new Constructor(ExecutionPlanner.class));
    InputStream inputStream = MassApiExecutor.class.getClassLoader()
        .getResourceAsStream(configMapper);
    ExecutionPlanner executionPlanner = yaml.load(inputStream);
    ExecutorService executor = Executors
        .newFixedThreadPool(executionPlanner.getApiExecutor().size());
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
    boolean bool = futures.stream().anyMatch(x -> {
      try {
        return x.get() != 0;
      } catch (InterruptedException | ExecutionException e) {
        return false;
      }
    });

    log.info("Finished all api execution");
    return true;
  }

}