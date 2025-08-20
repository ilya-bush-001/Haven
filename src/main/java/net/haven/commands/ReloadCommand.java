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
            sender.sendMessage(plugin.getMessage("messages.no-permissions", "&cYou don't have permission to execute this command!"));
            return true;
        }

        try {
            plugin.reloadConfig();
            plugin.reloadMessagesConfig();
            sender.sendMessage(plugin.getMessage("messages.reload.success", "&aConfiguration reloaded!"));
            return true;
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "An error occurred while reloading the configuration!");
            e.printStackTrace();
            return false;
        }
    }

}
