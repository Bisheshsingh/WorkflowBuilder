package org.workflow.manager.verifiers;

import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.Verifier;
import org.workflow.manager.contexts.WorkflowNodeExecutionContext;
import org.workflow.manager.workflow_nodes.WorkflowNode;
import org.workflow.manager.models.WorkflowResponse;

public class WorkflowNodeExecutionVerifier<C extends ContextObject>
        implements Verifier<WorkflowNodeExecutionContext<C>, Boolean> {

    @Override
    public Boolean verify(final WorkflowNodeExecutionContext<C> context) {
        final WorkflowNode<C> workflowNode = context.getNode();
        final WorkflowResponse response = context.getResponse();

        return workflowNode.getDirectDependencies().contains(response)
                || workflowNode.getWaitingDependencies().equals(workflowNode.getMarkedDependencies());
    }
}
