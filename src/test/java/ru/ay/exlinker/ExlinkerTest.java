package ru.ay.exlinker;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ay.example.controller.BookController;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;

public class ExlinkerTest {

    private Logger logger = LoggerFactory.getLogger(ExlinkerTest.class);

    @Test
    public void shouldLink() {
        String template = "https://github.com/AlexanderYastrebov/exlinker/blob/{rev}/src/test/java/{packagePath}/{fileName}#L{lineNumber}";
        Predicate<String> matcher = name -> name.startsWith("ru.ay.example.service");
        Exlinker exlinker = new Exlinker(matcher, template, "rev", "master");

        try {
            new BookController().alpha();
        } catch (Exception ex) {
            ex.printStackTrace();

            exlinker.link(ex);

            ex.printStackTrace();

            Throwable t = ex;
            while (t != null) {
                for (StackTraceElement ste : t.getStackTrace()) {
                    if (matcher.test(ste.getClassName())) {
                        assertTrue(ste.getFileName().startsWith("https://github.com/AlexanderYastrebov/exlinker/blob/master" +
                                "/src/test/java/ru/ay/example/service/BookService.java#L"));
                    }
                }
                t = t.getCause();
            }
        }
    }

    @Test
    public void shouldLogLinked() {
        try {
            new BookController().alpha();
        } catch (Exception ex) {
            logger.debug("debugging", ex);
        }
    }

    private String stackTraceToString(Exception ex, int limit) {
        StringWriter w = new StringWriter();

        ex.printStackTrace(new PrintWriter(w));

        return head(w.toString(), limit);
    }

    private String head(String s, int limit) {
        return Stream.of(s.split("\n"))
                .limit(limit)
                .collect(Collectors.joining("\n"));
    }
}
