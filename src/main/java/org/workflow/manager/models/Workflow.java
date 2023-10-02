package org.workflow.manager.models;

import lombok.Data;
import org.workflow.manager.activity.WorkflowNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public abstract class Workflow<C extends ContextObject> {
    protected final Map<WorkflowResponse, Set<WorkflowNode<C>>> responseActions;
    protected final Set<WorkflowResponse> triggerResponses;
    protected final Set<WorkflowResponse> endResponses;

    protected Workflow() {
        this.responseActions = new HashMap<>();
        this.triggerResponses = new HashSet<>();
        this.endResponses = new HashSet<>();
    }
}
