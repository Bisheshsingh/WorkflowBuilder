package org.workflow.manager.executors;

import org.workflow.manager.contexts.FailedHandlerContextObject;
import org.workflow.manager.models.ContextObject;

public class FailedWorkflowNodeExecutor <C extends ContextObject>
        extends WorkflowNodeExecutor<FailedHandlerContextObject<C>> {
}
