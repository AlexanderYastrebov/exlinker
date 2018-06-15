package ru.ay.exlinker;

import org.junit.Test;
import ru.ay.example.controller.BookController;

import java.util.function.Predicate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class ExlinkerTest {

    private Predicate<String> matcher = name -> name.startsWith("ru.ay.example.service");
    private String template = "https://github.com/AlexanderYastrebov/exlinker/blob/{rev}/exlinker-core/src/test/java/{packagePath}/{fileName}#L{lineNumber}";
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
                        assertTrue(ste.getFileName().startsWith("'https://github.com/AlexanderYastrebov/exlinker/" +
                                "blob/master/exlinker-core/src/test/java/ru/ay/example/service/BookService.java#L"));
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
}
