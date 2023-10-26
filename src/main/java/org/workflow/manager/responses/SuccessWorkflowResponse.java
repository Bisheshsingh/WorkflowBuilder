package org.workflow.manager.responses;

import lombok.NonNull;
import org.workflow.manager.models.WorkflowResponse;

public class SuccessWorkflowResponse extends WorkflowResponse {
    public SuccessWorkflowResponse(@NonNull final String stateName) {
        super(stateName);
    }

    public SuccessWorkflowResponse(final WorkflowResponse response) {
        super(response);
    }
}
