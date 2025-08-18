package net.haven.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.haven.Haven;

public class SpawnCommand implements CommandExecutor {

    private final Haven plugin;

    public SpawnCommand(Haven plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command only for players!");
            return true;
        }

        Player player = (Player) sender;

        String worldName = plugin.getConfig().getString("spawn.world");
        if (worldName == null) {
            player.sendMessage("SpawnPoint has not been installed yet!");
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
        player.sendMessage("Teleported to spawn!");
        return true;
    }
}
