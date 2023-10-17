package org.workflow.manager.models;

public class ContextObject implements Cloneable {
    @Override
    public ContextObject clone() throws CloneNotSupportedException {
        return (ContextObject) super.clone();
    }
}
