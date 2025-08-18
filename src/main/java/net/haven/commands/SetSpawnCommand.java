package net.haven.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.haven.Haven;
import org.jetbrains.annotations.NotNull;

public class SetSpawnCommand implements CommandExecutor {
    private final Haven plugin;

    public SetSpawnCommand(Haven plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only for players!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("haven.command.setspawn")) {
            player.sendMessage("You don't have permissions!");
            return true;
        }

        Location location = player.getLocation();

        plugin.getConfig().set("spawn.world", location.getWorld().getName());
        plugin.getConfig().set("spawn.x", location.getX());
        plugin.getConfig().set("spawn.y", location.getY());
        plugin.getConfig().set("spawn.z", location.getZ());
        plugin.getConfig().set("spawn.yaw", location.getYaw());
        plugin.getConfig().set("spawn.pitch", location.getPitch());

        player.sendMessage("SpawnPoint has been successfully created!");
        return true;
    }
}
