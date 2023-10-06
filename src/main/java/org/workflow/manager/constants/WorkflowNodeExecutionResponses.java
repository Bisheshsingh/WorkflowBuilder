package org.workflow.manager.constants;

import org.workflow.manager.models.EndWorkflowResponse;
import org.workflow.manager.models.FailedWorkflowResponse;
import org.workflow.manager.models.SuccessWorkflowResponse;
import org.workflow.manager.models.WorkflowResponse;

public class WorkflowNodeExecutionResponses {
    public static class SuccessfulResponses {
        public static final WorkflowResponse RETRY_SUCCESS =
                new SuccessWorkflowResponse("RETRY_SUCCESS");
        public static final WorkflowResponse PROXY_RUN_SUCCESS =
                new SuccessWorkflowResponse("PROXY_RUN_SUCCESS");
        public static final WorkflowResponse HANDLE_RETRY =
                new SuccessWorkflowResponse("HANDLE_RETRY");
    }

    public static class FailedResponses {
        public static final WorkflowResponse RETRY_FAILED =
                new FailedWorkflowResponse("RETRY_FAILED");
        public static final WorkflowResponse PROXY_RUN_FAILED =
                new FailedWorkflowResponse("PROXY_RUN_FAILED");
        public static final WorkflowResponse EXECUTION_FAILED =
                new FailedWorkflowResponse("EXECUTION_FAILED");
    }

    public static class EndResponses {
        public static final WorkflowResponse EXECUTION_SUCCESS =
                new EndWorkflowResponse("EXECUTION_SUCCESS");
    }
}
