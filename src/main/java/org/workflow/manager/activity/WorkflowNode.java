package org.workflow.manager.activity;

import com.google.inject.Module;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.constants.WorkflowResponses;
import org.workflow.manager.exceptions.ServiceException;
import org.workflow.manager.exceptions.WorkflowException;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.Service;
import org.workflow.manager.models.WorkflowResponse;
import org.workflow.manager.tools.GuiceConfig;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class WorkflowNode<C extends ContextObject> {
    private final Set<WorkflowResponse> responseDependency;
    @Getter
    private final Class<? extends Service<C>> serviceType;
    private final List<Module> modules;

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

    public <T extends Service<C>> WorkflowNode(final Class<T> serviceType, final Module module) {
        this.responseDependency = new HashSet<>();
        this.serviceType = serviceType;
        this.modules = Collections.singletonList(module);
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

    public WorkflowResponse execute(final C input) {
        final Service<C> service = GuiceConfig.init(modules)
                .getInjector().getInstance(serviceType);

        try {
            return service.run(input);
        } catch (final ServiceException e) {
            final String id = serviceType.getSimpleName();
            final String stackMsg = Arrays.stream(e.getStackTrace())
                    .map(StackTraceElement::toString)
                    .collect(Collectors.joining("\n"));

            log.error("{} : Failed to run the workflow", id);
            log.error("{} : Reason {}", id, e.getMessage());
            log.error("{} : Stack Trace {}", id, stackMsg);

            return WorkflowResponses.FAILED_WORKFLOW;
        }
    }
}
