package ru.ay.exlinker;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ay.example.controller.BookController;

import java.util.function.Predicate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class ExlinkerTest {

    private Logger logger = LoggerFactory.getLogger(ExlinkerTest.class);

    private Predicate<String> matcher = name -> name.startsWith("ru.ay.example.service");
    private String template = "https://github.com/AlexanderYastrebov/exlinker/blob/{rev}/src/test/java/{packagePath}/{fileName}#L{lineNumber}";
    private Exlinker unit = new Exlinker(matcher, template, "rev", "master");

    @Test
    public void shouldLink() {
        try {
            new BookController().alpha();
        } catch (Exception ex) {
            ex.printStackTrace();

            unit.link(ex);

            ex.printStackTrace();

            Throwable t = ex;
            while (t != null) {
                for (StackTraceElement ste : t.getStackTrace()) {
                    if (matcher.test(ste.getClassName())) {
                        assertTrue(ste.getFileName().startsWith("'https://github.com/AlexanderYastrebov/exlinker/blob/master" +
                                "/src/test/java/ru/ay/example/service/BookService.java#L"));
                    }
                }
                t = t.getCause();
            }
        }
    }

    @Test
    public void shouldNotLinkTwice() {
        try {
            new BookController().alpha();
        } catch (Exception ex) {
            unit.link(ex);

            StackTraceElement[] first = ex.getStackTrace();

            unit.link(ex);

            StackTraceElement[] second = ex.getStackTrace();

            assertArrayEquals(first, second);
        }
    }

    @Test
    public void shouldLogLinked() {
        try {
            new BookController().alpha();
        } catch (Exception ex) {
            logger.debug("once", ex);
            logger.debug("twice", ex);
        }
    }
}
