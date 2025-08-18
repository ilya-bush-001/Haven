package net.haven.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.haven.Haven;
import org.jetbrains.annotations.NotNull;

public class DeleteSpawnCommand implements CommandExecutor {

    private final Haven plugin;

    public DeleteSpawnCommand(Haven plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only for players!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("haven.command.deletespawn")) {
            player.sendMessage("You don't have permissions!");
            return true;
        }

        if (!plugin.getConfig().contains("spawn.world")) {
            player.sendMessage("SpawnPoint has not been set yet!");
            return true;
        }

        plugin.getConfig().set("spawn", null);
        plugin.saveConfig();

        player.sendMessage("SpawnPoint has been succesfully deleted!");
        return true;
    }

}
