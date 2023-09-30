package org.workflow.manager.activity;

import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.constants.Enums;
import org.workflow.manager.exceptions.ListenerException;
import org.workflow.manager.exceptions.WorkflowException;

@Slf4j
public class WorkflowExecutor<T> {
    private static Boolean areAllWorkflowsFinished;

    private void executeRun(T input, final WorkflowNode<T> startNode,
                            final WorkflowNode<T> endNode) throws WorkflowException {
        if (areAllWorkflowsFinished) {
            return;
        }

        if (!startNode.areAllDependenciesRanSuccessfully()) {
            return;
        }

        startNode.execute(input);

        if(endNode.getStatus().equals(Enums.WorkflowStatus.SUCCESSFUL)
                || endNode.getStatus().equals(Enums.WorkflowStatus.FAILED)
                || startNode.getStatus().equals(Enums.WorkflowStatus.FAILED)) {
            areAllWorkflowsFinished = Boolean.TRUE;
        }

        for (final WorkflowNode<T> workflowNode : startNode.getNextNodes()) {
            new Thread(() -> {
                try {
                    executeRun(input, workflowNode, endNode);
                } catch (WorkflowException e) {
                    throw new RuntimeException(e);
                }

            }).start();
        }
    }

    private void waitingForAllWorkflowsToComplete() {
        while(!areAllWorkflowsFinished) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                log.error(e.toString());
            }
        }
    }

    public void execute(final T input, final WorkflowNode<T> startNode,
                        final WorkflowNode<T> endNode) throws WorkflowException {
        areAllWorkflowsFinished = false;
        executeRun(input, startNode, endNode);
        waitingForAllWorkflowsToComplete();
    }
}
