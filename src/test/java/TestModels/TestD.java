package TestModels;

import org.workflow.manager.exceptions.ServiceException;
import org.workflow.manager.models.Service;
import org.workflow.manager.models.WorkflowResponse;

public class TestD extends Service<TestContext> {
    @Override
    protected WorkflowResponse performAction(TestContext input) throws ServiceException {
        input.setTxt4(String
                .format("%s -> %s -> %s", input.getTxt1(), input.getTxt2(), input.getTxt3()));

        return TestResponses.EndResponses.D_PASSED;
    }
}