package org.workflow.manager.contexts;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.WorkflowResponse;
import org.workflow.manager.workflow_nodes.WorkflowNode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WorkflowNodeExecutionContext<C extends ContextObject> extends ContextObject {
    private final WorkflowNode<C> node;
    private final WorkflowResponse response;
    private final String level;
    private final C context;
}
