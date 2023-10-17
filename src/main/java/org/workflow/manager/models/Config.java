package org.workflow.manager.models;

import org.workflow.manager.exceptions.BinderException;

public interface Config<T extends Binder<?, ?>> {
    void configure(T data) throws BinderException;
}
