package dev.relism.portforwarded.forge;

import dev.relism.portforwarded.common.AbstractLogger;
import dev.relism.portforwarded.common.log.CommonClass;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PortForwardedForge.MOD_ID)
public class PortForwardedForge {

    public static final String MOD_ID = "portforwarded";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private CommonClass common;

    public PortForwardedForge() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        MinecraftServer s = event.getServer();
        String ip = s.getLocalIp().isEmpty() ? "0.0.0.0" : s.getLocalIp();
        int port = s.getPort();

        AbstractLogger logger = new ForgeLoggerAdapter(LOGGER);
        common = new CommonClass("forge", logger, ip, port);
        common.initialize();
    }
}