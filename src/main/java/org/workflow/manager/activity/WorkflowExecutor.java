package org.workflow.manager.activity;

import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.constants.WorkflowStatus;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.FailedWorkflowResponse;
import org.workflow.manager.models.Workflow;
import org.workflow.manager.models.WorkflowResponse;

@Slf4j
public class WorkflowExecutor<C extends ContextObject> {
    private volatile WorkflowStatus WORKFLOW_STATUS;

    private Boolean handleWorkflowResponse(final WorkflowResponse response,
                                           final Workflow<C> workflow) {
        Boolean status = Boolean.TRUE;
        if (response instanceof FailedWorkflowResponse) {
            log.error("Workflow failed because of {}", response);
            WORKFLOW_STATUS = WorkflowStatus.FAILED;
        } else if (workflow.getEndResponses().contains(response)) {
            log.info("Workflow Completed Successfully with response : {}", response);
            WORKFLOW_STATUS = WorkflowStatus.SUCCESSFUL;
        } else if (workflow.getResponseActions().get(response) == null) {
            log.error("Unknown Listener error in {}", response);
            WORKFLOW_STATUS = WorkflowStatus.FAILED;
        } else {
            status = Boolean.FALSE;
        }

        return status;
    }

    private void emitResponse(final WorkflowResponse currentResponse,
                              final Workflow<C> workflow, final C context) {
        if (!WORKFLOW_STATUS.equals(WorkflowStatus.IN_PROGRESS)
                || handleWorkflowResponse(currentResponse, workflow)) {
            return;
        }

        for (WorkflowNode<C> node : workflow.getResponseActions().get(currentResponse)) {
            new Thread(() -> {
                node.markADependency(currentResponse);

                if (node.areAllDependencyRan()) {
                    final String serviceName = node.getServiceType().getSimpleName();
                    log.info("Executing {} service", serviceName);

                    final WorkflowResponse response = node.execute(context);

                    log.info("{} service ran with response : {}", serviceName, response);
                    emitResponse(response, workflow, context);
                }
            }).start();
        }
    }

    private void waitForExecutionToComplete() throws InterruptedException {
        while (WORKFLOW_STATUS.equals(WorkflowStatus.IN_PROGRESS)) {
            Thread.sleep(10);
        }
    }

    public WorkflowExecutor() {
        WORKFLOW_STATUS = WorkflowStatus.PENDING;
    }

    public void execute(final Workflow<C> workflow, final C context) {
        WORKFLOW_STATUS = WorkflowStatus.IN_PROGRESS;
        for (final WorkflowResponse response : workflow.getTriggerResponses()) {
            new Thread(() -> {
                emitResponse(response, workflow, context);
            }).start();
        }
    }

    public void execute(final Workflow<C> workflow, final C context,
                        final Boolean shouldWaitForExecutionToComplete) throws InterruptedException {
        execute(workflow, context);

        if (shouldWaitForExecutionToComplete) {
            waitForExecutionToComplete();
        }
    }
}
