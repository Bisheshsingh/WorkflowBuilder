package org.workflow.manager.models;

import org.workflow.manager.exceptions.ListenerException;

import java.util.function.Predicate;

public interface Listener<I, O> {
    O listen(I object) throws ListenerException;

    default O listen(I object, Predicate<I> predicate) throws ListenerException {
        while (!predicate.test(object)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                final String msg = "Unable to run the listener due to " + e.getMessage();
                throw new ListenerException(msg);
            }
        }

        return null;
    }
}
