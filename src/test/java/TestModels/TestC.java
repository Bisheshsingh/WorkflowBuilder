package TestModels;

import org.workflow.manager.annotations.ProxyRun;
import org.workflow.manager.exceptions.ServiceException;
import org.workflow.manager.models.Service;
import org.workflow.manager.models.WorkflowResponse;

public class TestC extends Service<TestContext> {
    @Override
    protected WorkflowResponse performAction(TestContext input) throws ServiceException {
        input.setTxt3(String.format("%sed", input.getTxt1()));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new ServiceException(e);
        }

        return TestResponses.SuccessfulResponses.C_PASSED;
    }

    @ProxyRun(levels = {"DryRun"})
    public WorkflowResponse dryRun(TestContext input) {
        final int size = input.getTxt1().length();
        input.setTxt3(String.format("%san", input.getTxt1().substring(0, size - 2)));

        return TestResponses.SuccessfulResponses.C_PASSED;
    }
}