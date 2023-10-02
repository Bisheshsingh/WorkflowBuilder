package org.workflow.manager.activity;

import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.constants.WorkflowStatus;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.FailedWorkflowResponse;
import org.workflow.manager.models.Workflow;
import org.workflow.manager.models.WorkflowResponse;

@Slf4j
public class WorkflowExecutor<C extends ContextObject> {
    private static WorkflowStatus WORKFLOW_STATUS;
    private final Workflow<C> workflow;

    private void emitResponse(final WorkflowResponse currentResponse, final C context) {
        if (workflow.getResponseActions().get(currentResponse) == null
                || !WORKFLOW_STATUS.equals(WorkflowStatus.IN_PROGRESS)) {
            log.info("There is no listener for {}", currentResponse);
            return;
        }

        for (WorkflowNode<C> node : workflow.getResponseActions().get(currentResponse)) {
            new Thread(() -> {
                node.markADependency(currentResponse);

                if (node.areAllDependencyRan()) {
                    final String serviceName = node.getServiceType().getSimpleName();
                    log.info("Executing {} service", serviceName);

                    final WorkflowResponse response = node.execute(context);

                    if (!WORKFLOW_STATUS.equals(WorkflowStatus.IN_PROGRESS)) {
                        return;
                    } else if (response instanceof FailedWorkflowResponse) {
                        WORKFLOW_STATUS = WorkflowStatus.FAILED;
                    } else if (workflow.getEndResponses().contains(response)) {
                        WORKFLOW_STATUS = WorkflowStatus.SUCCESSFUL;
                    }

                    log.info("{} service ran with response : {}", serviceName, response);
                    emitResponse(response, context);
                }
            }).start();
        }
    }

    public WorkflowExecutor(final Workflow<C> workflow) {
        this.workflow = workflow;
        WORKFLOW_STATUS = WorkflowStatus.PENDING;
    }

    public void execute(final C context) {
        WORKFLOW_STATUS = WorkflowStatus.IN_PROGRESS;
        for (final WorkflowResponse response : workflow.getTriggerResponses()) {
            new Thread(() -> {
                emitResponse(response, context);
            }).start();
        }
    }
}
