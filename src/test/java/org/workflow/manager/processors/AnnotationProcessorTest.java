package org.workflow.manager.processors;

import TestModels.TestA;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.workflow.manager.contexts.AnnotationHandlerContext;
import org.workflow.manager.exceptions.AnnotationHandleException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class AnnotationProcessorTest {
    @InjectMocks
    private AnnotationProcessor processor;

    @Spy
    private AnnotationHandlerContext context;

    @Test
    void process_withValidContext_processesAnnotationsSuccessfully() {
        context.setServiceType(TestA.class);
        context.setLevel("");

        assertDoesNotThrow(() -> processor.process(context));
    }

    @Test
    void process_withHandlerException_throwsAnnotationHandleException() {
        context.setServiceType(TestA.class);
        context.setLevel("multipleParams");

        assertThrows(AnnotationHandleException.class, () -> processor.process(context));
    }
}
