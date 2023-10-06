package org.workflow.manager.activity;

import org.workflow.manager.models.Workflow;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.WorkflowResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkflowBuilder<C extends ContextObject> extends Workflow<C> {
    public void addResponseAction(final WorkflowResponse response,
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

    @SafeVarargs
    public final WorkflowBuilder<C> addResponseActions(final WorkflowResponse response,
                                                       final WorkflowNode<C>... nodes) {
        Arrays.stream(nodes).forEach(node -> addResponseAction(response, node));

        return this;
    }

    public final WorkflowBuilder<C> addResponseActions(final WorkflowResponse response,
                                                       final List<WorkflowNode<C>> nodes) {
        nodes.forEach(node -> addResponseAction(response, node));

        return this;
    }

    public void addFailedAction(final WorkflowResponse response,
                                  final FailedWorkflowNode<C> node) {
        if (failedResponseActions.containsKey(response)) {
            failedResponseActions.get(response).add(node);
        } else {
            final Set<FailedWorkflowNode<C>> nodes = new HashSet<>();
            nodes.add(node);

            failedResponseActions.put(response, nodes);
        }

        node.dependsOn(response);
    }

    @SafeVarargs
    public final WorkflowBuilder<C> addFailedActions(final WorkflowResponse response,
                                                       final FailedWorkflowNode<C>... nodes) {
        Arrays.stream(nodes).forEach(node -> addFailedAction(response, node));

        return this;
    }

    public final WorkflowBuilder<C> addFailedActions(final WorkflowResponse response,
                                                       final List<FailedWorkflowNode<C>> nodes) {
        nodes.forEach(node -> addFailedAction(response, node));

        return this;
    }

    public static <C extends ContextObject> Workflow<C> clone(final Workflow<C> workflow) {
        final WorkflowBuilder<C> copyWorkflow = new WorkflowBuilder<>();

        workflow.getResponseActions().forEach((key, value) -> {
            copyWorkflow.addResponseActions(key, new ArrayList<>(value));
        });

        workflow.getFailedResponseActions().forEach((key, value) -> {
            copyWorkflow.addFailedActions(key, new ArrayList<>(value));
        });

        return copyWorkflow;
    }
}
