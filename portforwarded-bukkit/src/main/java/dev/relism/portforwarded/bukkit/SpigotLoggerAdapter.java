package dev.relism.portforwarded.bukkit;

import dev.relism.portforwarded.common.AbstractLogger;
import dev.relism.portforwarded.common.log.LogFmt;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;
import java.util.logging.Logger;


public final class SpigotLoggerAdapter extends AbstractLogger {

    private final Logger logger;

    public SpigotLoggerAdapter(Plugin plugin) {
        this.logger = plugin.getLogger();
    }

    @Override
    public void debug(String s, Object... objects) {
        log(Level.FINE, s, objects);
    }

    @Override
    public void info(String s, Object... objects) {
        log(Level.INFO, s, objects);
    }

    @Override
    public void warn(String s, Object... objects) {
        log(Level.WARNING, s, objects);
    }

    @Override
    public void error(String s, Object... objects) {
        log(Level.SEVERE, s, objects);
    }

    // uses LogFmt as bukkit's logger isnt slf4j-compatible
    private void log(Level level, String template, Object... args) {
        LogFmt.Formatted f = LogFmt.format(template, args);
        if (f.throwable() != null) {
            logger.log(level, f.message(), f.throwable());
        } else {
            logger.log(level, f.message());
        }
    }
}
