package org.workflow.manager.executors;

import TestModels.TestConfig;
import TestModels.TestContext;
import TestModels.TestResponses;
import org.junit.jupiter.api.RepeatedTest;
import org.workflow.manager.exceptions.BinderException;
import org.workflow.manager.models.WorkflowResponse;
import org.workflow.manager.tools.WorkflowRunner;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkflowExecutorIntegrationTest {
    @RepeatedTest(5)
    public void executeTest() {
        final TestContext context = new TestContext();
        final TestContext context1 = new TestContext();

        Runnable runnable1 = () -> {
            try {
                final WorkflowResponse response = WorkflowRunner
                        .createInstance()
                        .withConfig(new TestConfig())
                        .run(TestResponses.SuccessfulResponses.START_WORKFLOW, context);

                assertEquals(response, TestResponses.EndResponses.D_PASSED);
                assertEquals(context.getTxt4(), "Test -> Testing -> Tested");
            } catch (final BinderException e) {
                throw new RuntimeException(e);
            }
        };

        Runnable runnable2 = () -> {
            try {
                final WorkflowResponse response = WorkflowRunner
                        .createInstance()
                        .withConfig(new TestConfig())
                        .withLevel("DryRun")
                        .run(TestResponses.SuccessfulResponses.START_WORKFLOW, context1);

                assertEquals(response, TestResponses.EndResponses.D_PASSED);
                assertEquals(context1.getTxt4(), "DryRun -> DryRunning -> DryRan");
            } catch (final BinderException e) {
                throw new RuntimeException(e);
            }
        };

        Arrays.asList(runnable1, runnable1, runnable2, runnable2).parallelStream().forEach(Runnable::run);
    }
}
