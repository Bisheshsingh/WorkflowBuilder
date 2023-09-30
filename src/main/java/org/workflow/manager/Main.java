package org.workflow.manager;

import lombok.extern.slf4j.Slf4j;
import org.workflow.manager.activity.WorkflowExecutor;
import org.workflow.manager.activity.WorkflowNode;

@Slf4j
public class Main {
    public static void main(String[] args) {
        final Context context = new Context();
        final WorkflowExecutor<Context> workflowExecutor = new WorkflowExecutor<>();
        final WorkflowNode<Context> node1 = new WorkflowNode<>(new TestService());
        final WorkflowNode<Context> node2 = new WorkflowNode<>(new TestService2());
        final WorkflowNode<Context> node3 = new WorkflowNode<>(new TestService3());
        final WorkflowNode<Context> node4 = new WorkflowNode<>(new TestService4());

        node2.addDependency(node1);
        node3.addDependency(node1);
        node4.addDependency(node2).addDependency(node3);

        try {
            workflowExecutor.execute(context, node1, node4);
        } catch (Exception e) {
            log.error(e.toString());
        }

        log.info(context.toString());
    }
}