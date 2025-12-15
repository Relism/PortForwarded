package dev.relism.portforwarded.common;

public abstract class AbstractLogger {
    public abstract void debug(String msg, Object... args);
    public abstract void info(String msg, Object... args);
    public abstract void warn(String msg, Object... args);
    public abstract void error(String msg, Object... args);
}