package net.haven.commands.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigManager {
    private static JavaPlugin plugin;
    private static FileConfiguration messagesConfig;

    public static void initialize(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
        loadMessages();
    }

    public static void loadMessages() {
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public static String getMessage(String path) {
        if (messagesConfig == null) {
            return ChatColor.RED + "Messages not loaded!";
        }

        String message = messagesConfig.getString(path);
        if (message == null) {
            return ChatColor.RED + "Message not found: " + path;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getMessage(String path, Object... replacements) {
        String message = getMessage(path);
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(String.valueOf(replacements[i]),
                        String.valueOf(replacements[i + 1]));
            }
        }
        return message;
    }

    public static void reloadMessages() {
        loadMessages();
    }
}