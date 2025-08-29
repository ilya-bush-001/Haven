package net.haven.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.haven.Haven;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {

    private final Haven plugin;

    public SpawnCommand(Haven plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMessage("messages.error", "&cYou must be a player to use this command!"));
            return true;
        }

        if (!player.hasPermission("haven.command.spawn")) {
            sender.sendMessage(plugin.getMessage("messages.no-permissions", "&cYou don't have permission to execute this command!!"));
            return true;
        }

        String worldName = plugin.getConfig().getString("spawn.world");
        if (worldName == null) {
            sender.sendMessage(plugin.getMessage("messages.spawn.not-set", "&cSpawn point not set!"));
            return true;
        }

        Location spawnLocation = new Location(
                Bukkit.getWorld(worldName),
                plugin.getConfig().getDouble("spawn.x"),
                plugin.getConfig().getDouble("spawn.y"),
                plugin.getConfig().getDouble("spawn.z"),
                (float) plugin.getConfig().getDouble("spawn.yaw"),
                (float) plugin.getConfig().getDouble("spawn.pitch")
        );

        player.teleport(spawnLocation);
        sender.sendMessage(plugin.getMessage("messages.spawn.success", "&aTeleported to spawn!"));
        return true;
    }
}