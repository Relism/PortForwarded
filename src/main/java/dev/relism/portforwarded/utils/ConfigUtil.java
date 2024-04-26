package dev.relism.portforwarded.utils;

import dev.relism.portforwarded.PortForwarded;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigUtil {

    private final String filename;
    private final String friendlyFilename;
    private Map<String, Object> configData;

    public ConfigUtil(String filename) {
        this.friendlyFilename = filename;
        Path currentWorkingDir = Paths.get("").toAbsolutePath();
        Path configDir = currentWorkingDir.resolve("config");
        Path modDir = configDir.resolve(PortForwarded.MOD_ID);
        createDirectory(modDir);

        this.filename = String.format("%s/%s", modDir, filename);
        msg.log("Config file path: " + this.filename);
        this.configData = new HashMap<>();
        loadConfig();
    }

    private void createDirectory(Path directoryPath) {
        File directory = directoryPath.toFile();
        if (!directory.exists()) {
            try {
                FileUtils.forceMkdir(directory);
            } catch (IOException e) {
                msg.log("Error creating directory: " + directoryPath);
            }
        }
    }

    public void setValue(String key, Object value) {
        this.configData.put(key, value);
        saveConfig();
    }

    public String getString(String key) {
        Object value = configData.get(key);
        if (value instanceof String) {
            return (String) value;
        }
        return null;
    }

    public int getInt(String key) {
        Object value = configData.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return -1;
    }

    public boolean getBoolean(String key) {
        Object value = configData.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false;
    }

    public void saveConfig() {
        try (FileWriter writer = new FileWriter(filename)) {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            yaml.dump(configData, writer);
        } catch (IOException e) {
            msg.log("Error saving config: " + filename);
        }
    }

    private void loadConfig() {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
                setPresetConfigData();
            } catch (IOException e) {
                msg.log("Error creating config file: " + filename);
            }
        }
        if (file.exists()) {
            try (InputStream inputStream = new FileInputStream(file)) {
                Yaml yaml = new Yaml();
                configData = yaml.load(inputStream);
            } catch (Exception e) {
                msg.log("Error loading config file: " + filename);
            }
        }
    }

    private void setPresetConfigData() {
        switch(friendlyFilename) {
            case "config.yml":
                setValue("ngrok_token", "YOUR_NGROK_TOKEN");
                setValue("ngrok_region", "[EU, AP, AU, IN, JP, SA, US, US_CAL_1]");
                setValue("discord_webhook_url", "(leave blank for no webhook functionality)");
                break;
        }
    }
}

