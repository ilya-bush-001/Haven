package net.haven;

import net.haven.commands.SetSpawnCommand;
import net.haven.commands.SpawnCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Haven extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Haven enabled!");

        Objects.requireNonNull(this.getCommand("setspawn")).setExecutor(new SetSpawnCommand(this));
        Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new SpawnCommand(this));

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("Haven disabled!");
    }
}
