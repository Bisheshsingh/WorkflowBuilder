package org.workflow.manager.contexts;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.workflow.manager.executors.WorkflowExecutor;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.WorkflowConfig;
import org.workflow.manager.models.WorkflowOperation;
import org.workflow.manager.models.WorkflowResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class WorkflowExecutionContext<C extends ContextObject> extends ContextObject {
    private String workflowName;
    private WorkflowResponse response;
    private final WorkflowExecutor<C> workflowExecutor;
    private final WorkflowConfig<C> config;
    private final String level;
    private final C context;
    private final WorkflowOperation workflowOperation;

    @Override
    public WorkflowExecutionContext<C> clone() throws CloneNotSupportedException {
        super.clone();
        return new WorkflowExecutionContext<>(workflowExecutor, config, level, context, workflowOperation);
    }
}
