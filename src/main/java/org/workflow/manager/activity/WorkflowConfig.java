package org.workflow.manager.activity;

import com.google.inject.Module;
import lombok.Getter;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.FailedHandlerService;
import org.workflow.manager.models.FailedWorkflowResponse;
import org.workflow.manager.models.Service;
import org.workflow.manager.models.Workflow;
import org.workflow.manager.models.WorkflowResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public abstract class WorkflowConfig<C extends ContextObject> extends Workflow<C> {
    private Map<String, WorkflowNode<C>> nodeMap;
    private Map<String, FailedWorkflowNode<C>> failedHandlerNodeMap;

    protected WorkflowConfig() {
        this.nodeMap = new HashMap<>();
        this.failedHandlerNodeMap = new HashMap<>();
        initialize();
        configure();
    }

    protected final <T extends Service<C>> void addNode(final String nodeName, final Class<T> serviceClass,
                                                        final Module... modules) {
        nodeMap.put(nodeName, new WorkflowNode<>(serviceClass, modules));
    }

    protected final <T extends Service<C>> void addNode(final String nodeName, final Class<T> serviceClass) {
        nodeMap.put(nodeName, new WorkflowNode<>(serviceClass));
    }

    protected final void addNode(final String nodeName, final WorkflowNode<C> node) {
        nodeMap.put(nodeName, node);
    }

    protected final void addFailedHandlerNode(final String nodeName, final FailedWorkflowNode<C> node) {
        failedHandlerNodeMap.put(nodeName, node);
    }

    protected final <T extends FailedHandlerService<C>> void addFailedHandlerNode(final String nodeName,
                                                                                  final Class<T> serviceClass,
                                                                                  final Module... modules) {
        addFailedHandlerNode(nodeName, new FailedWorkflowNode<>(serviceClass, modules));
    }

    protected final <T extends FailedHandlerService<C>> void addFailedHandlerNode(final String nodeName,
                                                                                  final Class<T> serviceClass) {
        addFailedHandlerNode(nodeName, new FailedWorkflowNode<>(serviceClass));
    }

    protected final void addActions(final WorkflowResponse response,
                                    final List<WorkflowNode<C>> nodes) {
        for (final WorkflowNode<C> node : nodes) {
            if (!responseActions.containsKey(response)) {
                responseActions.put(response, new HashSet<>());
            }

            responseActions.get(response).add(node);
        }
    }

    @SafeVarargs
    protected final void addActions(final WorkflowResponse response,
                                    final WorkflowNode<C>... nodes) {
        addActions(response, new ArrayList<>(Arrays.asList(nodes)));
    }

    protected final void addActions(final WorkflowResponse response,
                                    final String... workflowNodeNames) {
        final List<WorkflowNode<C>> nodes = Arrays.stream(workflowNodeNames)
                .map(nodeMap::get).collect(Collectors.toList());

        addActions(response, nodes);
    }

    protected final void addFailedActions(final WorkflowResponse response,
                                          final List<FailedWorkflowNode<C>> nodes) {
        for (final FailedWorkflowNode<C> node : nodes) {
            if (!FailedHandlerService.class.isAssignableFrom(node.getServiceType())) {
                throw new ClassCastException("Cannot use other service classes " +
                        "for FailedWorkflowResponse except FailedHandlerService");
            }

            if (!failedResponseActions.containsKey(response)) {
                failedResponseActions.put(response, new HashSet<>());
            }

            failedResponseActions.get(response).add(node);
        }
    }

    @SafeVarargs
    protected final void addFailedActions(final WorkflowResponse response,
                                          final FailedWorkflowNode<C>... nodes) {
        addFailedActions(response, new ArrayList<>(Arrays.asList(nodes)));
    }

    protected final void addFailedActions(final WorkflowResponse response,
                                          final String... workflowNodeNames) {
        final List<FailedWorkflowNode<C>> nodes = Arrays.stream(workflowNodeNames)
                .map(failedHandlerNodeMap::get).collect(Collectors.toList());

        addFailedActions(response, nodes);
    }

    protected abstract void initialize();

    protected abstract void configure();
}
