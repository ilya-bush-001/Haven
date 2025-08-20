package net.haven;

import net.haven.commands.DeleteSpawnCommand;
import net.haven.commands.ReloadCommand;
import net.haven.commands.SetSpawnCommand;
import net.haven.commands.SpawnCommand;
import net.haven.utils.MessageUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Haven extends JavaPlugin {

    @Override
    public void onEnable() {
        messageUtils = new MessageUtils(this);

        getLogger().info("Haven enabled!");

        HvCommand hvCommand = new HvCommand(this);
        Objects.requireNonNull(this.getCommand("hv")).setExecutor(new HavenCommand(this));

        saveConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("Haven disabled!");
    }
}
