package dev.relism.portforwarded.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebhooksUtil {

    public static void sendEmbed(String webhookUrl, String title, String description, int color) {
        try {
            msg.log("Sending webhook message");
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