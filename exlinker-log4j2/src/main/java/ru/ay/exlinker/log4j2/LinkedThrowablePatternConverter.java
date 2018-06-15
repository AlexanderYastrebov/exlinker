package ru.ay.exlinker.log4j2;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;
import ru.ay.exlinker.Exlinker;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

@Plugin(name = "LinkedThrowablePatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"linkedThrowable"})
public final class LinkedThrowablePatternConverter extends ThrowablePatternConverter {

    private Exlinker exlinker;

    private LinkedThrowablePatternConverter(String[] options, String pattern, String template) {
        super("LinkedThrowable", "throwable", options);

        exlinker = new Exlinker(Pattern.compile(pattern).asPredicate(), template);
    }

    public static LinkedThrowablePatternConverter newInstance(String[] options) {
        requireNonNull(options);

        List<String> filtered = new ArrayList<>();
        String template = null;
        String pattern = null;
        for (String option : options) {
            if (is("template", option)) {
                template = valueOf("template", option);
            } else if (is("pattern", option)) {
                pattern = valueOf("pattern", option);
            } else {
                filtered.add(option);
            }
        }
        return new LinkedThrowablePatternConverter(filtered.toArray(new String[filtered.size()]), pattern, template);
    }

    private static boolean is(String name, String option) {
        return option.startsWith(name + "(") && option.endsWith(")");
    }

    private static String valueOf(String name, String option) {
        return option.substring(name.length() + 1, option.length() - 1);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        final Throwable throwable = event.getThrown();
        if (throwable != null && options.anyLines()) {
            exlinker.link(throwable);
        }
        super.format(event, toAppendTo);
    }
}
