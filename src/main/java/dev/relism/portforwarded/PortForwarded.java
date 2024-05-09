package dev.relism.portforwarded;

import com.github.alexdlaird.ngrok.NgrokClient;
import com.github.alexdlaird.ngrok.conf.JavaNgrokConfig;
import com.github.alexdlaird.ngrok.installer.NgrokVersion;
import com.github.alexdlaird.ngrok.protocol.Proto;
import com.github.alexdlaird.ngrok.protocol.Region;
import com.github.alexdlaird.ngrok.protocol.Tunnel;
import dev.relism.portforwarded.config.Config;
import dev.relism.portforwarded.config.YAMLConfig;
import dev.relism.portforwarded.ngrok.NgrokHelper;
import dev.relism.portforwarded.config.*;
import dev.relism.portforwarded.config.ConfigUtil.*;
import dev.relism.portforwarded.utils.DiscordWebhooksUtil;
import dev.relism.portforwarded.utils.msg;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class PortForwarded implements ModInitializer {

    private static NgrokClient ngrokClient;
    public static final String MOD_ID = "portforwarded";
    private static final NgrokVersion ngrokVersion = NgrokVersion.V3;
    private static final ConfigUtil configUtil = new ConfigUtil(MOD_ID);

    public void onInitialize() {
        YAMLConfig config = (YAMLConfig) configUtil.getConfig("config.yml", Filetype.YAML, PathType.RELATIVE);

        msg.log("Initializing PortForwarded 1.1 By Relism");
        msg.log("Github Repo: https://github.com/Relism/PortForwarded");
        msg.log("Modrinth Page: https://modrinth.com/mod/portforwarded");

        if (!checkConfig(config)) {
            ServerLifecycleEvents.SERVER_STARTED.register(server -> {
                if (server == null) {
                    throw new RuntimeException("Server is null!");
                }

                JavaNgrokConfig ngrokConfig = new JavaNgrokConfig.Builder()
                        .withNgrokVersion(ngrokVersion)
                        .withAuthToken(config.getString("ngrok_token"))
                        .withRegion(Region.valueOf(config.getString("ngrok_region")))
                        .withoutMonitoring()
                        .build();

                ngrokClient = new NgrokClient.Builder()
                        .withJavaNgrokConfig(ngrokConfig)
                        .build();

                String discordWebhook = config.getString("discord_webhook_url");

                Tunnel minecraftTunnel = NgrokHelper.createTunnel(Proto.TCP, server.getServerPort());
                String tunnelUrl = minecraftTunnel.getPublicUrl().replaceFirst("^tcp://", "");
                msg.log("==[PortForwarded]=========================================");
                msg.log("Forwarded Port: " + server.getServerPort());
                msg.log("Tunnel Running on URL: " + tunnelUrl);

                if (!discordWebhook.isEmpty()) {
                    DiscordWebhooksUtil.sendEmbed(discordWebhook, "New Server URL",
                            "The Minecraft server has started on the following URL: " + tunnelUrl, 0xB4D3B2);
                }
            });
        }
    }

    private static boolean checkConfig(Config config) {
        String ngrokToken = config.getString("ngrok_token");
        String ngrokRegionString = config.getString("ngrok_region");
        String discordWebhook = config.getString("discord_webhook_url");

        if (ngrokToken == null || ngrokToken.equals("YOUR_NGROK_TOKEN")) {
            msg.log("Error: Ngrok token is missing or invalid. Please set 'ngrok_token' in config.yml");
            return true;
        }

        if (ngrokRegionString == null || !isValidRegion(ngrokRegionString)) {
            msg.log("Error: Ngrok region is missing or invalid. Please set 'ngrok_region' in config.yml with a valid region code");
            return true;
        }

        if (discordWebhook != null && !isValidDiscordWebhook(discordWebhook)) {
            msg.log("Warning: Discord webhook URL might be invalid. Please double-check the format of 'discord_webhook_url' in config.yml");
        }

        return false;
    }

    private static boolean isValidRegion(String ngrokRegionString) {
        try {
            Region.valueOf(ngrokRegionString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean isValidDiscordWebhook(String discordWebhook) {
        return discordWebhook != null && discordWebhook.startsWith("https://discord.com/api/webhooks/");
    }

    public static NgrokClient getNgrokClient(){ return ngrokClient; }

    public static ConfigUtil getConfigUtil() { return configUtil; }
}