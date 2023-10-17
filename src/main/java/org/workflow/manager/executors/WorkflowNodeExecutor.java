package org.workflow.manager.executors;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.processors.AnnotationProcessor;
import org.workflow.manager.verifiers.WorkflowNodeExecutionVerifier;
import org.workflow.manager.exceptions.AnnotationHandleException;
import org.workflow.manager.exceptions.WorkflowException;
import org.workflow.manager.contexts.AnnotationHandlerContext;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.Executor;
import org.workflow.manager.workflow_nodes.WorkflowNode;
import org.workflow.manager.responses.FailedWorkflowResponse;
import org.workflow.manager.models.Service;
import org.workflow.manager.contexts.WorkflowNodeExecutionContext;
import org.workflow.manager.models.WorkflowResponse;
import org.workflow.manager.tools.GuiceConfig;

import java.lang.reflect.InvocationTargetException;

import static org.workflow.manager.constants.WorkflowResponses.WAITING_RESPONSE;

@Slf4j
public class WorkflowNodeExecutor<C extends ContextObject>
        implements Executor<WorkflowResponse, WorkflowNodeExecutionContext<C>> {
    @Inject
    private WorkflowNodeExecutionVerifier<C> workflowNodeExecutionVerifier;
    @Inject
    private AnnotationProcessor annotationProcessor;

    private WorkflowResponse runService(final AnnotationHandlerContext annotationHandlerContext,
                                        final C context, final Service<C> service,
                                        final Integer retryCount) throws WorkflowException {
        WorkflowResponse response = null;

        try {

            response = (WorkflowResponse) annotationHandlerContext
                    .getMethod().invoke(service, context);

            if (response instanceof FailedWorkflowResponse) {
                throw new WorkflowException((FailedWorkflowResponse) response);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new WorkflowException(e);
        } catch (WorkflowException e) {
            final String id = service.getClass().getSimpleName();

            log.error("{} : Unable to run {}", id, id);
            log.error("{} : Response State : {}", id, e.getResponse().getStateName());
            log.error("{} : Response : {}", id, e.getResponse());

            if (retryCount <= annotationHandlerContext.getRetryCount()) {
                try {
                    Thread.sleep(annotationHandlerContext.getRetryCoolDownTime());
                } catch (InterruptedException ex) {
                    throw new WorkflowException(ex);
                }

                log.info("{} : Retry {}/{} Started", id, retryCount, annotationHandlerContext.getRetryCount());

                response = runService(annotationHandlerContext, context, service, retryCount + 1);
            } else {
                response = e.getResponse();
            }
        }

        return response;
    }

    @Override
    public WorkflowResponse execute(final WorkflowNodeExecutionContext<C> context)
            throws WorkflowException, AnnotationHandleException {
        final WorkflowNode<C> workflowNode = context.getNode();
        final String level = context.getLevel();

        log.info("Execution of {} service started!",
                workflowNode.getServiceType().getSimpleName());

        workflowNode.markResponseDependency(context.getResponse());

        if (!workflowNodeExecutionVerifier.verify(context)) {
            log.info("{} service ended with response {}!",
                    workflowNode.getServiceType().getSimpleName(), WAITING_RESPONSE);

            return WAITING_RESPONSE;
        }

        final AnnotationHandlerContext annotationHandlerContext =
                new AnnotationHandlerContext(workflowNode.getServiceType(), level);

        annotationProcessor.process(annotationHandlerContext);

        final Service<C> service = workflowNode.getService() == null ?
                GuiceConfig.init().getInstance(workflowNode.getServiceType())
                : workflowNode.getService();

        workflowNode.setService(service);

        final WorkflowResponse response =
                runService(annotationHandlerContext, context.getContext(), service, 1);

        log.info("{} service ended with response {}!",
                workflowNode.getServiceType().getSimpleName(), response);

        return response;
    }
}
