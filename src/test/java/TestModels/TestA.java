package TestModels;

import org.workflow.manager.annotations.ProxyRun;
import org.workflow.manager.exceptions.ServiceException;
import org.workflow.manager.models.Service;
import org.workflow.manager.models.WorkflowResponse;

public class TestA extends Service<TestContext> {
    @Override
    protected WorkflowResponse performAction(TestContext input) throws ServiceException {
        input.setTxt1("Test");

        return TestResponses.FailedResponses.A_FAILED;
    }

    @ProxyRun(levels = {"DryRun"})
    public WorkflowResponse dryRun(TestContext context) {
        context.setTxt1("DryRun");
        return TestResponses.SuccessfulResponses.A_PASSED;
    }

    @ProxyRun(levels = {"wrongParams"})
    public WorkflowResponse wrongParams(String context) {
        return TestResponses.SuccessfulResponses.A_PASSED;
    }

    @ProxyRun(levels = {"wrongReturnType"})
    public String wrongReturnType(TestContext context) {
        return TestResponses.SuccessfulResponses.A_PASSED.getStateName();
    }

    @ProxyRun(levels = {"multipleParams"})
    public String multipleParams(TestContext context, String x) {
        return TestResponses.SuccessfulResponses.A_PASSED.getStateName();
    }
}