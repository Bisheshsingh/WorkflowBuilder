package org.workflow.manager.models;

import org.workflow.manager.exceptions.AnnotationHandleException;

public interface Processor<T> {
    void process(T context) throws AnnotationHandleException;
}
