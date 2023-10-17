package org.workflow.manager.contexts;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.Service;

import java.lang.reflect.Method;

@EqualsAndHashCode(callSuper = true)
@Data
public class AnnotationHandlerContext extends ContextObject {
    private Class<? extends Service<? extends ContextObject>> serviceType;
    private String level;
    private Integer retryCount;
    private Integer retryCoolDownTime;
    private Method method;
}
