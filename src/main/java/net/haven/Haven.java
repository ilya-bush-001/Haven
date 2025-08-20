package net.haven;

import net.haven.commands.DeleteSpawnCommand;
import net.haven.commands.ReloadCommand;
import net.haven.commands.SetSpawnCommand;
import net.haven.commands.SpawnCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Haven extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Haven enabled!");

        Objects.requireNonNull(this.getCommand("hv")).setExecutor(new SetSpawnCommand(this));

        Objects.requireNonNull(this.getCommand("setspawn")).setExecutor(new SetSpawnCommand(this));
        Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new SpawnCommand(this));
        Objects.requireNonNull(this.getCommand("delspawn")).setExecutor(new DeleteSpawnCommand(this));
        Objects.requireNonNull(this.getCommand("hvreload")).setExecutor(new ReloadCommand(this));

        saveConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("Haven disabled!");
    }
}