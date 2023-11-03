package org.workflow.manager.tools;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

@Slf4j
public class ThreadTools {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            log.error("Unable to make it sleep due to " + e);
        }
    }

    public static String numberIdGenerator() {
        long id = SECURE_RANDOM.nextLong();

        return String.valueOf(((id < 0) ? -id : id) + 10000);
    }
}
