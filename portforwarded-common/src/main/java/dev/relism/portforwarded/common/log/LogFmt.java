package dev.relism.portforwarded.common.log;

import java.util.ArrayList;
import java.util.List;

//helper for non-slf4j AbstractLogger implementations
public final class LogFmt {

    private LogFmt() {}

    public static Formatted format(String template, Object... args) {
        if (template == null) template = "null";

        Throwable throwable = null;
        Object[] effectiveArgs = args;

        // slf4j convention: if the last arg is a Throwable, treat it as such
        if (args != null && args.length > 0 && args[args.length - 1] instanceof Throwable t) {
            throwable = t;
            effectiveArgs = new Object[args.length - 1];
            System.arraycopy(args, 0, effectiveArgs, 0, args.length - 1);
        }

        String msg = formatBraces(template, effectiveArgs);
        return new Formatted(msg, throwable);
    }

    private static String formatBraces(String template, Object[] args) {
        if (args == null || args.length == 0) return template;

        StringBuilder out = new StringBuilder(template.length() + 32);
        int argIndex = 0;

        for (int i = 0; i < template.length(); i++) {
            char c = template.charAt(i);

            if (c == '\\' && i + 2 < template.length()
                    && template.charAt(i + 1) == '{' && template.charAt(i + 2) == '}') {
                out.append("{}");
                i += 2;
                continue;
            }

            if (c == '{' && i + 1 < template.length() && template.charAt(i + 1) == '}') {
                if (argIndex < args.length) {
                    out.append(args[argIndex++]);
                } else {
                    out.append("{}");
                }
                i++;
                continue;
            }

            out.append(c);
        }

        if (argIndex < args.length) {
            List<String> extras = new ArrayList<>();
            for (int j = argIndex; j < args.length; j++) extras.add(String.valueOf(args[j]));
            out.append(" ").append(extras);
        }

        return out.toString();
    }

    public record Formatted(String message, Throwable throwable) {}
}