package org.workflow.manager;

import org.workflow.manager.exceptions.ServiceException;
import org.workflow.manager.models.Service;

public class TestService implements Service<Context> {
    @Override
    public void run(final Context input) throws ServiceException {
        input.setTxt1("Test");
    }
}
