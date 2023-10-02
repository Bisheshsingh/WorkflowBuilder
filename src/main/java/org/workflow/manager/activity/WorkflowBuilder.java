package org.workflow.manager.activity;

import org.workflow.manager.models.Workflow;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.WorkflowResponse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WorkflowBuilder<C extends ContextObject> extends Workflow<C> {
    private void addResponseActions(final WorkflowResponse response,
                                    final WorkflowNode<C> node) {
        if (responseActions.containsKey(response)) {
            responseActions.get(response).add(node);
        } else {
            final Set<WorkflowNode<C>> nodes = new HashSet<>();
            nodes.add(node);

            responseActions.put(response, nodes);
        }

        node.dependsOn(response);
    }

    public WorkflowBuilder() {
        super();
    }

    @SafeVarargs
    public final WorkflowBuilder<C> addTriggerResponseActions(final WorkflowResponse response,
                                                              final WorkflowNode<C>... nodes) {
        triggerResponses.add(response);
        Arrays.stream(nodes).forEach(node -> addResponseActions(response, node));

        return this;
    }

    public WorkflowBuilder<C> addEndResponseActions(final WorkflowResponse response) {
        endResponses.add(response);
        return this;
    }

    @SafeVarargs
    public final WorkflowBuilder<C> addDependentResponseActions(final WorkflowResponse response,
                                                                final WorkflowNode<C>... nodes) {
        Arrays.stream(nodes).forEach(node -> addResponseActions(response, node));

        return this;
    }
}
