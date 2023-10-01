package org.workflow.manager.models;

import org.workflow.manager.exceptions.ServiceException;

public interface Service <T extends ContextObject> {
    WorkflowResponse run(T input) throws ServiceException;
}
