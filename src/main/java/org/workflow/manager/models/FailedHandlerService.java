package org.workflow.manager.models;

import org.workflow.manager.contexts.FailedHandlerContextObject;

public abstract class FailedHandlerService<C extends ContextObject>
        extends Service<FailedHandlerContextObject<C>> {

}
