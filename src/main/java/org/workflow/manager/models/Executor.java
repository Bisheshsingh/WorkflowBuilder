package org.workflow.manager.models;

import org.workflow.manager.exceptions.AnnotationHandleException;
import org.workflow.manager.exceptions.WorkflowException;

public interface Executor<T, U> {
    T execute(U data) throws WorkflowException, AnnotationHandleException;
}
