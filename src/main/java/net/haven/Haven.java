package net.haven;

import net.haven.commands.SpawnCommand;
import net.haven.commands.handlers.CommandHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class Haven extends JavaPlugin {
    private FileConfiguration messages;

    @Override
    public void onEnable() {
        Objects.requireNonNull(this.getCommand("hv")).setExecutor(new CommandHandler(this));
        Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new SpawnCommand(this));

        saveConfig();

        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        getLogger().info("Haven enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Haven disabled!");
    }

}