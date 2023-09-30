package org.workflow.manager.activity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.constants.Enums.WorkflowStatus;
import org.workflow.manager.exceptions.ServiceException;
import org.workflow.manager.exceptions.WorkflowException;
import org.workflow.manager.models.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Slf4j
public class WorkflowNode<T> {
    private final List<WorkflowNode<T>> dependsOn;
    private final List<WorkflowNode<T>> nextNodes;
    private volatile Service<T> service;
    private volatile WorkflowStatus status;

    protected WorkflowNode<T> addNextNode(final WorkflowNode<T> nextNode) {
        nextNodes.add(nextNode);
        return this;
    }

    public WorkflowNode(final Service<T> service) {
        this.dependsOn = new ArrayList<>();
        this.nextNodes = new ArrayList<>();
        this.service = service;
        this.status = WorkflowStatus.PENDING;
    }

    public WorkflowNode<T> addDependency(final WorkflowNode<T> nextNode) {
        dependsOn.add(nextNode);
        nextNode.addNextNode(this);
        return this;
    }

    public Boolean areAllDependenciesRanSuccessfully() {
        for (final WorkflowNode<T> workflowNode : dependsOn) {
            if (!workflowNode.getStatus().equals(WorkflowStatus.SUCCESSFUL)) {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    public void execute(final T input) throws WorkflowException {
        try {
            status = WorkflowStatus.IN_PROGRESS;
            log.info("{} service ran with status : {}",
                    service.getClass().getSimpleName(), status);
            service.run(input);
            status = WorkflowStatus.SUCCESSFUL;
            log.info("{} service ran successfully",
                    service.getClass().getSimpleName());
        } catch (final Exception e) {
            status = WorkflowStatus.FAILED;
            throw new WorkflowException(e);
        }
    }
}
