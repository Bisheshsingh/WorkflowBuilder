package org.workflow.manager.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FailedWorkflowResponse extends WorkflowResponse {
    private String errorMessage;
    private StackTraceElement[] stackTraces;

    public FailedWorkflowResponse(final String stateName, final String errorMessage,
                                  final StackTraceElement[] stackTraces) {
        super(stateName);
        this.errorMessage = errorMessage;
        this.stackTraces = stackTraces;
    }

    public FailedWorkflowResponse(final String stateName, final Exception e) {
        super(stateName);
        this.stackTraces = e.getStackTrace();
        this.errorMessage = e.getMessage();
    }

    public FailedWorkflowResponse(final String stateName) {
        super(stateName);
    }

    public FailedWorkflowResponse(WorkflowResponse response) {
        super(response);

        if(response instanceof FailedWorkflowResponse) {
            FailedWorkflowResponse failedResponse = (FailedWorkflowResponse) response;
            this.errorMessage = failedResponse.getErrorMessage();
            this.stackTraces = failedResponse.getStackTraces();
            failedResponse = null; //De-Allocating the pointer
        }

        response = null;  //De-Allocating the pointer
    }
}
