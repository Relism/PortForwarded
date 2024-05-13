package dev.relism.portforwarded.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utility class for sending embeds to Discord via webhooks.
 */
public class DiscordWebhooksUtil {

    /**
     * Sends an embed to a Discord channel via a webhook.
     *
     * @param webhookUrl The URL of the Discord webhook.
     * @param title The title of the embed.
     * @param description The description of the embed.
     * @param color The color of the embed, as an integer.
     */
    public static void sendEmbed(String webhookUrl, String title, String description, int color) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String requestBody = "{\n" +
                    "    \"embeds\": [\n" +
                    "        {\n" +
                    "            \"title\": \"" + title + "\",\n" +
                    "            \"description\": \"" + description + "\",\n" +
                    "            \"color\": " + color + "\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";

            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            connection.getResponseCode();
            connection.getResponseMessage();

            connection.disconnect();
        } catch (IOException e) {
            msg.log("An error occurred while sending the webhook message: " + e.getMessage());
        }
    }
}