package org.workflow.manager.handlers;

import TestModels.TestA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.workflow.manager.contexts.AnnotationHandlerContext;
import org.workflow.manager.exceptions.AnnotationHandleException;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProxyAnnotationHandlerTest {

    @InjectMocks
    private ProxyAnnotationHandler handler;

    @Spy
    private AnnotationHandlerContext context;

    @BeforeEach
    void setUp() {
        context.setServiceType(TestA.class);
    }

    @Test
    void testHandleWithAnnotatedMethod() throws AnnotationHandleException {
        context.setLevel("DryRun");

        handler.handle(context);

        assertEquals(context.getMethod().getName(), "dryRun");
    }

    @Test
    void testHandleWithInvalidAnnotatedMethod() throws AnnotationHandleException {
        context.setLevel("invalidLevel");

        handler.handle(context);

        assertEquals(context.getMethod().getName(), "run");
    }

    @Test
    void testHandleWithDefaultMethod() throws AnnotationHandleException {
        context.setLevel("default");

        handler.handle(context);

        assertEquals(context.getMethod().getName(), "run");
    }

    @Test
    void testHandleWithMultipleParamsMethod() throws AnnotationHandleException {
        context.setLevel("multipleParams");

        assertThrows(AnnotationHandleException.class, () -> handler.handle(context));
    }

    @Test
    void testHandleWithWrongParamsMethod() throws AnnotationHandleException {
        context.setLevel("wrongParams");

        assertThrows(AnnotationHandleException.class, () -> handler.handle(context));
    }

    @Test
    void testHandleWithWrongReturnTypeMethod() throws AnnotationHandleException {
        context.setLevel("wrongReturnType");

        assertThrows(AnnotationHandleException.class, () -> handler.handle(context));
    }
}
