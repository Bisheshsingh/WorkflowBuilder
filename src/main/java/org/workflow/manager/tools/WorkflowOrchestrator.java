package org.workflow.manager.tools;

import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.constants.WorkflowStatus;
import org.workflow.manager.contexts.WorkflowExecutionContext;
import org.workflow.manager.executors.WorkflowExecutor;
import org.workflow.manager.models.ContextObject;


@Slf4j
public class WorkflowOrchestrator {

    public static <C extends ContextObject> void orchestrate(final WorkflowExecutionContext<C> context) {
        WorkflowExecutor<C> workflowExecutor = context.getWorkflowExecutor();

        if(workflowExecutor == null || !workflowExecutor.getLock()) {
            log.error("Forcefully closing the workflowNode : {}", context.getWorkflowName());
            return;
        }

        final WorkflowStatus status = workflowExecutor
                .getWorkflowStatusVerifier().verify(context);

        workflowExecutor.updateWorkflowResponse(context.getResponse());

        switch (status) {
            case IN_PROGRESS:
                workflowExecutor.getSuccessfulWorkflowResponseHandler().handle(context);
                break;
            case SUCCESSFUL:
            default:
            case FORCE_CLOSE:
                workflowExecutor.notifyExecution();
                break;
            case FAILED:
                workflowExecutor.getFailedWorkflowResponseHandler().handle(context);
                break;
        }
    }
}
