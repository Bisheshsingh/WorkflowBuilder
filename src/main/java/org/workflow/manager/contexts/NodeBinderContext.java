package org.workflow.manager.contexts;

import lombok.Builder;
import lombok.Getter;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.WorkflowResponse;


@Builder
@Getter
public class NodeBinderContext<C extends ContextObject> extends ContextObject{
    private WorkflowResponse[] waitingResponseDependencies;
    private WorkflowResponse[] directResponseDependencies;
}
