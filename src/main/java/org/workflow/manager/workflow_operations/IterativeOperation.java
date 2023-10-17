package org.workflow.manager.workflow_operations;

import org.workflow.manager.models.WorkflowOperation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IterativeOperation implements WorkflowOperation {
    private final ExecutorService executorService;

    public IterativeOperation() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void operate(final Runnable runnable) {
        executorService.submit(runnable);
    }

    @Override
    public void shutDown() {
        executorService.shutdownNow();
    }
}
