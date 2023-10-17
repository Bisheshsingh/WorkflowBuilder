package org.workflow.manager.verifiers;

import TestModels.TestConfig;
import TestModels.TestContext;
import TestModels.TestResponses;
import com.google.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.workflow.manager.constants.WorkflowStatus;
import org.workflow.manager.contexts.WorkflowExecutionContext;
import org.workflow.manager.exceptions.BinderException;
import org.workflow.manager.models.WorkflowResponse;
import org.workflow.manager.responses.EndWorkflowResponse;
import org.workflow.manager.responses.SuccessWorkflowResponse;
import org.workflow.manager.tools.GuiceConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkflowStatusVerifierTest {
    @Inject
    private WorkflowStatusVerifier<TestContext> verifier;
    @Inject
    private TestConfig config;
    @Mock
    private WorkflowExecutionContext<TestContext> mockContext;

    @BeforeEach
    void setUp() throws BinderException {
        GuiceConfig.init().getInjector().injectMembers(this);
        config.configure(null);
    }

    @Test
    void testVerifyWithFailedResponseWithNullFailedResponseActions() {
        WorkflowResponse response = TestResponses.FailedResponses.B_FAILED;

        when(mockContext.getResponse()).thenReturn(response);
        when(mockContext.getConfig()).thenReturn(config);

        assertEquals(WorkflowStatus.FORCE_CLOSE, verifier.verify(mockContext));
    }

    @Test
    void testVerifyWithFailedResponseWithValidFailedResponseActions() {
        WorkflowResponse response = TestResponses.FailedResponses.A_FAILED;

        when(mockContext.getResponse()).thenReturn(response);
        when(mockContext.getConfig()).thenReturn(config);

        assertEquals(WorkflowStatus.FAILED, verifier.verify(mockContext));
    }

    @Test
    void testVerifyWithEndWorkflowResponse() {
        when(mockContext.getResponse()).thenReturn(new EndWorkflowResponse("TestState"));

        assertEquals(WorkflowStatus.SUCCESSFUL, verifier.verify(mockContext));
    }

    @Test
    void testVerifyWithSuccessWorkflowResponseWithNullResponseActions() {
        SuccessWorkflowResponse response = new SuccessWorkflowResponse("TestState");

        when(mockContext.getResponse()).thenReturn(response);
        when(mockContext.getConfig()).thenReturn(config);

        assertEquals(WorkflowStatus.FORCE_CLOSE, verifier.verify(mockContext));
    }

    @Test
    void testVerifyWithSuccessWorkflowResponseWithValidResponseActions() {
        WorkflowResponse response = TestResponses.SuccessfulResponses.A_PASSED;

        when(mockContext.getResponse()).thenReturn(response);
        when(mockContext.getConfig()).thenReturn(config);

        assertEquals(WorkflowStatus.IN_PROGRESS, verifier.verify(mockContext));
    }

    @Test
    void testVerifyWithUnknownResponse() {
        when(mockContext.getResponse()).thenReturn(new WorkflowResponse("TestState") {
            @Override
            public String getStateName() {
                return super.getStateName();
            }
        });

        assertEquals(WorkflowStatus.FORCE_CLOSE, verifier.verify(mockContext));
    }
}
