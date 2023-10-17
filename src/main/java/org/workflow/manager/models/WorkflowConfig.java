package org.workflow.manager.models;

import org.workflow.manager.exceptions.BinderException;
import org.workflow.manager.node_binders.FailedNodeBinder;
import org.workflow.manager.node_binders.SuccessNodeBinder;

public abstract class WorkflowConfig<C extends ContextObject>
        extends Workflow<C>
        implements Config<Binder<?, ?>> {

    @Override
    public void configure(final Binder<?, ?> data) throws BinderException {
        configureSuccessNodes(new SuccessNodeBinder<>(this));
        configureFailedNodes(new FailedNodeBinder<>(this));
    }

    public abstract void configureSuccessNodes(final SuccessNodeBinder<C> binder) throws BinderException;

    public abstract void configureFailedNodes(final FailedNodeBinder<C> binder) throws BinderException;
}
