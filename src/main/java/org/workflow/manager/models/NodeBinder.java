package org.workflow.manager.models;

import org.workflow.manager.contexts.NodeBinderContext;
import org.workflow.manager.exceptions.BinderException;

public abstract class NodeBinder<C extends ContextObject, S extends Service<?>>
        implements Binder<NodeBinderContext<C>, Class<? extends S>> {
    protected WorkflowResponse[] waitingResponseDependencies;
    protected WorkflowResponse[] directResponseDependencies;
    protected final Workflow<C> workflow;

    public NodeBinder(final Workflow<C> workflow) {
        this.workflow = workflow;
    }

    @Override
    public final Binder<NodeBinderContext<C>, Class<? extends S>> bind(final NodeBinderContext<C> data) {
        this.directResponseDependencies = data.getDirectResponseDependencies();
        this.waitingResponseDependencies = data.getWaitingResponseDependencies();

        return this;
    }

    public abstract NodeBinder<C, S> bindDirectResponses(final WorkflowResponse... responses) throws BinderException;

    public abstract NodeBinder<C, S> bindWaitingResponses(final WorkflowResponse... responses) throws BinderException;
}
