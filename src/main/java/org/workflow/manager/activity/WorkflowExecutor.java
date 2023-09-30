package org.workflow.manager.activity;

public class WorkflowExecutor<T> {
    public void execute(final T input, final WorkflowNode<T> startNode) {
        if (!startNode.areAllDependenciesRanSuccessfully()) {
            return;
        }

        startNode.execute(input);
        startNode.setIsWorkflowSuccessful();

        for (final WorkflowNode<T> workflowNode : startNode.getNextNodes()) {
            execute(input, workflowNode);
        }
    }
}
