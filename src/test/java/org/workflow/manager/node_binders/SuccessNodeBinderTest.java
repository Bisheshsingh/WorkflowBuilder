package org.workflow.manager.node_binders;

import TestModels.TestConfig;
import TestModels.TestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.workflow.manager.exceptions.BinderException;
import org.workflow.manager.responses.FailedWorkflowResponse;
import org.workflow.manager.responses.SuccessWorkflowResponse;

import static org.junit.jupiter.api.Assertions.*;

public class SuccessNodeBinderTest {

    private SuccessNodeBinder<TestContext> binder;

    @BeforeEach
    void setUp() {
        binder = new SuccessNodeBinder<>(new TestConfig());
    }

    @Test
    void bindDirectResponses_withSuccessfulResponse_bindsSuccessfully() {
        SuccessWorkflowResponse response = new SuccessWorkflowResponse("TestState");

        assertDoesNotThrow(() -> binder.bindDirectResponses(response));
    }

    @Test
    void bindDirectResponses_withFailedResponse_throwsBinderException() {
        FailedWorkflowResponse failedResponse = new FailedWorkflowResponse("error");

        assertThrows(BinderException.class, () -> binder.bindDirectResponses(failedResponse));
    }
}
