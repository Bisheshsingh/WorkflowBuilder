package org.workflow.manager.responses;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.workflow.manager.models.WorkflowResponse;

@Getter
@Setter
@ToString
public class FailedWorkflowResponse extends WorkflowResponse {
    private String errorMessage;
    private StackTraceElement[] stackTraces;

    public FailedWorkflowResponse(@NonNull final String stateName, final String errorMessage,
                                  final StackTraceElement[] stackTraces) {
        super(stateName);
        this.errorMessage = errorMessage;
        this.stackTraces = stackTraces;
    }

    public FailedWorkflowResponse(@NonNull final String stateName, final Exception e) {
        super(stateName);
        this.stackTraces = e.getStackTrace();
        this.errorMessage = e.getMessage();
    }

    public FailedWorkflowResponse(@NonNull final String stateName) {
        super(stateName);
    }

    public FailedWorkflowResponse(final WorkflowResponse response) {
        super(response);

        if(response instanceof FailedWorkflowResponse) {
            FailedWorkflowResponse failedResponse = (FailedWorkflowResponse) response;
            this.errorMessage = failedResponse.getErrorMessage();
            this.stackTraces = failedResponse.getStackTraces();
        }
    }
}
