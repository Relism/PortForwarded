package dev.relism.portforwarded.fabric;

import dev.relism.portforwarded.common.log.CommonClass;
import net.fabricmc.api.ModInitializer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PortForwardedFabricMod implements ModInitializer {

    @Override
    public void onInitialize() {
        Properties properties = loadServerProperties();

        String ip = properties.getProperty("server-ip", "").trim();
        if (ip.isEmpty()) {
            ip = "0.0.0.0";
        }

        int port = 25565;
        try {
            port = Integer.parseInt(
                    properties.getProperty("server-port", "25565").trim()
            );
        } catch (NumberFormatException ignored) {
        }

        CommonClass commonClass =
                new CommonClass("fabric", new FabricLoggerAdapter(), ip, port);

        commonClass.initialize();
    }

    private Properties loadServerProperties() {
        Properties props = new Properties();
        Path path = resolveServerPropertiesPath();

        if (Files.isReadable(path)) {
            try (InputStream in = Files.newInputStream(path)) {
                props.load(in);
            } catch (IOException ignored) {
            }
        }

        return props;
    }

    private Path resolveServerPropertiesPath() {
        String dir = System.getProperty("user.dir");
        if (dir == null || dir.isBlank()) {
            return Path.of("server.properties");
        }
        return Path.of(dir).resolve("server.properties");
    }
}