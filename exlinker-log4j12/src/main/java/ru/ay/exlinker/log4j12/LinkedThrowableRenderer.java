package ru.ay.exlinker.log4j12;

import org.apache.log4j.DefaultThrowableRenderer;
import org.apache.log4j.spi.OptionHandler;
import org.apache.log4j.spi.ThrowableRenderer;
import ru.ay.exlinker.Exlinker;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class LinkedThrowableRenderer implements ThrowableRenderer, OptionHandler {

    private ThrowableRenderer delegate = new DefaultThrowableRenderer();

    private String template;
    private Predicate<String> matcher;
    private Exlinker exlinker;

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setPattern(String pattern) {
        this.matcher = Pattern.compile(pattern).asPredicate();
    }

    @Override
    public void activateOptions() {
        exlinker = new Exlinker(matcher, template);
    }

    @Override
    public String[] doRender(Throwable throwable) {
        return delegate.doRender(exlinker.link(throwable));
    }
}
