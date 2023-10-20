package org.workflow.manager.exceptions;

import lombok.Getter;
import org.workflow.manager.responses.FailedWorkflowResponse;
import org.workflow.manager.models.WorkflowResponse;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class WorkflowException extends Exception{
    private transient WorkflowResponse response;

    public WorkflowException(final FailedWorkflowResponse response) {
        super(String.format("Unable to run the service because of state : %s, reason : %s and stackTrace : %s",
                response.getStateName(), response.getErrorMessage(),
                response.getStackTraces() != null ?
                Arrays.stream(response.getStackTraces())
                        .map(StackTraceElement::toString).collect(Collectors.joining("\n")) : null));

        this.response = response;
    }

    public WorkflowException(final Exception e) {
        super(e);
    }
}
