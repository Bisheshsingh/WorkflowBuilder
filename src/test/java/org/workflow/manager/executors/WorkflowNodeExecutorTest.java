package org.workflow.manager.executors;

import TestModels.TestA;
import TestModels.TestB;
import TestModels.TestContext;
import TestModels.TestResponses;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.workflow.manager.constants.WorkflowResponses;
import org.workflow.manager.contexts.WorkflowNodeExecutionContext;
import org.workflow.manager.exceptions.AnnotationHandleException;
import org.workflow.manager.exceptions.WorkflowException;
import org.workflow.manager.models.WorkflowResponse;
import org.workflow.manager.tools.GuiceConfig;
import org.workflow.manager.verifiers.WorkflowNodeExecutionVerifier;
import org.workflow.manager.workflow_nodes.WorkflowNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkflowNodeExecutorTest {
    @Mock
    private WorkflowNodeExecutionVerifier<TestContext> workflowNodeExecutionVerifier;

    @Mock
    private WorkflowNodeExecutionContext<TestContext> mockContext;

    private WorkflowNodeExecutor<TestContext> workflowNodeExecutor;

    private WorkflowNode<TestContext> workflowNode;

    @BeforeEach
    public void setUp() {
        workflowNodeExecutor = GuiceConfig.getInjector(binder ->
                        binder.bind(new TypeLiteral<WorkflowNodeExecutionVerifier<TestContext>>() {
                                })
                                .toInstance(workflowNodeExecutionVerifier))
                .getInstance(Key.get(new TypeLiteral<>() {
                }));

        workflowNode = new WorkflowNode<>(TestB.class);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(workflowNodeExecutionVerifier, mockContext);
    }

    @Test
    public void testExecuteSuccessfully() throws WorkflowException, AnnotationHandleException {
        when(workflowNodeExecutionVerifier.verify(any())).thenReturn(true);
        when(mockContext.getNode()).thenReturn(workflowNode);
        when(mockContext.getLevel()).thenReturn("");
        when(mockContext.getContext()).thenReturn(new TestContext());

        WorkflowResponse response = workflowNodeExecutor.execute(mockContext);

        assertEquals(TestResponses.SuccessfulResponses.B_PASSED, response);
    }

    @Test
    public void testExecuteWithFailedResponse() throws WorkflowException, AnnotationHandleException {
        workflowNode = new WorkflowNode<>(TestA.class);

        when(workflowNodeExecutionVerifier.verify(any())).thenReturn(true);
        when(mockContext.getNode()).thenReturn(workflowNode);
        when(mockContext.getLevel()).thenReturn("");
        when(mockContext.getContext()).thenReturn(new TestContext());

        WorkflowResponse response = workflowNodeExecutor.execute(mockContext);

        assertEquals(TestResponses.FailedResponses.A_FAILED, response);
    }

    @Test
    public void testExecuteWhenVerifierReturnsFalse() throws WorkflowException, AnnotationHandleException {
        when(workflowNodeExecutionVerifier.verify(any())).thenReturn(false);
        when(mockContext.getNode()).thenReturn(workflowNode);

        WorkflowResponse response = workflowNodeExecutor.execute(mockContext);

        assertEquals(WorkflowResponses.WAITING_RESPONSE, response);
    }

    @Test
    public void testExecuteWithService() throws WorkflowException, AnnotationHandleException {
        workflowNode.setService(new TestB());

        when(workflowNodeExecutionVerifier.verify(any())).thenReturn(true);
        when(mockContext.getNode()).thenReturn(workflowNode);
        when(mockContext.getLevel()).thenReturn("");
        when(mockContext.getContext()).thenReturn(new TestContext());

        WorkflowResponse response = workflowNodeExecutor.execute(mockContext);

        assertEquals(TestResponses.SuccessfulResponses.B_PASSED, response);
    }

    @Test
    public void testExecuteWhenAnnotationProcessorThrowsException() {
        when(workflowNodeExecutionVerifier.verify(any())).thenReturn(true);
        when(mockContext.getNode()).thenReturn(workflowNode);

        assertThrows(AnnotationHandleException.class, () -> {
            workflowNodeExecutor.execute(mockContext);
        });
    }

    @Test
    public void testExecuteWhenRunServiceThrowsWorkflowException() {
        when(workflowNodeExecutionVerifier.verify(any())).thenReturn(true);
        when(mockContext.getNode()).thenReturn(workflowNode);
        when(mockContext.getLevel()).thenReturn("");

        assertThrows(WorkflowException.class, () -> {
            workflowNodeExecutor.execute(mockContext);
        });
    }
}
