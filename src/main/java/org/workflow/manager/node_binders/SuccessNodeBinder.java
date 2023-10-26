package org.workflow.manager.node_binders;

import lombok.NonNull;
import org.workflow.manager.exceptions.BinderException;
import org.workflow.manager.responses.FailedWorkflowResponse;
import org.workflow.manager.responses.SuccessWorkflowResponse;
import org.workflow.manager.workflow_nodes.WorkflowNode;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.NodeBinder;
import org.workflow.manager.models.Service;
import org.workflow.manager.models.Workflow;
import org.workflow.manager.models.WorkflowResponse;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SuccessNodeBinder<C extends ContextObject> extends NodeBinder<C, Service<C>> {
    private void verifyResponses(final WorkflowResponse... responses) throws BinderException {
        for (final WorkflowResponse workflowResponse : responses) {
            if(!(workflowResponse instanceof SuccessWorkflowResponse)) {
                throw new BinderException(String.format(
                        "Unable to bind %s because it is not SuccessfulWorkflowResponse type",
                        workflowResponse));
            }
        }
    }

    private void addResponseActions(final WorkflowNode<C> workflowNode,
                                    final WorkflowResponse... responses) {
        if (responses == null) {
            return;
        }

        final Map<WorkflowResponse, Set<WorkflowNode<C>>> actions = workflow
                .getResponseActions();

        for (final WorkflowResponse response : responses) {
            if (!actions.containsKey(response)) {
                actions.put(response, new HashSet<>());
            }

            actions.get(response).add(workflowNode);
        }
    }

    public SuccessNodeBinder(final Workflow<C> workflow) {
        super(workflow);
    }

    @Override
    public SuccessNodeBinder<C> bindDirectResponses(final WorkflowResponse... responses) throws BinderException {
        verifyResponses(responses);
        this.directResponseDependencies = responses;
        return this;
    }

    @Override
    public SuccessNodeBinder<C> bindWaitingResponses(final WorkflowResponse... responses) throws BinderException {
        verifyResponses(responses);
        this.waitingResponseDependencies = responses;
        return this;
    }

    @Override
    public void to(@NonNull final Class<? extends Service<C>> data) {
        final WorkflowNode<C> workflowNode = new WorkflowNode<>(data);

        workflowNode.addDirectResponseDependency(directResponseDependencies);
        workflowNode.addWaitingResponseDependency(waitingResponseDependencies);

        addResponseActions(workflowNode, directResponseDependencies);
        addResponseActions(workflowNode, waitingResponseDependencies);
    }
}
