package org.workflow.manager.handlers;

import TestModels.TestConfig;
import TestModels.TestContext;
import TestModels.TestOperation;
import TestModels.TestResponses;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.workflow.manager.contexts.WorkflowExecutionContext;
import org.workflow.manager.exceptions.AnnotationHandleException;
import org.workflow.manager.exceptions.BinderException;
import org.workflow.manager.exceptions.WorkflowException;
import org.workflow.manager.executors.WorkflowNodeExecutor;

import org.workflow.manager.models.WorkflowResponse;
import org.workflow.manager.tools.WorkflowOrchestrator;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuccessfulWorkflowResponseHandlerTest {

    @Mock
    private WorkflowNodeExecutor<TestContext> workflowNodeExecutor;

    @Mock
    private WorkflowExecutionContext<TestContext> mockExecutionContext;

    @Mock
    private WorkflowResponse mockResponse;

    @InjectMocks
    private TestOperation workflowOperation;

    @InjectMocks
    private SuccessfulWorkflowResponseHandler<TestContext> handler;

    @InjectMocks
    private TestConfig config;

    @BeforeAll
    public static void setAllUp() {
        try (MockedStatic<WorkflowOrchestrator> mocked = Mockito.mockStatic(WorkflowOrchestrator.class)) {
            System.out.println("Mocking Static Orchestrator");
        }
    }

    @BeforeEach
    void setUp() throws BinderException, CloneNotSupportedException {
        config.configure(null);
        mockResponse = TestResponses.SuccessfulResponses.START_WORKFLOW;

        when(mockExecutionContext.getResponse()).thenReturn(mockResponse);
        when(mockExecutionContext.getConfig()).thenReturn(config);
        when(mockExecutionContext.getWorkflowOperation()).thenReturn(workflowOperation);
        when(mockExecutionContext.clone()).thenReturn(mockExecutionContext);
    }

    @Test
    void testHandleWithSuccessfulNodeExecution() throws WorkflowException, AnnotationHandleException {
        when(workflowNodeExecutor.execute(any())).thenReturn(mockResponse);

        handler.handle(mockExecutionContext);

        verify(workflowNodeExecutor).execute(any());
    }

    @Test
    void testHandleWithWorkflowException() throws WorkflowException, AnnotationHandleException {
        when(workflowNodeExecutor.execute(any())).thenThrow(WorkflowException.class);

        handler.handle(mockExecutionContext);

        verify(workflowNodeExecutor).execute(any());
    }

    @Test
    void testHandleWithAnnotationHandleException() throws WorkflowException, AnnotationHandleException {
        when(workflowNodeExecutor.execute(any())).thenThrow(AnnotationHandleException.class);

        handler.handle(mockExecutionContext);

        verify(workflowNodeExecutor).execute(any());
    }

    @Test
    void testHandleWithCloneNotSupportedException() throws CloneNotSupportedException {
        when(mockExecutionContext.clone()).thenThrow(CloneNotSupportedException.class);

        handler.handle(mockExecutionContext);

        verify(mockExecutionContext).clone();
    }
}
