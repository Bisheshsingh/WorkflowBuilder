package org.workflow.manager.models;

import lombok.Getter;
import org.workflow.manager.workflow_nodes.FailedWorkflowNode;
import org.workflow.manager.workflow_nodes.WorkflowNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
public abstract class Workflow<C extends ContextObject> {
    protected Map<WorkflowResponse, Set<WorkflowNode<C>>> responseActions;
    protected Map<WorkflowResponse, Set<FailedWorkflowNode<C>>> failedResponseActions;

    protected Workflow() {
        this.responseActions = new HashMap<>();
        this.failedResponseActions = new HashMap<>();
    }
}
