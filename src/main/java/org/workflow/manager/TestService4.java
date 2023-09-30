package org.workflow.manager;

import org.workflow.manager.exceptions.ServiceException;
import org.workflow.manager.models.Service;

public class TestService4 implements Service<Context> {
    @Override
    public void run(final Context input) throws ServiceException {
        input.setTxt4(input.getTxt2() + " " + input.getTxt3());
    }
}