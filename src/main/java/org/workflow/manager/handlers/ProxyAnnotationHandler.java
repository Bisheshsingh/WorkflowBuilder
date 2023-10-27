package org.workflow.manager.handlers;

import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.annotations.ProxyRun;
import org.workflow.manager.exceptions.AnnotationHandleException;
import org.workflow.manager.models.AnnotationHandler;
import org.workflow.manager.contexts.AnnotationHandlerContext;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.WorkflowResponse;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class ProxyAnnotationHandler implements AnnotationHandler {
    private static final String DEFAULT_METHOD_NAME = "run";

    private Boolean methodVerify(final Method method) {
        return (method.getReturnType().equals(WorkflowResponse.class))
                && (method.getParameterCount() == 1)
                && (ContextObject.class.isAssignableFrom(method.getParameterTypes()[0]));
    }

    @Override
    public Void handle(final AnnotationHandlerContext input) throws AnnotationHandleException {
        Method defaultMethod = null;

        for (final Method method : input.getServiceType().getMethods()) {
            if (method.getName().equals(DEFAULT_METHOD_NAME) && methodVerify(method)) {
                defaultMethod = method;
            }

            if (method.isAnnotationPresent(ProxyRun.class)
                    && Arrays.asList(method.getAnnotation(ProxyRun.class).levels())
                    .contains(input.getLevel())) {
                if (!methodVerify(method)) {
                    throw new AnnotationHandleException("Invalid method " + method.getName());
                }

                input.setMethod(method);
                return null;
            }
        }

        final String id = input.getServiceType().getSimpleName();

        if (input.getLevel() == null || !input.getLevel().equals("default")) {
            log.info("{} : Unable to find method with level {}", id, input.getLevel());
            log.info("{} : Going with default method {}", id, DEFAULT_METHOD_NAME);
        }

        input.setMethod(defaultMethod);

        return null;
    }
}
