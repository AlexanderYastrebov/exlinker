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

import static org.junit.Assert.assertEquals;

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

            String stackTrace = stackTraceToString(ex, 12);

            assertEquals("java.lang.RuntimeException: Omega server not available\n" +
                    "\tat ru.ay.example.dao.BookDao.iota(BookDao.java:14)\n" +
                    "\tat ru.ay.example.dao.BookDao.theta(BookDao.java:10)\n" +
                    "\tat ru.ay.example.dao.BookDao.eta(BookDao.java:6)\n" +
                    "\tat ru.ay.example.service.BookService.zeta(https://github.com/AlexanderYastrebov/exlinker/blob/master/src/test/java/ru/ay/example/service/BookService.java#L18)\n" +
                    "\tat ru.ay.example.service.BookService.epsilon(https://github.com/AlexanderYastrebov/exlinker/blob/master/src/test/java/ru/ay/example/service/BookService.java#L14)\n" +
                    "\tat ru.ay.example.service.BookService.access$100(https://github.com/AlexanderYastrebov/exlinker/blob/master/src/test/java/ru/ay/example/service/BookService.java#L5)\n" +
                    "\tat ru.ay.example.service.BookService$Task.run(https://github.com/AlexanderYastrebov/exlinker/blob/master/src/test/java/ru/ay/example/service/BookService.java#L24)\n" +
                    "\tat ru.ay.example.service.BookService.delta(https://github.com/AlexanderYastrebov/exlinker/blob/master/src/test/java/ru/ay/example/service/BookService.java#L10)\n" +
                    "\tat ru.ay.example.controller.BookController.gamma(BookController.java:18)\n" +
                    "\tat ru.ay.example.controller.BookController.beta(BookController.java:14)\n" +
                    "\tat ru.ay.example.controller.BookController.alpha(BookController.java:10)", stackTrace);
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
