package net.haven;

import net.haven.commands.SpawnCommand;
import net.haven.commands.handlers.CommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Haven extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Haven enabled!");

        Objects.requireNonNull(this.getCommand("hv")).setExecutor(new CommandHandler(this));
        Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new SpawnCommand(this));

        saveConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("Haven disabled!");
    }
}