package org.workflow.manager.responses;

import org.workflow.manager.models.WorkflowResponse;

public class EndWorkflowResponse extends WorkflowResponse {
    public EndWorkflowResponse(final String stateName) {
        super(stateName);
    }

    public EndWorkflowResponse(final WorkflowResponse response) {
        super(response);
    }
}
