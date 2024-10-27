package net.watakak;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MixSetsConfig {
    private static final String CONFIG_FILE = "config/mixsets.properties";
    private static final String DEFAULT_CONFIG_PATH = "/assets/mixsets/mixsets.properties";
    private Properties properties;

    public MixSetsConfig() {
        properties = new Properties();
        loadConfig();
    }

    private void loadConfig() {
        File configFile = new File(CONFIG_FILE);
        if (configFile.exists()) {
            try (FileInputStream input = new FileInputStream(configFile)) {
                properties.load(input);
            } catch (IOException e) {
                MixSets.LOGGER.error("Failed to load config file", e);
            }
        } else {
            copyDefaultConfig();
            loadConfig(); // Загружаем конфиг после копирования
        }
    }

    private void copyDefaultConfig() {
        try (InputStream input = getClass().getResourceAsStream(DEFAULT_CONFIG_PATH)) {
            if (input == null) {
                MixSets.LOGGER.error("Default configuration file not found at " + DEFAULT_CONFIG_PATH);
                return;
            }
            File configFile = new File(CONFIG_FILE);
            Files.createDirectories(configFile.getParentFile().toPath());
            Files.copy(input, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            MixSets.LOGGER.info("Default configuration file copied to " + CONFIG_FILE);
        } catch (IOException e) {
            MixSets.LOGGER.error("Failed to copy default config file", e);
        }
    }

    // Greetings - watakak
    public boolean isGreetingsEnabled() {
        return Boolean.parseBoolean(properties.getProperty("Greetings", "true"));
    }

    public List<String> getGreetingMessages() {
        String messages = properties.getProperty("Greetings.Messages", "Hello Fabric World!");
        return new ArrayList<>(Arrays.asList(messages.split(",")));
    }

    // Title Clearing Fix - DuncanRuns
    public boolean isTitleClearingFixEnabled() {
        return Boolean.parseBoolean(properties.getProperty("TitleClearingFix", "true"));
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        try (FileOutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, "MixSets Client Configurations");
        } catch (IOException e) {
            MixSets.LOGGER.error("Failed to save config file", e);
        }
    }
}
