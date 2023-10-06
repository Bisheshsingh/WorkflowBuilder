package TestModels;

import org.workflow.manager.exceptions.ServiceException;
import org.workflow.manager.models.FailedHandlerContextObject;
import org.workflow.manager.models.FailedHandlerService;
import org.workflow.manager.models.WorkflowResponse;

public class TestAHandler extends FailedHandlerService<TestContext> {
    @Override
    protected WorkflowResponse performAction(FailedHandlerContextObject<TestContext> input) throws ServiceException {
        return TestResponses.SuccessfulResponses.A_PASSED;
    }
}
