package org.workflow.manager.models;

public interface Verifier<I, O> {
    O verify(I data);
}
