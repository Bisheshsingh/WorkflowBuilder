package org.workflow.manager.exceptions;

import lombok.Getter;
import org.workflow.manager.models.FailedWorkflowResponse;
import org.workflow.manager.models.WorkflowResponse;

@Getter
public class WorkflowException extends Exception{
    private WorkflowResponse response;

    public WorkflowException(final FailedWorkflowResponse response) {
        super(String.format("Unable to run the service because of state : %s\n and reason : %s",
                response.getStateName(), response.getErrorMessage()));
        this.response = response;
    }

    public WorkflowException(final Exception e) {
        super(e);
    }
}
