package TestModels;

import org.workflow.manager.exceptions.BinderException;
import org.workflow.manager.models.Service;
import org.workflow.manager.models.WorkflowConfig;
import org.workflow.manager.node_binders.FailedNodeBinder;
import org.workflow.manager.node_binders.SuccessNodeBinder;
import static TestModels.TestResponses.SuccessfulResponses.*;
import static TestModels.TestResponses.FailedResponses.*;

public class TestConfig extends WorkflowConfig<TestContext> {
    @Override
    public void configureSuccessNodes(final SuccessNodeBinder<TestContext> binder) throws BinderException {
        binder.bindWaitingResponses(START_WORKFLOW).to(TestA.class);
        binder.bindWaitingResponses(A_PASSED).to(TestB.class);
        binder.bindWaitingResponses(A_PASSED).to(TestC.class);
        binder.bindWaitingResponses(B_PASSED, C_PASSED).to(TestD.class);
    }

    @Override
    public void configureFailedNodes(final FailedNodeBinder<TestContext> binder) throws BinderException {
        binder.bindDirectResponses(A_FAILED).to(TestAHandler.class);
    }
}
