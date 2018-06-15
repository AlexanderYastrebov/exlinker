package ru.ay.exlinker.log4j12;

import org.apache.log4j.EnhancedThrowableRenderer;
import org.apache.log4j.spi.ThrowableRenderer;
import ru.ay.exlinker.Exlinker;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class LinkedThrowableRenderer implements ThrowableRenderer {

    private EnhancedThrowableRenderer delegate = new EnhancedThrowableRenderer();

    private String template;
    private Predicate<String> matcher;
    private Exlinker exlinker;

    @Override
    public String[] doRender(Throwable throwable) {
        return delegate.doRender(exlinker.link(throwable));
    }

    public void setTemplate(String template) {
        this.template = template;

        initExlinker();
    }

    public void setPattern(String pattern) {
        this.matcher = Pattern.compile(pattern).asPredicate();

        initExlinker();
    }

    private void initExlinker() {
        if (template == null || matcher == null) {
            return;
        }
        exlinker = new Exlinker(matcher, template);
    }
}
