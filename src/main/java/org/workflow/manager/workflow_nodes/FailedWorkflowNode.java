package org.workflow.manager.workflow_nodes;

import org.workflow.manager.models.ContextObject;
import org.workflow.manager.contexts.FailedHandlerContextObject;
import org.workflow.manager.models.Service;

public class FailedWorkflowNode<C extends ContextObject>
        extends WorkflowNode<FailedHandlerContextObject<C>> {
    public <T extends Service<FailedHandlerContextObject<C>>> FailedWorkflowNode(final Class<T> serviceType) {
        super(serviceType);
    }
}
