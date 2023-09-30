package org.workflow.manager.activity;

import org.workflow.manager.constants.Enums.WorkflowStatus;
import org.workflow.manager.exceptions.ListenerException;
import org.workflow.manager.models.Listener;

public class WorkflowListener implements Listener<WorkflowNode<?>, WorkflowStatus> {
    public WorkflowStatus listen(final WorkflowNode<?> workflowNode) throws ListenerException {
        while (!workflowNode.getStatus().equals(WorkflowStatus.SUCCESSFUL)
                && !workflowNode.getStatus().equals(WorkflowStatus.FAILED)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                final String msg = "Unable to run the listener due to " + e.getMessage();
                throw new ListenerException(msg);
            }
        }

        return workflowNode.getStatus();
    }
}
