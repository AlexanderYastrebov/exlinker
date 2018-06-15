package ru.ay.exlinker;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public class Exlinker {

    private final Predicate<String> classNameMatcher;
    private final String linkTemplate;

    public Exlinker(Predicate<String> classNameMatcher, String linkTemplate, String... linkTemplateParams) {
        this.classNameMatcher = requireNonNull(classNameMatcher);
        this.linkTemplate = resolve(requireNonNull(linkTemplate), linkTemplateParams);
    }

    public Throwable link(Throwable throwable) {
        StackTraceElement[] stackTrace = throwable.getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            stackTrace[i] = new SteLinker(stackTrace[i]).getLinked();
        }
        throwable.setStackTrace(stackTrace);
        return throwable;
    }

    private String resolve(String template, String[] params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("params key-values should have even length");
        }
        for (int i = 0; i < params.length; i += 2) {
            template = template.replace("{" + params[i] + "}", params[i + 1]);
        }
        return template;
    }

    private class SteLinker {

        private final StackTraceElement ste;

        SteLinker(StackTraceElement ste) {
            this.ste = ste;
        }

        StackTraceElement getLinked() {
            return classNameMatcher.test(ste.getClassName()) ? makeLinked() : ste;
        }

        private StackTraceElement makeLinked() {
            return new StackTraceElement(
                    ste.getClassName(),
                    ste.getMethodName(),
                    makeLink(),
                    -1);// linkTemplate should take care of line number
        }

        private String makeLink() {
            String className = ste.getClassName();
            String packageName = className.substring(0, className.lastIndexOf('.'));
            String packagePath = packageName.replace('.', '/');

            String result = linkTemplate
                    .replace("{packagePath}", packagePath)
                    .replace("{lineNumber}", Integer.toString(ste.getLineNumber()));

            String fileName = ste.getFileName();
            if (fileName != null) {
                result = result.replace("{fileName}", fileName);
            }
            return result;
        }
    }
}
