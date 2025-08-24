package net.haven;

import net.haven.commands.SpawnCommand;
import net.haven.config.ConfigLocalization;
import net.haven.commands.handlers.CommandHandler;
import net.haven.completers.HavenTabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Haven extends JavaPlugin {
    private ConfigLocalization localization;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        localization = new ConfigLocalization(this);
        localization.loadLanguages();

        Objects.requireNonNull(this.getCommand("hv")).setExecutor(new CommandHandler(this));
        Objects.requireNonNull(this.getCommand("hv")).setTabCompleter(new HavenTabCompleter(this));
        Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new SpawnCommand(this));

        getLogger().info("Haven enabled!");
    }

    public String getMessage(String path, Object... replacements) {
        String message = localization.getMessage(path);

        for (int i = 0; i < replacements.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(replacements[i]));
        }

        return message;
    }

    public ConfigLocalization getLocalization() {
        return localization;
    }

    public List<String> getMessageList(String path) {
        return localization.getMessageList(path);
    }

    public List<String> getMessageList(String path, Object... replacements) {
        List<String> messages = localization.getMessageList(path);
        List<String> formattedMessages = new ArrayList<>();

        for (String message : messages) {
            for (int i = 0; i < replacements.length; i++) {
                message = message.replace("{" + i + "}", String.valueOf(replacements[i]));
            }
            formattedMessages.add(message);
        }

        return formattedMessages;
    }
}