package dev.relism.portforwarded.sponge;

import dev.relism.portforwarded.common.AbstractLogger;
import org.apache.logging.log4j.Logger;

public final class SpongeLoggerAdapter extends AbstractLogger {

    private final Logger logger;

    public SpongeLoggerAdapter(Logger logger) {
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