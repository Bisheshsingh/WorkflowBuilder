package org.workflow.manager.models;

public class SuccessWorkflowResponse extends WorkflowResponse {
    public SuccessWorkflowResponse(final String stateName) {
        super(stateName);
    }

    public SuccessWorkflowResponse(final WorkflowResponse response) {
        super(response);
    }
}
