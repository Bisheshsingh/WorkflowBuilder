package org.workflow.manager.responses;

import lombok.NonNull;
import org.workflow.manager.models.WorkflowResponse;

public class EndWorkflowResponse extends WorkflowResponse {
    public EndWorkflowResponse(@NonNull final String stateName) {
        super(stateName);
    }

    public EndWorkflowResponse(final WorkflowResponse response) {
        super(response);
    }
}
