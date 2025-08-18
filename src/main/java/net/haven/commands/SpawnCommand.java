package net.haven.commands;

import net.md_5.bungee.api.ChatColor;
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
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command only for players!");
            return true;
        }

        Player player = null;

        if (!player.hasPermission("haven.command.spawn")) {
            player.sendMessage(ChatColor.RED + "You don't have permissions!");
            return true;
        }

        player = (Player) sender;

        String worldName = plugin.getConfig().getString("spawn.world");
        if (worldName == null) {
            player.sendMessage(ChatColor.RED + "SpawnPoint has not been set yet!");
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
        player.sendMessage(ChatColor.GREEN + "Teleported to spawn!");
        return true;
    }
}
