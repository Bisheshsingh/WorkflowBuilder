package TestModels;

import org.workflow.manager.models.WorkflowOperation;

public class TestOperation implements WorkflowOperation {
    @Override
    public void operate(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void shutDown() {

    }
}
