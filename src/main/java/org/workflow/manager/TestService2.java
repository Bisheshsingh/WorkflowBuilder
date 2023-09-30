package org.workflow.manager;

import org.workflow.manager.exceptions.ServiceException;
import org.workflow.manager.models.Service;

public class TestService2 implements Service<Context> {
    @Override
    public void run(final Context input) throws ServiceException {
        input.setTxt2(input.getTxt1() + "ing");
    }
}