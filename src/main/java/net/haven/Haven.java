package net.haven;

import net.haven.commands.SpawnCommand;
import net.haven.commands.config.ConfigManager;
import net.haven.commands.handlers.CommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Haven extends JavaPlugin {

    @Override
    public void onEnable() {
        saveConfig();

        ConfigManager.initialize(this);

        Objects.requireNonNull(this.getCommand("hv")).setExecutor(new CommandHandler(this));
        Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new SpawnCommand(this));

        getLogger().info("Haven enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Haven disabled!");
    }

    public String getMessage(String path) {
        return ConfigManager.getMessage(path);
    }

    public String getMessage(String path, Object... replacements) {
        return ConfigManager.getMessage(path, replacements);
    }

    public void reloadMessagesConfig() {
        ConfigManager.reloadMessages();
    }
}