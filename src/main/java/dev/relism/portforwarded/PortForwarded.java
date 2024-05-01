package dev.relism.portforwarded;

import com.github.alexdlaird.ngrok.NgrokClient;
import com.github.alexdlaird.ngrok.conf.JavaNgrokConfig;
import com.github.alexdlaird.ngrok.installer.NgrokVersion;
import com.github.alexdlaird.ngrok.protocol.Proto;
import com.github.alexdlaird.ngrok.protocol.Region;
import com.github.alexdlaird.ngrok.protocol.Tunnel;
import dev.relism.portforwarded.ngrok.NgrokHelper;
import dev.relism.portforwarded.utils.ConfigUtil;
import dev.relism.portforwarded.utils.DiscordWebhooksUtil;
import dev.relism.portforwarded.utils.msg;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class PortForwarded implements ModInitializer {

    private static NgrokClient ngrokClient;
    private static final ConfigUtil config = new ConfigUtil("config.yml");

    public static final String MOD_ID = "portforwarded";

    static final String ngrokRegionString = config.getString("ngrok_region");
    static final String ngroktoken = config.getString("ngrok_token");
    static final String discordWebhook = config.getString("discord_webhook_url");

    static final NgrokVersion ngrokVersion = NgrokVersion.V3;

    @Override
    public void onInitialize() {
        msg.log("Initializing PortForwarded 1.0 By Relism");
        msg.log("Github Repo: https://github.com/Relism/PortForwarded");
        msg.log("Modrinth Page: https://modrinth.com/mod/portforwarded");
        if(!checkConfig()){
            ServerLifecycleEvents.SERVER_STARTED.register(server -> {
                if (server != null) {
                    JavaNgrokConfig ngrokConfig = new JavaNgrokConfig.Builder()
                            .withNgrokVersion(ngrokVersion)
                            .withRegion(Region.valueOf(ngrokRegionString))
                            .withoutMonitoring()
                            .build();

                    ngrokClient = new NgrokClient.Builder()
                            .withJavaNgrokConfig(ngrokConfig)
                            .build();

                    ngrokClient.setAuthToken(ngroktoken);

                    Tunnel minecraftTunnel = NgrokHelper.createTunnel(Proto.TCP, server.getServerPort());
                    String tunnelUrl = minecraftTunnel.getPublicUrl().replaceFirst("^tcp://", "");
                    msg.log("==[PortForwarded]=========================================");
                    msg.log("Forwarded Port: " + server.getServerPort());
                    msg.log("Tunnel Running on URL: " + tunnelUrl);
                    if(!discordWebhook.isEmpty()){
                        DiscordWebhooksUtil.sendEmbed(discordWebhook, "New Server URL", "The Minecraft server has started on the following URL: " + tunnelUrl, 0xB4D3B2);
                    }
                } else {
                    throw new RuntimeException("Server is null!");
                }
            });
        }
    }

    private static boolean checkConfig() {
        boolean invalidConfig = false;

        if (ngroktoken == null || ngroktoken.equals("YOUR_NGROK_TOKEN")) {
            invalidConfig = true;
            msg.log("Error: Ngrok token is missing or invalid. Please set 'ngrok_token' in config.yml");
        }

        if (ngrokRegionString == null || !isValidRegion()) {
            invalidConfig = true;
            msg.log("Error: Ngrok region is missing or invalid. Please set 'ngrok_region' in config.yml with a valid region code");
        }

        if (discordWebhook != null && !isValidDiscordWebhook()) {
            invalidConfig = true;
            msg.log("Warning: Discord webhook URL might be invalid. Please double-check the format of 'discord_webhook_url' in config.yml");
        }

        if (invalidConfig) {
            msg.log("Disabling PortForwarded mod due to invalid configuration.");
        }

        return invalidConfig;
    }

    private static boolean isValidRegion() {
        try {
            Region.valueOf(ngrokRegionString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean isValidDiscordWebhook() {
        if (discordWebhook != null && discordWebhook.isEmpty()) return true;
        assert discordWebhook != null;
        return discordWebhook.startsWith("https://discord.com/api/webhooks/");
    }

    public static NgrokClient getNgrokClient(){ return ngrokClient; }
}