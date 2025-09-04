package net.haven.config;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ConfigLocalization {
    private final JavaPlugin plugin;
    private String defaultLanguage;
    private final Map<String, FileConfiguration> languages = new HashMap<>();
    private final Map<String, File> languageFiles = new HashMap<>();

    private static final String[] DEFAULT_LANGUAGES = {
            "english", "russian", "ukrainian", "spanish",
            "german", "french", "polish", "portuguese"
    };

    public ConfigLocalization(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadLanguages() {
        plugin.reloadConfig();
        defaultLanguage = plugin.getConfig().getString("language", "english");

        File languagesFolder = initializeLanguagesFolder();
        createDefaultLanguageFiles();
        loadAllLanguageConfigs();
        validateDefaultLanguage();
    }

    private File initializeLanguagesFolder() {
        File languagesFolder = new File(plugin.getDataFolder(), "languages");
        if (!languagesFolder.exists()) {
            if (languagesFolder.mkdirs()) {
                plugin.getLogger().info("Created languages directory: " + languagesFolder.getPath());
            } else {
                plugin.getLogger().warning("Failed to create languages directory!");
            }
        }
        return languagesFolder;
    }

    private void createDefaultLanguageFiles() {
        for (String language : DEFAULT_LANGUAGES) {
            saveLanguageFile(language + ".yml");
        }
    }

    private void saveLanguageFile(String fileName) {
        File file = new File(plugin.getDataFolder(), "languages/" + fileName);
        if (!file.exists()) {
            try {
                plugin.saveResource("languages/" + fileName, false);
                plugin.getLogger().info("Created default language file: " + fileName);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Default language file not found in resources: " + fileName);
            }
        }
    }

    private void loadAllLanguageConfigs() {
        languages.clear();
        languageFiles.clear();

        File languagesFolder = new File(plugin.getDataFolder(), "languages");
        File[] languageFiles = languagesFolder.listFiles((dir, name) -> name.endsWith(".yml"));

        if (languageFiles != null) {
            for (File file : languageFiles) {
                String languageName = file.getName().replace(".yml", "").toLowerCase();
                loadLanguageConfig(languageName, file);
            }
        }
    }

    private void loadLanguageConfig(String languageName, File file) {
        try {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            languages.put(languageName, configuration);
            this.languageFiles.put(languageName, file);
            plugin.getLogger().info("Loaded language: " + languageName);
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Failed to load language file: " + file.getName(), e);
        }
    }

    private void validateDefaultLanguage() {
        if (!languages.containsKey(defaultLanguage)) {
            if (!languages.isEmpty()) {
                defaultLanguage = languages.keySet().iterator().next();
                plugin.getLogger().warning("Default language not found, using: " + defaultLanguage);
            } else {
                plugin.getLogger().severe("No language files found! Localization will not work properly.");
            }
        }
    }

    public String getMessage(String key) {
        return getMessage(key, null, new HashMap<>());
    }

    public String getMessage(String key, Player player) {
        return getMessage(key, player, new HashMap<>());
    }

    public String getMessage(String key, Player player, Map<String, String> placeholders) {
        return getMessage(key, player, placeholders, defaultLanguage);
    }

    public String getMessage(String key, Player player, Map<String, String> placeholders, String language) {
        FileConfiguration languageConfig = getLanguageConfig(language);

        if (languageConfig == null || !languageConfig.contains(key)) {
            return getFallbackMessage(key);
        }

        String message = languageConfig.getString(key);
        return formatMessage(message, player, placeholders);
    }

    private FileConfiguration getLanguageConfig(String language) {
        FileConfiguration config = languages.get(language.toLowerCase());
        if (config == null) {
            config = languages.get(defaultLanguage);
        }
        return config;
    }

    private String getFallbackMessage(String key) {
        return ChatColor.RED + "Missing message: " + key;
    }

    private String formatMessage(String message, Player player, Map<String, String> placeholders) {
        if (message == null) {
            return ChatColor.RED + "Null message";
        }

        if (player != null) {
            message = message.replace("%player%", player.getName());
        }

        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                message = message.replace("%" + entry.getKey() + "%", entry.getValue());
            }
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public boolean hasLanguage(String language) {
        return languages.containsKey(language.toLowerCase());
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String language) {
        if (hasLanguage(language)) {
            this.defaultLanguage = language.toLowerCase();
            plugin.getConfig().set("language", language);
            plugin.saveConfig();
            plugin.getLogger().info("Default language set to: " + language);
        } else {
            plugin.getLogger().warning("Cannot set default language: " + language + " - not available");
        }
    }

    public void reload() {
        plugin.reloadConfig();
        defaultLanguage = plugin.getConfig().getString("language", "english");
        languages.clear();
        loadLanguages();
        plugin.getLogger().info("Localization reloaded");
    }

    public Map<String, FileConfiguration> getLanguages() {
        return new HashMap<>(languages);
    }

    public List<String> getMessageList(String key) {
        return getMessageList(key, null, new HashMap<>());
    }

    public List<String> getMessageList(String key, Player player, Map<String, String> placeholders) {
        FileConfiguration configuration = languages.get(defaultLanguage);

        if (configuration == null || !configuration.contains(key)) {
            return new ArrayList<>();
        }

        List<String> messages = configuration.getStringList(key);
        List<String> formattedMessages = new ArrayList<>();

        for (String message : messages) {
            formattedMessages.add(formatMessage(message, player, placeholders));
        }

        return formattedMessages;
    }

    public File getLanguageFile(String language) {
        return languageFiles.get(language.toLowerCase());
    }
}