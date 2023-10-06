package TestModels;

import org.workflow.manager.activity.WorkflowConfig;
import static TestModels.ServiceNames.*;
import static TestModels.TestResponses.SuccessfulResponses.*;
import static TestModels.TestResponses.FailedResponses.*;

public class TestConfig extends WorkflowConfig<TestContext> {
    @Override
    protected void initialize() {
        addNode(TEST_A, TestA.class);
        addNode(TEST_B, TestB.class);
        addNode(TEST_C, TestC.class);
        addNode(TEST_D, TestD.class);
        addFailedHandlerNode(TEST_A_HANDLER, TestAHandler.class);
    }

    @Override
    protected void configure() {
        addActions(START_WORKFLOW, TEST_A);
        addActions(A_PASSED, TEST_B, TEST_C);
        addActions(B_PASSED, TEST_D);
        addActions(C_PASSED, TEST_D);
        addFailedActions(A_FAILED, TEST_A_HANDLER);
    }
}
