package org.workflow.manager.activity;

import org.workflow.manager.models.Workflow;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.WorkflowResponse;

import java.util.HashSet;
import java.util.Set;

public class WorkflowBuilder<C extends ContextObject> extends Workflow<C> {
    private WorkflowBuilder<C> addResponseActions(final WorkflowResponse response,
                                                  final WorkflowNode<C> node) {
        if (responseActions.containsKey(response)) {
            responseActions.get(response).add(node);
        } else {
            final Set<WorkflowNode<C>> nodes = new HashSet<>();
            nodes.add(node);

            responseActions.put(response, nodes);
        }

        node.dependsOn(response);
        return this;
    }

    public WorkflowBuilder() {
        super();
    }

    public WorkflowBuilder<C> addTriggerResponseActions(final WorkflowResponse response,
                                                        final WorkflowNode<C> node) {
        triggerResponses.add(response);
        return addResponseActions(response, node);
    }

    public WorkflowBuilder<C> addEndResponseActions(final WorkflowResponse response,
                                                    final WorkflowNode<C> node) {
        endResponses.add(response);
        return addResponseActions(response, node);
    }

    public WorkflowBuilder<C> addDependentResponseActions(final WorkflowResponse response,
                                                          final WorkflowNode<C> node) {
        return addResponseActions(response, node);
    }
}
