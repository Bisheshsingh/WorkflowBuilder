package org.workflow.manager.handlers;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.contexts.WorkflowExecutionContext;
import org.workflow.manager.contexts.WorkflowNodeExecutionContext;
import org.workflow.manager.exceptions.AnnotationHandleException;
import org.workflow.manager.exceptions.WorkflowException;
import org.workflow.manager.executors.WorkflowNodeExecutor;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.WorkflowResponse;
import org.workflow.manager.models.WorkflowResponseHandler;
import org.workflow.manager.responses.FailedWorkflowResponse;
import org.workflow.manager.tools.WorkflowOrchestrator;
import org.workflow.manager.workflow_nodes.WorkflowNode;

import java.util.Set;

@Slf4j
public class SuccessfulWorkflowResponseHandler<C extends ContextObject> extends WorkflowResponseHandler<C> {
    @Inject
    private WorkflowNodeExecutor<C> workflowNodeExecutor;

    @Override
    public Void handle(final WorkflowExecutionContext<C> input) {
        final Set<WorkflowNode<C>> nodes =
                input.getConfig().getResponseActions().get(input.getResponse());

        Runnable runnable = null;

        for (final WorkflowNode<C> node : nodes) {
            runnable = () -> {
                final WorkflowNodeExecutionContext<C> context = new WorkflowNodeExecutionContext<>(
                        node, input.getResponse(), input.getLevel(), input.getContext()
                );

                WorkflowResponse response = null;

                try {
                    response = workflowNodeExecutor.execute(context);
                } catch (WorkflowException | AnnotationHandleException e) {
                    response = new FailedWorkflowResponse("FAILED_TO_EXECUTE_THE_NODE", e);
                }

                WorkflowExecutionContext<C> newContext = null;

                try {
                    newContext = input.clone();
                    newContext.setResponse(response);
                    newContext.setWorkflowName(context.getNode().getServiceType().getSimpleName());
                } catch (CloneNotSupportedException e) {
                    log.error("Unable to clone the execution context in {} because of {}",
                            context.getNode().getServiceType().getSimpleName(), e.toString());
                    response = new FailedWorkflowResponse("EMERGENCY_EXIT");
                    newContext = input;
                    newContext.setResponse(response);
                }

                WorkflowOrchestrator.orchestrate(newContext);
            };

            input.getWorkflowOperation().operate(runnable);
        }

        return null;
    }
}
