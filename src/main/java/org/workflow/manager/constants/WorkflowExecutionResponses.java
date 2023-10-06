package org.workflow.manager.constants;

import org.workflow.manager.models.SuccessWorkflowResponse;
import org.workflow.manager.models.WorkflowResponse;

public class WorkflowExecutionResponses {
    public static class SuccessfulResponse {
        final WorkflowResponse RESPONSE_HANDLE_SUCCESSFUL =
                new SuccessWorkflowResponse("RESPONSE_HANDLE_SUCCESSFUL");
        final WorkflowResponse VERIFY_PARAMETER_SUCCESSFUL =
                new SuccessWorkflowResponse("VERIFY_PARAMETER_SUCCESSFUL");
    }

    public static class FailedResponse {
        final WorkflowResponse VERIFY_PARAMETER_FAILED =
                new SuccessWorkflowResponse("VERIFY_PARAMETER_FAILED");
    }
}
