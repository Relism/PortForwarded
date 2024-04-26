package dev.relism.portforwarded.utils;

import dev.relism.portforwarded.PortForwarded;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class msg {
    public static final Logger LOGGER = LoggerFactory.getLogger(PortForwarded.MOD_ID);

    public static void log(String message){
        LOGGER.info(message);
    }

    public static void send(@NotNull ServerPlayerEntity player, String message){
        player.sendMessage(Text.of(message));
    }
}
