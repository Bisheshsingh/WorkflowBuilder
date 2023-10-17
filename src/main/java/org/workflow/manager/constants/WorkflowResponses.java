package org.workflow.manager.constants;

import org.workflow.manager.models.WorkflowResponse;
import org.workflow.manager.responses.SuccessWorkflowResponse;

public class WorkflowResponses {
    public static final WorkflowResponse WAITING_RESPONSE =
            new SuccessWorkflowResponse("Waiting for other responses");
}
