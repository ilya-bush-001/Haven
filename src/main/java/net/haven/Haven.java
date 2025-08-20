package net.haven;

import net.haven.commands.*;
import net.haven.utils.MessageUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Haven extends JavaPlugin {

    @Override
    public void onEnable() {
        messageUtils = new MessageUtils(this);

        HvCommand hvCommand = new HvCommand(this);
        Objects.requireNonNull(this.getCommand("hv")).setExecutor(new HavenCommand(this));
        Objects.requireNonNull(this.getCommand("hv")).setExecutor(new DeleteSpawnCommand(this));
        Objects.requireNonNull(this.getCommand("hv")).setExecutor(new ReloadCommand(this));
        Objects.requireNonNull(this.getCommand("hv")).setExecutor(new SetSpawnCommand(this));
        Objects.requireNonNull(this.getCommand("hv")).setExecutor(new SpawnCommand(this));

        saveConfig();

        getLogger().info("Haven enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Haven disabled!");
    }
}
