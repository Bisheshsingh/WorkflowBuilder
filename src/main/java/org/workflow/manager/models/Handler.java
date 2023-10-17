package org.workflow.manager.models;

public interface Handler <I extends ContextObject, O> {
    O handle(I input) throws Exception;
}
