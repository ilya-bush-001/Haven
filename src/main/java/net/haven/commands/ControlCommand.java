package net.haven.commands;

import net.haven.Haven;
import net.haven.listeners.MenuListener;
import net.haven.gui.ControlGUIHolder;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ControlCommand implements CommandExecutor {

    private final Haven plugin;
    private final MenuListener menuListener;
    private final ReloadCommand reloadCommand;
    private final MiniMessage mm = MiniMessage.miniMessage();
    private final BukkitAudiences audiences;

    public ControlCommand(Haven plugin, MenuListener menuListener, ReloadCommand reloadCommand, BukkitAudiences audiences) {
        this.plugin = plugin;
        this.menuListener = menuListener;
        this.reloadCommand = reloadCommand;
        this.audiences = audiences;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMessage("messages.error", "&cYou must be a player to use this command!"));
            return true;
        }

        if (!player.hasPermission("haven.command.control")) {

            String message = plugin.getMessage("messages.no-permissions", "<color:#38F200>[✘] You don't have permission to execute this command!!");
            Component parsed = mm.deserialize(message);
            audiences.sender(sender).sendMessage(parsed);
            return true;
        }

        try {
            ControlGUIHolder controlMenu = new ControlGUIHolder(menuListener, reloadCommand);
            player.openInventory(controlMenu.getInventory());
            return true;

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to open control panel for player: " + player.getName());
            plugin.getLogger().severe("Error: " + e.getMessage());

            String message = plugin.getMessage("messages.error", "<color:#38F200>[✘] An error occurred while opening the control panel!");
            Component parsed = mm.deserialize(message);
            audiences.sender(sender).sendMessage(parsed);
            return false;
        }
    }
}