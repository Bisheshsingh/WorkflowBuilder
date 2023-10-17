package org.workflow.manager.verifiers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.workflow.manager.contexts.WorkflowNodeExecutionContext;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.workflow_nodes.WorkflowNode;
import org.workflow.manager.models.WorkflowResponse;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowNodeExecutionVerifierTest {

    @Mock
    WorkflowNodeExecutionContext<ContextObject> mockContext;

    @Mock
    WorkflowNode<ContextObject> mockWorkflowNode;

    @Mock
    WorkflowResponse mockResponse;

    @InjectMocks
    WorkflowNodeExecutionVerifier<ContextObject> verifier;

    @BeforeEach
    void setUp() {
        when(mockContext.getNode()).thenReturn(mockWorkflowNode);
        when(mockContext.getResponse()).thenReturn(mockResponse);
    }

    @Test
    void testVerifyWithDirectDependencyResponse() {
        when(mockWorkflowNode.getDirectDependencies())
                .thenReturn(new HashSet<>(Collections.singletonList(mockResponse)));

        assertTrue(verifier.verify(mockContext));
    }

    @Test
    void testVerifyWithEqualWaitingAndMarkedDependencies() {
        when(mockWorkflowNode.getDirectDependencies())
                .thenReturn(new HashSet<>(Collections.emptyList()));
        when(mockWorkflowNode.getWaitingDependencies())
                .thenReturn(new HashSet<>(Collections.singletonList(mockResponse)));
        when(mockWorkflowNode.getMarkedDependencies())
                .thenReturn(new HashSet<>(Collections.singletonList(mockResponse)));

        assertTrue(verifier.verify(mockContext));
    }

    @Test
    void testVerifyWithNoMatch() {
        when(mockWorkflowNode.getDirectDependencies())
                .thenReturn(new HashSet<>(Collections.emptyList()));
        when(mockWorkflowNode.getWaitingDependencies())
                .thenReturn(new HashSet<>(Collections.emptyList()));
        when(mockWorkflowNode.getMarkedDependencies())
                .thenReturn(new HashSet<>(Collections.singletonList(mockResponse)));

        assertFalse(verifier.verify(mockContext));
    }
}
