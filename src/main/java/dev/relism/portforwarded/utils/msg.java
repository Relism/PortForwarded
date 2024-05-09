package dev.relism.portforwarded.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class msg {

    public static final Logger LOGGER = LogManager.getLogger(msg.class);

    public static void log(String message) {
        LOGGER.info(message);
    }
}