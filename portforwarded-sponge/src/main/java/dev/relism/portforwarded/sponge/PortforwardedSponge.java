package dev.relism.portforwarded.sponge;

import com.google.inject.Inject;
import dev.relism.portforwarded.common.log.CommonClass;
import dev.relism.portforwarded.common.AbstractLogger;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.net.InetSocketAddress;

@Plugin("portforwarded")
public class PortforwardedSponge {

    private final Logger logger;
    private CommonClass common;

    @Inject
    public PortforwardedSponge(Logger logger) {
        this.logger = logger;
    }

    @Listener
    public void onServerStarted(StartedEngineEvent<Server> e) {
        Server server = e.engine();
        InetSocketAddress address = server.boundAddress().get();
        String ip = address.getAddress().getHostAddress();
        int port = address.getPort();

        AbstractLogger spongeLogger = new SpongeLoggerAdapter(logger);

        common = new CommonClass("sponge", spongeLogger, ip, port);
        common.initialize();
    }
}
