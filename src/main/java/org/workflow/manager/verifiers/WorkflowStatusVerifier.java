package org.workflow.manager.verifiers;

import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.constants.WorkflowStatus;
import org.workflow.manager.contexts.WorkflowExecutionContext;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.Verifier;
import org.workflow.manager.responses.EndWorkflowResponse;
import org.workflow.manager.responses.FailedWorkflowResponse;
import org.workflow.manager.responses.SuccessWorkflowResponse;

@Slf4j
public class WorkflowStatusVerifier<C extends ContextObject>
        implements Verifier<WorkflowExecutionContext<C>, WorkflowStatus> {
    @Override
    public WorkflowStatus verify(final WorkflowExecutionContext<C> context) {
        WorkflowStatus status = WorkflowStatus.IN_PROGRESS;

        if (context.getResponse() instanceof FailedWorkflowResponse) {
            status = WorkflowStatus.FAILED;
            log.error("Workflow failed because of {} : {}",
                    context.getResponse().getStateName(), new FailedWorkflowResponse(context.getResponse()).getErrorMessage());
        } else if (context.getResponse() instanceof EndWorkflowResponse) {
            status = WorkflowStatus.SUCCESSFUL;
            log.info("Workflow Completed Successfully with response : {}", context.getResponse());
        } else if (!(context.getResponse() instanceof SuccessWorkflowResponse)) {
            status = WorkflowStatus.FORCE_CLOSE;
            log.error("Unknown type of response in {}", context.getResponse());
        } else if (context.getConfig().getResponseActions().get(context.getResponse()) == null) {
            status = WorkflowStatus.FORCE_CLOSE;
            log.error("Unknown response error in {}", context.getResponse());
        }

        return status;
    }
}
