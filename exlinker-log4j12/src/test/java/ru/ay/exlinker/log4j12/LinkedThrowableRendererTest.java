package ru.ay.exlinker.log4j12;

import org.apache.log4j.Logger;
import org.junit.Test;

public class LinkedThrowableRendererTest {

    private Logger logger = Logger.getLogger(LinkedThrowableRendererTest.class);

    @Test
    public void shouldLog() {
        try {
            throw new RuntimeException("Hello");
        } catch (Exception ex) {
            logger.error("oops", ex);
        }
    }
}
