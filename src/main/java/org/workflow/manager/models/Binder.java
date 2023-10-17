package org.workflow.manager.models;

public interface Binder<T, U> {
    Binder<T, U> bind(T data);

    void to(U data);
}
