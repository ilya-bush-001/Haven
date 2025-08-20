package net.haven.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.haven.Haven;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements CommandExecutor {

    private final Haven plugin;

    public HelpCommand(Haven plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("haven.command.help")) {
            sender.sendMessage(plugin.getMessage("messages.no-permissions", "&cYou don't have permission to execute this command!"));
            return true;
        }

        showHelp(sender);
        return true;
    }

    public void showHelp(CommandSender sender) {
        String[] helpMessages = {
                ChatColor.GOLD + "=== " + ChatColor.GREEN + "Haven Commands" + ChatColor.GOLD + " ===",
                ChatColor.YELLOW + "/hv setspawn" + ChatColor.GRAY + " - Set spawn point",
                ChatColor.YELLOW + "/hv delspawn" + ChatColor.GRAY + " - Delete spawn point",
                ChatColor.YELLOW + "/hv reload" + ChatColor.GRAY + " - Reload configuration",
                ChatColor.YELLOW + "/hv help" + ChatColor.GRAY + " - Show this help",
                ChatColor.YELLOW + "/spawn" + ChatColor.GRAY + " - Teleport to spawn"
        };

        sender.sendMessage(helpMessages);
    }
}