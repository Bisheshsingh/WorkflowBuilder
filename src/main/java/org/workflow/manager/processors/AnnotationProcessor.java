package org.workflow.manager.processors;

import com.google.common.collect.ImmutableList;
import org.workflow.manager.handlers.ProxyAnnotationHandler;
import org.workflow.manager.handlers.RetryAnnotationHandler;
import org.workflow.manager.exceptions.AnnotationHandleException;
import org.workflow.manager.models.AnnotationHandler;
import org.workflow.manager.contexts.AnnotationHandlerContext;
import org.workflow.manager.models.Processor;
import org.workflow.manager.tools.GuiceConfig;

public class AnnotationProcessor implements Processor<AnnotationHandlerContext> {
    private static final ImmutableList<Class<? extends AnnotationHandler>> handlers = ImmutableList.of(
            ProxyAnnotationHandler.class,
            RetryAnnotationHandler.class
    );


    @Override
    public void process(final AnnotationHandlerContext context) throws AnnotationHandleException {
        try {
            handlers.parallelStream().forEach(handlerClass -> {
                final AnnotationHandler handler = GuiceConfig.getInstance(handlerClass);

                try {
                    handler.handle(context);
                } catch (AnnotationHandleException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new AnnotationHandleException(e);
        }
    }
}
