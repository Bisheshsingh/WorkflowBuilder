package org.workflow.manager.tools;

import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.constants.WorkflowStatus;
import org.workflow.manager.contexts.WorkflowExecutionContext;
import org.workflow.manager.executors.WorkflowExecutor;
import org.workflow.manager.models.ContextObject;

@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
public class WorkflowOrchestrator {
    private static WorkflowExecutor workflowExecutor;

    public static <C extends ContextObject> void init(final WorkflowExecutor<C> workflowExecutor1) {
        workflowExecutor = workflowExecutor1;
    }

    public static <C extends ContextObject> void orchestrate(final WorkflowExecutionContext<C> context) {
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
            case FORCE_CLOSE:
                workflowExecutor.notifyExecution();
                break;
            case FAILED:
                workflowExecutor.getFailedWorkflowResponseHandler().handle(context);
                break;
            default:
                break;
        }
    }
}
