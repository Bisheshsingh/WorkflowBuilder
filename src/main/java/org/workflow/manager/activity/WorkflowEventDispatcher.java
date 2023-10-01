package org.workflow.manager.activity;

import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.models.ContextObject;
import org.workflow.manager.models.WorkflowResponse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class WorkflowEventDispatcher<C extends ContextObject> {
    private final Map<WorkflowResponse, Set<WorkflowNode<C>>> listenerMap;

    public WorkflowEventDispatcher() {
        this.listenerMap = new HashMap<>();
    }

    public WorkflowEventDispatcher<C> addListener(final WorkflowResponse response, final WorkflowNode<C> node) {
        if (listenerMap.containsKey(response)) {
            listenerMap.get(response).add(node);
        } else {
            final Set<WorkflowNode<C>> nodes = new HashSet<>();
            nodes.add(node);

            listenerMap.put(response, nodes);
        }

        node.dependsOn(response);
        return this;
    }

    public void emit(final WorkflowResponse currentResponse, final C context) {
        if (listenerMap.get(currentResponse) == null) {
            log.info("There is no listener for {}", currentResponse);
            return;
        }

        for (WorkflowNode<C> node : listenerMap.get(currentResponse)) {
            new Thread(() -> {
                node.markADependency(currentResponse);

                if (node.areAllDependencyRan()) {
                    final String serviceName = node.getServiceType().getSimpleName();
                    log.info("Executing {} service", serviceName);

                    final WorkflowResponse response = node.execute(context);

                    log.info("{} service ran with response : {}", serviceName, response);
                    emit(response, context);
                }
            }).start();
        }
    }
}
