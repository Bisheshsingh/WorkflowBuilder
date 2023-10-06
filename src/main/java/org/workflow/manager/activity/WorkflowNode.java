package org.workflow.manager.activity;

import com.google.inject.Module;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.annotations.Retry;
import org.workflow.manager.annotations.Run;
import org.workflow.manager.constants.RunLevels;
import org.workflow.manager.exceptions.WorkflowException;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.FailedWorkflowResponse;
import org.workflow.manager.models.Service;
import org.workflow.manager.models.WorkflowResponse;
import org.workflow.manager.tools.GuiceConfig;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class WorkflowNode<C extends ContextObject> {
    public static final String DEFAULT_METHOD = "run";
    private final Set<WorkflowResponse> responseDependency;
    @Getter
    private final Class<? extends Service<C>> serviceType;
    private final List<Module> modules;
    private Service<C> service;

    private WorkflowResponse execute(final C input, final Method method,
                                     final Integer currentRetryCount,
                                     final Integer totalRetryCount, final Integer coolDownTime) {
        service = (service == null) ? GuiceConfig.init(modules)
                .getInjector().getInstance(serviceType) : service;

        try {
            final WorkflowResponse response = (WorkflowResponse) method.invoke(service, input);

            if (response instanceof FailedWorkflowResponse) {
                throw new WorkflowException((FailedWorkflowResponse) response);
            }

            return response;
        } catch (final Exception e) {
            final String id = serviceType.getSimpleName();
            final String stackMsg = Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\n"));

            if (currentRetryCount > 0) {
                log.error("{} : Retry {}/{} - Failed", id, currentRetryCount, totalRetryCount);
            } else {
                log.error("{} : Failed to run the workflow", id);
            }

            log.error("{} : Reason - {}", id, e.toString());
            log.error("{} : Stack Trace - {}", id, stackMsg);

            if (currentRetryCount < totalRetryCount) {
                log.info("{} : Retry {}/{} - Started", id,
                        currentRetryCount + 1, totalRetryCount);

                try {
                    Thread.sleep(coolDownTime);
                } catch (InterruptedException ex) {
                    log.error("{} : Unable to cool down with {} time", id, coolDownTime);
                }

                return execute(input, method, currentRetryCount + 1,
                        totalRetryCount, coolDownTime);
            }

            return (e instanceof WorkflowException) ? ((WorkflowException) e).getResponse() :
                    new FailedWorkflowResponse("ERRORED_WORKFLOW", e);
        }
    }

    private Method getMethodNameFromRun(final String level) {
        Method defaultMethod  = null;
        for (final Method method : serviceType.getMethods()) {
            if(method.getName().equals(DEFAULT_METHOD)) {
                defaultMethod = method;
            }

            if (method.isAnnotationPresent(Run.class)
                    && method.getAnnotation(Run.class).level().equals(level)
                    && method.getReturnType().equals(WorkflowResponse.class)
                    && method.getParameterCount() == 1
                    && ContextObject.class.isAssignableFrom(method.getParameterTypes()[0])){

                return method;
            }
        }

        return defaultMethod;
    }

    public boolean areAllDependencyRan() {
        return responseDependency.isEmpty();
    }

    public void markADependency(final WorkflowResponse response) {
        responseDependency.remove(response);
    }

    public <T extends Service<C>> WorkflowNode(final Class<T> serviceType) {
        this.responseDependency = new HashSet<>();
        this.serviceType = serviceType;
        this.modules = new ArrayList<>();
    }

    public <T extends Service<C>> WorkflowNode(final Class<T> serviceType, final Module... modules) {
        this(serviceType, Arrays.asList(modules));
    }

    public <T extends Service<C>> WorkflowNode(final Class<T> serviceType,
                                               final List<Module> modules) {
        this.responseDependency = new HashSet<>();
        this.serviceType = serviceType;
        this.modules = modules;
    }

    public void dependsOn(final WorkflowResponse response) {
        responseDependency.add(response);
    }

    public WorkflowResponse execute(final C input, final String level) {
        int retryCount = 0;
        int coolDownTime = 0;

        if (serviceType.isAnnotationPresent(Retry.class)) {
            final Retry annotation = serviceType.getAnnotation(Retry.class);

            retryCount = annotation.count();
            coolDownTime = annotation.coolDown();
        }

        final Method method = getMethodNameFromRun(level);

        return execute(input, method, 0, retryCount, coolDownTime);
    }

    public WorkflowResponse execute(final C input) {
        return execute(input, RunLevels.DEFAULT);
    }
}
