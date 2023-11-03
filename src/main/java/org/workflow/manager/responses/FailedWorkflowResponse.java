package org.workflow.manager.responses;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.workflow.manager.models.WorkflowResponse;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
@Setter
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
        this.errorMessage = e.toString();
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

    @Override
    public String toString() {
        final String trace = (stackTraces == null) ? "NO TRACE"
                : Arrays.stream(stackTraces)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));

        return "FailedWorkflowResponse(" +
                "stateName='" + stateName + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", stackTraces=" + trace +
                ')';
    }
}
