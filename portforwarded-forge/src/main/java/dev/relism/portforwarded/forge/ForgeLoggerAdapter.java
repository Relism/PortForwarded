package dev.relism.portforwarded.forge;

import dev.relism.portforwarded.common.AbstractLogger;
import org.apache.logging.log4j.Logger;

public class ForgeLoggerAdapter extends AbstractLogger {

    private final Logger logger;

    public ForgeLoggerAdapter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void debug(String msg, Object... args) {
        logger.debug(msg, args);
    }

    @Override
    public void info(String msg, Object... args) {
        logger.info(msg, args);
    }

    @Override
    public void warn(String msg, Object... args) {
        logger.warn(msg, args);
    }

    @Override
    public void error(String msg, Object... args) {
        logger.error(msg, args);
    }
}