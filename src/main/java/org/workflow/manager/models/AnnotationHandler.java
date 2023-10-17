package org.workflow.manager.models;

import org.workflow.manager.contexts.AnnotationHandlerContext;
import org.workflow.manager.exceptions.AnnotationHandleException;

public interface AnnotationHandler extends Handler<AnnotationHandlerContext, Void> {
    @Override
    Void handle(final AnnotationHandlerContext input) throws AnnotationHandleException;
}
