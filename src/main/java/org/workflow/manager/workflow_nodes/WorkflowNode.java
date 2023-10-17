package org.workflow.manager.workflow_nodes;

import lombok.Data;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.Service;
import org.workflow.manager.models.WorkflowResponse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Data
public class WorkflowNode<C extends ContextObject> {
    private final Set<WorkflowResponse> directDependencies;
    private final Set<WorkflowResponse> waitingDependencies;
    private final Set<WorkflowResponse> markedDependencies;
    private final Class<? extends Service<C>> serviceType;
    private Service<C> service;

    public <T extends Service<C>> WorkflowNode(final Class<T> serviceType) {
        this.directDependencies = new HashSet<>();
        this.waitingDependencies = new HashSet<>();
        this.markedDependencies = new HashSet<>();
        this.serviceType = serviceType;
    }

    public WorkflowNode<C> addDirectResponseDependency(final WorkflowResponse... responseDependencies) {
        if (responseDependencies != null) {
            this.directDependencies.addAll(Arrays.asList(responseDependencies));
        }

        return this;
    }

    public WorkflowNode<C> addWaitingResponseDependency(final WorkflowResponse... responseDependencies) {
        if (responseDependencies != null) {
            this.waitingDependencies.addAll(Arrays.asList(responseDependencies));
        }

        return this;
    }

    public WorkflowNode<C> markResponseDependency(final WorkflowResponse... responses) {
        this.markedDependencies.addAll(Arrays.asList(responses));
        return this;
    }

    public void resetMarked() {
        if (!markedDependencies.isEmpty()) {
            markedDependencies.clear();
        }
    }
}
