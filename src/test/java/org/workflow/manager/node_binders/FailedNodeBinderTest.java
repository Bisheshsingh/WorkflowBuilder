package org.workflow.manager.node_binders;

import TestModels.TestAHandler;
import TestModels.TestConfig;
import TestModels.TestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.workflow.manager.exceptions.BinderException;
import org.workflow.manager.models.Workflow;
import org.workflow.manager.responses.FailedWorkflowResponse;
import org.workflow.manager.responses.SuccessWorkflowResponse;

import static org.junit.jupiter.api.Assertions.*;

public class FailedNodeBinderTest {

    private FailedNodeBinder<TestContext> binder;

    @BeforeEach
    void setUp() {
        Workflow<TestContext> workflow = new TestConfig();

        binder = new FailedNodeBinder<>(workflow);
    }

    @Test
    void bindDirectResponses_withFailedResponse_bindsSuccessfully() {
        FailedWorkflowResponse response = new FailedWorkflowResponse("error");
        FailedWorkflowResponse response1 = new FailedWorkflowResponse("error1");

        assertDoesNotThrow(() -> binder.bindDirectResponses(response, response1).to(TestAHandler.class));
    }

    @Test
    void bindWaitingResponses_withFailedResponse_bindsSuccessfully() {
        FailedWorkflowResponse response = new FailedWorkflowResponse("error");

        assertDoesNotThrow(() -> binder.bindWaitingResponses(response, response).to(TestAHandler.class));
    }

    @Test
    void bindDirectResponses_withSuccessfulResponse_throwsBinderException() {
        SuccessWorkflowResponse successResponse = new SuccessWorkflowResponse("TestState");

        assertThrows(BinderException.class, () -> binder.bindDirectResponses(successResponse).to(TestAHandler.class));
    }
}
