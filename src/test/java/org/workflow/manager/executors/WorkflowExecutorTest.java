package org.workflow.manager.executors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import TestModels.TestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.workflow.manager.constants.WorkflowStatus;
import org.workflow.manager.contexts.WorkflowExecutionContext;
import org.workflow.manager.exceptions.BinderException;
import org.workflow.manager.handlers.FailedWorkflowResponseHandler;
import org.workflow.manager.handlers.SuccessfulWorkflowResponseHandler;
import org.workflow.manager.models.WorkflowConfig;
import org.workflow.manager.models.WorkflowResponse;
import org.workflow.manager.models.WorkflowOperation;
import org.workflow.manager.verifiers.WorkflowStatusVerifier;

import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
public class WorkflowExecutorTest {
    @InjectMocks
    private WorkflowExecutor<TestContext> workflowExecutor;

    @Mock
    private WorkflowConfig<TestContext> config;

    @Mock
    private WorkflowOperation workflowOperation;

    @Mock
    private WorkflowExecutionContext<TestContext> executionContext;

    @Mock
    private WorkflowStatusVerifier<TestContext> workflowStatusVerifier;

    @Mock
    private SuccessfulWorkflowResponseHandler<TestContext> successfulWorkflowResponseHandler;

    @Mock
    private FailedWorkflowResponseHandler<TestContext> failedWorkflowResponseHandler;

    @Mock
    private WorkflowResponse startResponse;

    @Mock
    private TestContext context;

    @Test
    public void testExecute_withExecutionContext() throws BinderException {
        when(executionContext.getWorkflowOperation()).thenReturn(workflowOperation);
        when(executionContext.getConfig()).thenReturn(config);
        when(config.getResponseActions()).thenReturn(new HashMap<>());
        when(workflowStatusVerifier.verify(any())).thenReturn(WorkflowStatus.FORCE_CLOSE);

        workflowExecutor.execute(executionContext);

        verify(config).configure(null);
    }

    @Test
    public void testExecute_withConfigContextAndResponse() throws BinderException {
        when(workflowStatusVerifier.verify(any())).thenReturn(WorkflowStatus.FORCE_CLOSE);

        workflowExecutor.execute(config, context, startResponse);

        verify(config).configure(null);
    }

    @Test
    public void testExecute_withConfigContextResponseAndLevel() throws BinderException {
        when(workflowStatusVerifier.verify(any())).thenReturn(WorkflowStatus.FORCE_CLOSE);

        workflowExecutor.execute(config, context, startResponse, "LEVEL_1");

        verify(config).configure(null);
    }

    @Test
    public void testExecute_withConfigContextResponseAndOperation() throws BinderException {
        when(workflowStatusVerifier.verify(any())).thenReturn(WorkflowStatus.FORCE_CLOSE);

        workflowExecutor.execute(config, context, startResponse, workflowOperation);

        verify(config).configure(null);
    }

    @Test
    public void testExecute_withAllParameters() throws BinderException {
        when(workflowStatusVerifier.verify(any())).thenReturn(WorkflowStatus.FORCE_CLOSE);

        workflowExecutor.execute(config, context, startResponse, "LEVEL_1", workflowOperation);

        verify(config).configure(null);
        assertEquals(startResponse, workflowExecutor.getWorkflowResponse());
    }

    @Test
    public void testUpdateWorkflowResponse() {
        workflowExecutor.updateWorkflowResponse(startResponse);

        assertEquals(startResponse, workflowExecutor.getWorkflowResponse());
    }

    @Test
    public void testNotifyExecution() {
        workflowExecutor.notifyExecution();

        assertEquals(false, workflowExecutor.getLock());
    }

    @Test
    public void testBinderExceptionThrown() throws BinderException {
        when(executionContext.getConfig()).thenReturn(config);
        doThrow(BinderException.class).when(config).configure(null);

        assertThrows(BinderException.class, () -> workflowExecutor.execute(executionContext));
    }

    @Test
    public void testWaitAndRelease() {
        workflowExecutor.lockExecution();

        Thread th = new Thread(() -> workflowExecutor.waitForExecution());
        th.start();

        assertEquals(true, workflowExecutor.getLock());

        workflowExecutor.notifyExecution();

        assertEquals(false, workflowExecutor.getLock());
        th.interrupt();
    }

    @Test
    public void testResponseHandlers() {
        assertEquals(failedWorkflowResponseHandler, workflowExecutor.getFailedWorkflowResponseHandler());
        assertEquals(successfulWorkflowResponseHandler, workflowExecutor.getSuccessfulWorkflowResponseHandler());
    }
}
