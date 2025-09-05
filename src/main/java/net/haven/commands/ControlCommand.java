package net.haven.commands;

import net.haven.Haven;
import net.haven.listeners.MenuListener;
import net.haven.gui.ControlGUIHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ControlCommand implements CommandExecutor {

    private final Haven plugin;
    private final MenuListener menuListener;
    private final ReloadCommand reloadCommand;

    public ControlCommand(Haven plugin, MenuListener menuListener, ReloadCommand reloadCommand) {
        this.plugin = plugin;
        this.menuListener = menuListener;
        this.reloadCommand = reloadCommand;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMessage("messages.error", "&cYou must be a player to use this command!"));
            return true;
        }

        if (!player.hasPermission("haven.command.control")) {
            sender.sendMessage(plugin.getMessage("messages.no-permissions",
                    "&cYou don't have permission to execute this command!"));
            return true;
        }

        try {
            ControlGUIHolder controlMenu = new ControlGUIHolder(menuListener, reloadCommand);
            player.openInventory(controlMenu.getInventory());

            player.sendMessage(plugin.getMessage("messages.opened", "&aYou opened control panel"));
            return true;

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to open control panel for player: " + player.getName());
            plugin.getLogger().severe("Error: " + e.getMessage());

            player.sendMessage(plugin.getMessage("messages.error",
                    "&cAn error occurred while opening the control panel!"));
            return false;
        }
    }
}