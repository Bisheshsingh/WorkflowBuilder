package org.workflow.manager.constants;

import org.workflow.manager.models.WorkflowResponse;

public class WorkflowResponses {
    public static final WorkflowResponse FAILED_WORKFLOW =
            new WorkflowResponse("FAILED TO RUN THE WORKFLOW");
    public static final WorkflowResponse START_WORKFLOW =
            new WorkflowResponse("WORKFLOW INITIALIZED");
}
