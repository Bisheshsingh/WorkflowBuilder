package org.workflow.manager.handlers;

import org.workflow.manager.annotations.Retry;
import org.workflow.manager.exceptions.AnnotationHandleException;
import org.workflow.manager.models.AnnotationHandler;
import org.workflow.manager.contexts.AnnotationHandlerContext;

public class RetryAnnotationHandler implements AnnotationHandler {

    @Override
    public Void handle(final AnnotationHandlerContext input) throws AnnotationHandleException {
        final Retry anno = input.getServiceType().getAnnotation(Retry.class);

        if(anno == null) {
            input.setRetryCount(0);
            input.setRetryCoolDownTime(0);

            return null;
        }

        if (anno.count() > 500 || anno.count() < 0) {
            throw new AnnotationHandleException("Invalid Retry Count " + anno.count());
        } else if(anno.coolDown() < 0) {
            throw new AnnotationHandleException("Invalid Retry Cool Down Time " + anno.coolDown());
        }

        input.setRetryCount(anno.count());
        input.setRetryCoolDownTime(anno.coolDown());

        return null;
    }
}
