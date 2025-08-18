package net.haven.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.haven.Haven;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final Haven plugin;

    public ReloadCommand(Haven plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!sender.hasPermission("haven.command.reload")) {
            sender.sendMessage(ChatColor.RED + "You don't have permissions!");
            return true;
        }

        try {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "You have successfully reloaded the plugin!");
            return true;
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "An error occurred while reloading the configuration!");
            e.printStackTrace();
            return false;
        }
    }

}
