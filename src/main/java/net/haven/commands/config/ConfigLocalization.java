package net.haven.commands.config;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ConfigLocalization {
    private final JavaPlugin plugin;
    private String defaultLanguage;
    private final Map<String, FileConfiguration> languages = new HashMap<>();
    private final Map<String, File> languagesFiles = new HashMap<>();

    public ConfigLocalization(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadLanguages() {
        plugin.saveConfig();
        plugin.reloadConfig();
        defaultLanguage = plugin.getConfig().getString("language", "en");

        File languagesFolder = new File(plugin.getDataFolder(), "languages");
        if (!languagesFolder.exists()) {
            languagesFolder.mkdirs();
        }

        saveLanguageFile("english.yml");
        saveLanguageFile("russian.yml");

        File[] languageFiles = languagesFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (languageFiles != null) {
            for (File file : languageFiles) {
                String languageName = file.getName().replace(".yml", "");
                FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);

                try {
                    InputStream stream = plugin.getResource("languages/" + file.getName());
                    if (stream != null) {
                        configuration.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8)));
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Cannot load default messages for " + languageName);
                }
                languages.put(languageName, configuration);
                this.languagesFiles.put(languageName, file);
                plugin.getLogger().info("Loaded language: " + languageName);
            }
        }

        if (!languages.containsKey(defaultLanguage)) {
            plugin.getLogger().warning("Default language '" + defaultLanguage + "' not found! Using first available language.");
            if (!languages.isEmpty()) {
                defaultLanguage = languages.keySet().iterator().next();
            } else {
                plugin.getLogger().severe("No language files found!");
            }
        }
    }

    private void saveLanguageFile(String fileName) {
        File file = new File(plugin.getDataFolder(), "languages/" + fileName);
        if (!file.exists()) {
            plugin.saveResource("languages/" + fileName, false);
            plugin.getLogger().info("Created default language file: " + fileName);
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

        FileConfiguration languagesConfig = languages.get(language);

        if (languagesConfig == null) {
            languagesConfig = languages.get(defaultLanguage);
            if (languagesConfig == null) {
                return ChatColor.RED + "Language not loaded: " + key;
            }
        }

        if (!languagesConfig.contains(key)) {
            return ChatColor.RED + "Missing message: " + key;
        }

        String message = languagesConfig.getString(key);

        if (player != null) {
            message = message.replace("%player%", player.getName());
        }

        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                message = message.replace("%" + entry.getKey() + "%", entry.getValue());
            }
        }

        return message.replace('&', '§');
    }

    public boolean hasLanguage(String language) {
        return languages.containsKey(language);
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String language) {
        if (languages.containsKey(language)) {
            this.defaultLanguage = language;
            plugin.getConfig().set("language", language);
            plugin.saveConfig();
        }
    }

    public void reload() {
        plugin.reloadConfig();
        defaultLanguage = plugin.getConfig().getString("language", "english");

        languages.clear();
        loadLanguages();
    }

    public Map<String,FileConfiguration> getLanguages() {
        return languages;
    }
}
