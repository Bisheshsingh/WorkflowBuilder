package org.workflow.manager.contexts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.workflow.manager.models.ContextObject;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FailedHandlerContextObject<C extends ContextObject> extends ContextObject{
    private Exception exception;
    private C contextObject;
}
