package org.workflow.manager.executors;

import com.google.inject.Inject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.constants.WorkflowResponses;
import org.workflow.manager.contexts.WorkflowExecutionContext;
import org.workflow.manager.exceptions.BinderException;
import org.workflow.manager.handlers.FailedWorkflowResponseHandler;
import org.workflow.manager.handlers.SuccessfulWorkflowResponseHandler;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.WorkflowConfig;
import org.workflow.manager.models.WorkflowOperation;
import org.workflow.manager.models.WorkflowResponse;
import org.workflow.manager.tools.WorkflowOrchestrator;
import org.workflow.manager.verifiers.WorkflowStatusVerifier;
import org.workflow.manager.workflow_operations.AutoThreadOperation;

import java.util.HashSet;


@Slf4j
@Data
public class WorkflowExecutor<C extends ContextObject> {
    @Inject
    private SuccessfulWorkflowResponseHandler<C> successfulWorkflowResponseHandler;
    @Inject
    private FailedWorkflowResponseHandler<C> failedWorkflowResponseHandler;
    @Inject
    private WorkflowStatusVerifier<C> workflowStatusVerifier;

    private volatile Boolean lock;
    private volatile WorkflowResponse workflowResponse;

    private void waitForExecution() {
        while (lock) {
        }
    }

    public void notifyExecution() {
        lock = false;
    }

    public void updateWorkflowResponse(final WorkflowResponse response) {
        this.workflowResponse = response;
    }

    public void execute(final WorkflowExecutionContext<C> data) throws BinderException {
        data.getConfig().configure(null);
        data.getConfig().getResponseActions().put(WorkflowResponses.WAITING_RESPONSE, new HashSet<>());
        this.lock = true;

        WorkflowOrchestrator.init(this);
        WorkflowOrchestrator.orchestrate(data);
        waitForExecution();
        data.getWorkflowOperation().shutDown();
    }

    public void execute(final WorkflowConfig<C> config, final C context,
                        final WorkflowResponse startResponse) throws BinderException {
        final WorkflowExecutionContext<C> executionContext = new WorkflowExecutionContext<>(
                config, "default", context, new AutoThreadOperation());

        executionContext.setResponse(startResponse);
        execute(executionContext);
    }

    public void execute(final WorkflowConfig<C> config, final C context,
                        final WorkflowResponse startResponse, final String level) throws BinderException {
        final WorkflowExecutionContext<C> executionContext = new WorkflowExecutionContext<>(
                config, level, context, new AutoThreadOperation());
        executionContext.setResponse(startResponse);
        execute(executionContext);
    }

    public void execute(final WorkflowConfig<C> config, final C context,
                        final WorkflowResponse startResponse,
                        final WorkflowOperation workflowOperation) throws BinderException {
        final WorkflowExecutionContext<C> executionContext = new WorkflowExecutionContext<>(
                config, "default", context, workflowOperation);
        executionContext.setResponse(startResponse);
        execute(executionContext);
    }

    public void execute(final WorkflowConfig<C> config, final C context,
                        final WorkflowResponse startResponse,
                        final String level, final WorkflowOperation workflowOperation) throws BinderException {
        final WorkflowExecutionContext<C> executionContext = new WorkflowExecutionContext<>(
                config, level, context, workflowOperation);
        executionContext.setResponse(startResponse);
        execute(executionContext);
    }
}