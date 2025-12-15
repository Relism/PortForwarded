package dev.relism.portforwarded.common.log;

import dev.relism.portforwarded.common.AbstractLogger;
import dev.relism.portforwarded.gwts.client.logging.ClientLogger;

public class ClientLoggerFactory {
    public static ClientLogger fromAbstractLogger(AbstractLogger logger) {
        return new ClientLogger() {
            @Override
            public void debug(String s, Object... objects) {
                logger.debug(s, objects);
            }

            @Override
            public void info(String s, Object... objects) {
                logger.info(s, objects);
            }

            @Override
            public void warn(String s, Object... objects) {
                logger.warn(s, objects);
            }

            @Override
            public void error(String s, Object... objects) {
                logger.error(s, objects);
            }
        };
    }
}