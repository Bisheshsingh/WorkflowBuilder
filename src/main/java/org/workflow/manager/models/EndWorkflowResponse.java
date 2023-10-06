package org.workflow.manager.models;

public class EndWorkflowResponse extends WorkflowResponse {
    public EndWorkflowResponse(final String stateName) {
        super(stateName);
    }

    public EndWorkflowResponse(final WorkflowResponse response) {
        super(response);
    }
}
