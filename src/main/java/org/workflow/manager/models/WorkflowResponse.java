package org.workflow.manager.models;

import lombok.Data;
import lombok.NonNull;

import javax.print.DocFlavor;

@Data
public abstract class WorkflowResponse {
    protected final String stateName;

    protected WorkflowResponse(@NonNull final String stateName) {
        this.stateName = stateName;
    }
    protected  <T extends WorkflowResponse> WorkflowResponse(final T response) {
        this.stateName = response.getStateName();
    }
}
