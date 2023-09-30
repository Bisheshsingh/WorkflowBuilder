package org.workflow.manager.exceptions;

public class ListenerException extends Exception{
    public ListenerException(final String message) {
        super(message);
    }

    public ListenerException(final Exception exception) {
        super(exception);
    }
}
