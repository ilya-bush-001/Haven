package net.haven.commands.config;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class ConfigManager {
    private static JavaPlugin plugin;
    private static ConfigLocalization localization;

    public static void initialize(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    public static void setLocalization(ConfigLocalization loc) {
        localization = loc;
    }

    public static String getMessage(String path) {
        if (localization == null) {
            return ChatColor.RED + "Localization not initialized!";
        }
        return localization.getMessage(path);
    }

    public static String getMessage(String path, Object... replacements) {
        String message = getMessage(path);

        for (int i = 0; i < replacements.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(replacements[i]));
        }

        return message;
    }

    public static String getMessage(String path, Player player) {
        if (localization == null) {
            return ChatColor.RED + "Localization not initialized!";
        }
        return localization.getMessage(path, player, new HashMap<>());
    }

    public static void reloadMessages() {
        if (localization != null) {
            localization.reload();
        }
    }
}