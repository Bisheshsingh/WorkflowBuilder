package org.workflow.manager.models;

import org.workflow.manager.contexts.WorkflowExecutionContext;

public abstract class WorkflowResponseHandler<C extends ContextObject>
        implements Handler<WorkflowExecutionContext<C>, Void> {

}
