package org.workflow.manager.tools;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadTools {
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("Unable to make it sleep due to " + e);
        }
    }
}
