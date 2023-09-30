package org.workflow.manager.exceptions;

public class WorkflowException extends Exception {
    public WorkflowException(final String message) {
        super(message);
    }

    public WorkflowException(final Exception exception) {
        super(exception);
    }
}
