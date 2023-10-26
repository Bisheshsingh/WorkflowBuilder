package org.workflow.manager.node_binders;

import lombok.NonNull;
import org.workflow.manager.exceptions.BinderException;
import org.workflow.manager.responses.FailedWorkflowResponse;
import org.workflow.manager.workflow_nodes.FailedWorkflowNode;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.FailedHandlerService;
import org.workflow.manager.models.NodeBinder;
import org.workflow.manager.models.Workflow;
import org.workflow.manager.models.WorkflowResponse;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FailedNodeBinder<C extends ContextObject>
        extends NodeBinder<C, FailedHandlerService<C>> {

    private void verifyResponse(final WorkflowResponse... responses) throws BinderException {
        for (final WorkflowResponse workflowResponse : responses) {
            if(!(workflowResponse instanceof FailedWorkflowResponse)) {
                throw new BinderException(String.format(
                        "Unable to bind %s because it is not FailedWorkflowResponse type",
                        workflowResponse));
            }
        }
    }

    private void addResponseActions(final FailedWorkflowNode<C> workflowNode,
                                    final WorkflowResponse... responses) {
        if(responses == null) {
            return;
        }

        final Map<WorkflowResponse, Set<FailedWorkflowNode<C>>> actions = workflow
                .getFailedResponseActions();

        for (final WorkflowResponse response : responses) {
            if (!actions.containsKey(response)) {
                actions.put(response, new HashSet<>());
            }

            actions.get(response).add(workflowNode);
        }
    }

    public FailedNodeBinder(final Workflow<C> workflow) {
        super(workflow);
    }

    @Override
    public void to(@NonNull final Class<? extends FailedHandlerService<C>> data) {
        final FailedWorkflowNode<C> workflowNode = new FailedWorkflowNode<>(data);

        workflowNode.addDirectResponseDependency(directResponseDependencies);
        workflowNode.addWaitingResponseDependency(waitingResponseDependencies);

        addResponseActions(workflowNode, directResponseDependencies);
        addResponseActions(workflowNode, waitingResponseDependencies);
    }

    @Override
    public FailedNodeBinder<C> bindDirectResponses(final WorkflowResponse... responses) throws BinderException {
        verifyResponse(responses);
        this.directResponseDependencies = responses;
        return this;
    }

    @Override
    public FailedNodeBinder<C> bindWaitingResponses(final WorkflowResponse... responses) throws BinderException {
        verifyResponse(responses);
        this.waitingResponseDependencies = responses;
        return this;
    }
}
