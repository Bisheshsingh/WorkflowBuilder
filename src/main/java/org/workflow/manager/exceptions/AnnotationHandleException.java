package org.workflow.manager.exceptions;

public class AnnotationHandleException extends Exception {
    public AnnotationHandleException(final String message) {
        super(message);
    }

    public AnnotationHandleException(final Exception e) {
        super(e);
    }
}
