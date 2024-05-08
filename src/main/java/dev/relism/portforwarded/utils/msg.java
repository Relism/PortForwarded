package dev.relism.portforwarded.utils;

import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class msg {

    public static final Logger LOGGER = LogManager.getLogger(msg.class);
    public static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_COLOR_PREFIX = "\u001B[38;2;";

    public static void log(String message) {
        LOGGER.info(translateColorCodes(message) + ANSI_RESET);
    }

    private static String translateColorCodes(String text) {
        StringBuilder translatedText = new StringBuilder();

        Matcher matcher = HEX_PATTERN.matcher(text);
        int currentIndex = 0;

        while (matcher.find()) {
            translatedText.append(translateColorCodes(text, currentIndex, matcher.start()));
            translatedText.append(matcher.group(1));
            currentIndex = matcher.end();
        }

        translatedText.append(translateColorCodes(text, currentIndex, text.length()));

        return translatedText.toString();
    }

    private static String translateColorCodes(String text, int startIndex, int endIndex) {
        StringBuilder translatedText = new StringBuilder();
        int colorCodeIndex = text.indexOf("&#", startIndex);

        if (colorCodeIndex != -1 && colorCodeIndex + 7 < endIndex) {
            translatedText.append(text, startIndex, colorCodeIndex);

            String hexColorCode = text.substring(colorCodeIndex + 2, colorCodeIndex + 8);
            String ansiColorCode = convertHexToAnsi(hexColorCode);

            if (ansiColorCode != null) {
                translatedText.append(ANSI_COLOR_PREFIX).append(ansiColorCode).append("m");
            } else {
                translatedText.append("&#");
                translatedText.append(hexColorCode);
            }

            translatedText.append(text, colorCodeIndex + 8, endIndex);
        } else {
            translatedText.append(text, startIndex, endIndex);
        }

        return translatedText.toString();
    }

    private static String convertHexToAnsi(String hexColorCode) {
        try {
            int red = Integer.parseInt(hexColorCode.substring(0, 2), 16);
            int green = Integer.parseInt(hexColorCode.substring(2, 4), 16);
            int blue = Integer.parseInt(hexColorCode.substring(4, 6), 16);
            return red + ";" + green + ";" + blue;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}