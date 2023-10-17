package org.workflow.manager.handlers;

import TestModels.TestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.workflow.manager.annotations.Retry;
import org.workflow.manager.contexts.AnnotationHandlerContext;
import org.workflow.manager.exceptions.AnnotationHandleException;
import org.workflow.manager.exceptions.ServiceException;
import org.workflow.manager.models.Service;
import org.workflow.manager.models.WorkflowResponse;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RetryAnnotationHandlerTest {

    @InjectMocks
    private RetryAnnotationHandler handler;

    @Spy
    private AnnotationHandlerContext mockContext;

    private static class NoAnnotationServiceType extends Service<TestContext> {
        @Override
        protected WorkflowResponse performAction(TestContext input) throws ServiceException {
            return null;
        }
    }

    @Retry(count = 600, coolDown = 10)
    private static class InvalidCountServiceType extends Service<TestContext> {
        @Override
        protected WorkflowResponse performAction(TestContext input) throws ServiceException {
            return null;
        }
    }

    @Retry(count = 10, coolDown = -5)
    private static class InvalidCoolDownServiceType extends Service<TestContext> {
        @Override
        protected WorkflowResponse performAction(TestContext input) throws ServiceException {
            return null;
        }
    }
    @Retry(count = 10, coolDown = 5)
    private static class ValidServiceType extends Service<TestContext> {
        @Override
        protected WorkflowResponse performAction(TestContext input) throws ServiceException {
            return null;
        }
    }
    @BeforeEach
    void setUp() {

    }

    @Test
    void testHandleWithNoAnnotation() throws AnnotationHandleException {
        mockContext.setServiceType(NoAnnotationServiceType.class);

        handler.handle(mockContext);

        assertEquals(0, mockContext.getRetryCount());
        assertEquals(0, mockContext.getRetryCoolDownTime());
    }

    @Test
    void testHandleWithInvalidCount() {
        mockContext.setServiceType(InvalidCountServiceType.class);

        final AnnotationHandleException e =
                assertThrows(AnnotationHandleException.class, () -> handler.handle(mockContext));

        assertTrue(e.getMessage().startsWith("Invalid Retry Count"));
    }

    @Test
    void testHandleWithInvalidCoolDown() {
        mockContext.setServiceType(InvalidCoolDownServiceType.class);

        final AnnotationHandleException e =
                assertThrows(AnnotationHandleException.class, () -> handler.handle(mockContext));

        assertTrue(e.getMessage().startsWith("Invalid Retry Cool Down Time"));
    }

    @Test
    void testHandleWithValidRetryAnnotation() throws AnnotationHandleException {
        mockContext.setServiceType(ValidServiceType.class);

        handler.handle(mockContext);

        assertEquals(10, mockContext.getRetryCount());
        assertEquals(5, mockContext.getRetryCoolDownTime());
    }
}
