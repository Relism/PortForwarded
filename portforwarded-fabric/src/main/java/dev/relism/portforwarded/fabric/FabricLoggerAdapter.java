package dev.relism.portforwarded.fabric;

import dev.relism.portforwarded.common.AbstractLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FabricLoggerAdapter extends AbstractLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger("portforwarded");

    @Override
    public void debug(String s, Object... objects) {
        LOGGER.debug(s, objects);
    }

    @Override
    public void info(String s, Object... objects) {
        LOGGER.info(s, objects);
    }

    @Override
    public void warn(String s, Object... objects) {
        LOGGER.warn(s, objects);
    }

    @Override
    public void error(String s, Object... objects) {
        LOGGER.error(s, objects);
    }
}