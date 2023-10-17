package org.workflow.manager.exceptions;

public class BinderException extends Exception {
    public BinderException(final String message) {
        super(message);
    }

    public BinderException(final Exception e) {
        super(e);
    }
}
