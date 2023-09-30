package org.workflow.manager.models;

import org.workflow.manager.exceptions.ServiceException;

public interface Service <T> {
    void run(T input) throws ServiceException;
}
