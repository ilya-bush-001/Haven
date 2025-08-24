package net.haven.commands;

import net.md_5.bungee.api.ChatColor;
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
            sender.sendMessage(plugin.getMessage("messages.error", "&cYou must be a player to use this command!"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("haven.command.deletespawn")) {
            sender.sendMessage(plugin.getMessage("messages.no-permissions", "&cYou don't have permission to execute this command!"));
            return true;
        }

        if (!plugin.getConfig().contains("spawn.world")) {
            sender.sendMessage(plugin.getMessage("messages.delspawn.success", "&aSpawn point successfully set!"));
            return true;
        }

        plugin.getConfig().set("spawn", null);
        plugin.saveConfig();

        sender.sendMessage(plugin.getMessage("messages.delspawn.success", "&aSpawn point deleted!"));
        return true;
    }

}