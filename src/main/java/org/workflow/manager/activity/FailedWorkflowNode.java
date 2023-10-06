package org.workflow.manager.activity;

import com.google.inject.Module;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.FailedHandlerContextObject;
import org.workflow.manager.models.Service;

import java.util.List;

public class FailedWorkflowNode<C extends ContextObject>
        extends WorkflowNode<FailedHandlerContextObject<C>> {
    public <T extends Service<FailedHandlerContextObject<C>>> FailedWorkflowNode(Class<T> serviceType) {
        super(serviceType);
    }

    public <T extends Service<FailedHandlerContextObject<C>>> FailedWorkflowNode(Class<T> serviceType, Module... modules) {
        super(serviceType, modules);
    }

    public <T extends Service<FailedHandlerContextObject<C>>> FailedWorkflowNode(Class<T> serviceType, List<Module> modules) {
        super(serviceType, modules);
    }
}
