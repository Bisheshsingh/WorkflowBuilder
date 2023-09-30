package org.workflow.manager.exceptions;

public class ServiceException extends Exception {
    public ServiceException(final String message) {
        super(message);
    }

    public ServiceException(final Exception exception) {
        super(exception);
    }
}
