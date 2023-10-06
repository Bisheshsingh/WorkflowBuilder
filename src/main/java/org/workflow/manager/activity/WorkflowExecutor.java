package org.workflow.manager.activity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.constants.RunLevels;
import org.workflow.manager.constants.WorkflowStatus;
import org.workflow.manager.exceptions.WorkflowException;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.EndWorkflowResponse;
import org.workflow.manager.models.FailedHandlerContextObject;
import org.workflow.manager.models.FailedWorkflowResponse;
import org.workflow.manager.models.SuccessWorkflowResponse;
import org.workflow.manager.models.Workflow;
import org.workflow.manager.models.WorkflowResponse;

@Slf4j
@Getter
public class WorkflowExecutor<C extends ContextObject> {
    private volatile WorkflowStatus workflowStatus;
    private volatile WorkflowResponse workflowResponse;

    private void handleWorkflowResponse(final WorkflowResponse response, final C context,
                                        final String level, final Workflow<C> workflow) {
        Boolean status = Boolean.TRUE;
        workflowResponse = response;
        if (response instanceof FailedWorkflowResponse) {
            log.error("Workflow failed because of {} : {}",
                    response.getStateName(), ((FailedWorkflowResponse) response).getErrorMessage());

            if (workflow.getFailedResponseActions().get(response) == null) {
                workflowStatus = WorkflowStatus.FAILED;
            }
        } else if (response instanceof EndWorkflowResponse) {
            log.info("Workflow Completed Successfully with response : {}", response);
            workflowStatus = WorkflowStatus.SUCCESSFUL;
        } else if (!(response instanceof SuccessWorkflowResponse)) {
            log.error("Unknown type of response in {}", response);
            workflowStatus = WorkflowStatus.FAILED;
        } else if (workflow.getResponseActions().get(response) == null) {
            log.error("Unknown response error in {}", response);
            workflowStatus = WorkflowStatus.FAILED;
        } else {
            status = Boolean.FALSE;
        }

        if (!workflowStatus.equals(WorkflowStatus.IN_PROGRESS)) {
            return;
        } else if(status) {
            handleFailedResponse(new FailedWorkflowResponse(response), context, level, workflow);
        } else {
            handleSuccessfulResponse(new SuccessWorkflowResponse(response), context, level, workflow);
        }
    }

    private void handleFailedResponse(final FailedWorkflowResponse currentResponse,
                                      final C context, final String level, final Workflow<C> workflow) {
        final FailedHandlerContextObject<C> contextObject = new FailedHandlerContextObject<>();
        contextObject.setException(new WorkflowException(currentResponse));
        contextObject.setContextObject(context);

        for (FailedWorkflowNode<C> node : workflow.getFailedResponseActions().get(currentResponse)) {
            new Thread(() -> {
                node.markADependency(currentResponse);

                if (node.areAllDependencyRan()) {
                    final String serviceName = node.getServiceType().getSimpleName();
                    log.info("Executing {} Failed Handler Service", serviceName);

                    final WorkflowResponse response = node.execute(contextObject, level);

                    log.info("{} Failed Handler Service ran with response : {}", serviceName, response);
                    handleWorkflowResponse(response, context, level, workflow);
                }
            }).start();
        }
    }

    private void handleSuccessfulResponse(final SuccessWorkflowResponse currentResponse, final C context,
                                          final String level, final Workflow<C> workflow) {
        for (WorkflowNode<C> node : workflow.getResponseActions().get(currentResponse)) {
            new Thread(() -> {
                node.markADependency(currentResponse);

                if (node.areAllDependencyRan()) {
                    final String serviceName = node.getServiceType().getSimpleName();
                    log.info("Executing {} service", serviceName);

                    final WorkflowResponse response = node.execute(context, level);

                    log.info("{} service ran with response : {}", serviceName, response);
                    handleWorkflowResponse(response, context, level, workflow);
                }
            }).start();
        }
    }

    private void waitForExecutionToComplete() throws InterruptedException {
        while (workflowStatus.equals(WorkflowStatus.IN_PROGRESS)) {
            Thread.sleep(10);
        }
    }

    public WorkflowExecutor() {
        workflowStatus = WorkflowStatus.PENDING;
        workflowResponse = new SuccessWorkflowResponse("Not Started");
    }

    public void execute(final Workflow<C> workflow, final C context,
                        final Boolean shouldWaitForExecutionToComplete,
                        final String level, final WorkflowResponse... responses) throws InterruptedException {
        workflowStatus = WorkflowStatus.IN_PROGRESS;
        for (final WorkflowResponse response : responses) {
            new Thread(() -> {
                handleWorkflowResponse(response, context, level, workflow);
            }).start();
        }

        if (shouldWaitForExecutionToComplete) {
            waitForExecutionToComplete();
        }
    }

    public void execute(final Workflow<C> workflow, final C context,
                        final Boolean shouldWaitForExecutionToComplete,
                        final WorkflowResponse... responses) throws InterruptedException {
        execute(workflow, context,
                shouldWaitForExecutionToComplete, RunLevels.DEFAULT, responses);
    }
}
