package org.workflow.manager.executors;

import TestModels.TestConfig;
import TestModels.TestContext;
import TestModels.TestExecutor;
import TestModels.TestResponses;
import org.junit.jupiter.api.Test;
import org.workflow.manager.processors.AnnotationProcessor;
import org.workflow.manager.tools.GuiceConfig;
import org.workflow.manager.tools.WorkflowRunner;
import org.workflow.manager.workflow_operations.AutoThreadOperation;
import org.workflow.manager.workflow_operations.BatchOperations;

class WorkflowExecutorTest {
    @Test
    void execute() throws Exception {
        TestConfig config = new TestConfig();
        TestContext context = new TestContext();

        System.out.println(config.getResponseActions());


        System.out.println(WorkflowRunner
                .createInstance()
                .withConfig(config)
                .run(TestResponses.SuccessfulResponses.START_WORKFLOW, context));

        System.out.println(context);
    }
}