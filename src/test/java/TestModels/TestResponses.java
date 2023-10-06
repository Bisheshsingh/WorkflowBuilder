package TestModels;

import org.workflow.manager.models.EndWorkflowResponse;
import org.workflow.manager.models.FailedWorkflowResponse;
import org.workflow.manager.models.SuccessWorkflowResponse;
import org.workflow.manager.models.WorkflowResponse;

public class TestResponses {
    public static final class EndResponses {
        public static final WorkflowResponse D_PASSED = new EndWorkflowResponse("D_PASSED");
    }

    public static final class SuccessfulResponses {
        public static final WorkflowResponse START_WORKFLOW = new SuccessWorkflowResponse("START_WORKFLOW");
        public static final WorkflowResponse A_PASSED = new SuccessWorkflowResponse("A_PASSED");
        public static final WorkflowResponse B_PASSED = new SuccessWorkflowResponse("B_PASSED");
        public static final WorkflowResponse C_PASSED = new SuccessWorkflowResponse("C_PASSED");
    }

    public static final class FailedResponses {
        public static final WorkflowResponse A_FAILED = new FailedWorkflowResponse("A_FAILED");
        public static final WorkflowResponse B_FAILED = new FailedWorkflowResponse("B_FAILED");
        public static final WorkflowResponse C_FAILED = new FailedWorkflowResponse("C_FAILED");
        public static final WorkflowResponse D_FAILED = new FailedWorkflowResponse("D_FAILED");
    }
}
