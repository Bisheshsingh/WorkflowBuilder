package org.workflow.manager.models;

import lombok.Data;

@Data
public class FailedHandlerContextObject<C extends ContextObject> implements ContextObject{
    private Exception exception;
    private C contextObject;
}
