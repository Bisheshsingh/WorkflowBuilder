package org.workflow.manager.workflow_operations;

import lombok.Data;
import org.workflow.manager.models.WorkflowOperation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
public class BatchOperations implements WorkflowOperation {
    private final ExecutorService executorService;

    public BatchOperations(final Integer batchSize) {
        this.executorService = Executors.newFixedThreadPool(batchSize);
    }

    @Override
    public void operate(final Runnable runnable) {
        executorService.execute(runnable);
    }

    @Override
    public void shutDown() {
        executorService.shutdownNow();
    }
}
