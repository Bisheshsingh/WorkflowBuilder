package org.workflow.manager.workflow_operations;

import org.workflow.manager.models.WorkflowOperation;

import java.util.concurrent.ExecutorService;

public class CustomOperation implements WorkflowOperation {
    private final ExecutorService executorService;

    public CustomOperation(final ExecutorService executorService) {
        this.executorService = executorService;
    }


    @Override
    public void operate(Runnable runnable) {
        executorService.submit(runnable);
    }

    @Override
    public void shutDown() {
        executorService.shutdownNow();
    }
}
