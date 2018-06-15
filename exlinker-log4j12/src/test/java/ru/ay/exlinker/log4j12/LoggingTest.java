package ru.ay.exlinker.log4j12;

import org.apache.log4j.Logger;
import org.junit.Test;

public class LoggingTest {

    private Logger logger = Logger.getLogger(LoggingTest.class);

    @Test
    public void shouldLog() {
        try {
            doSomething();
        } catch (Exception ex) {
            logger.error("Error occured", ex);
        }
    }

    private void doSomething() {
        try {
            doSomethingElse();
        } catch (Exception ex) {
            throw new RuntimeException("Wrapping", ex);
        }
    }

    private void doSomethingElse() {
        throw new RuntimeException("Oops");
    }
}
