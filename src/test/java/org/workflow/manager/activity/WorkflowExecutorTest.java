package org.workflow.manager.activity;

import TestModels.TestConfig;
import TestModels.TestContext;
import org.junit.jupiter.api.Test;

import static TestModels.TestResponses.SuccessfulResponses.*;

class WorkflowExecutorTest {
    @Test
    void execute() throws InterruptedException {
        WorkflowExecutor<TestContext> executor = new WorkflowExecutor<>();
        TestContext context = new TestContext();
        TestConfig config = new TestConfig();

        executor.execute(config, context,
                true, "DryRun", START_WORKFLOW);

        System.out.println(executor.getWorkflowStatus());
        System.out.println(executor.getWorkflowResponse().getStateName());
        System.out.println(context);
    }
}