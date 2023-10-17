package org.workflow.manager.models;

import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.exceptions.ServiceException;
import org.workflow.manager.responses.FailedWorkflowResponse;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public abstract class Service<T extends ContextObject> {
    public WorkflowResponse run(T input) {
        WorkflowResponse response = null;

        try {
            response = performAction(input);
        } catch (final ServiceException e) {
            final String stackMsg = Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\n"));

            log.error("Failed to run the service");
            log.error("Reason {}", e.getMessage());
            log.error("Stack Trace {}", stackMsg);

            response = new FailedWorkflowResponse("FAILED_TO_RUN_THE_SERVICE", e);
        }

        return response;
    }

    protected abstract WorkflowResponse performAction(T input) throws ServiceException;
}
