package org.workflow.manager;

import org.workflow.manager.exceptions.ServiceException;
import org.workflow.manager.models.Service;

public class TestService3 implements Service<Context> {
    @Override
    public void run(final Context input) throws ServiceException {
        input.setTxt3(input.getTxt1() + "ed");
    }
}