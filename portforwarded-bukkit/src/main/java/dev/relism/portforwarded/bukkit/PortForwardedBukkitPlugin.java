package dev.relism.portforwarded.bukkit;

import dev.relism.portforwarded.common.log.CommonClass;
import org.bukkit.plugin.java.JavaPlugin;

public class PortForwardedBukkitPlugin extends JavaPlugin {
    private CommonClass commonClass;

    @Override
    public void onEnable() {
        String ip = getServer().getIp();
        int port = getServer().getPort();
        commonClass = new CommonClass("bukkit", new SpigotLoggerAdapter(this), ip, port);
        commonClass.initialize();
    }

    @Override
    public void onDisable() {
        getLogger().info("PortForwarded Bukkit shutting down.");
    }
}