package net.haven.commands;

import net.haven.Haven;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final Haven plugin;
    private static final String PERMISSION = "haven.command.reload";
    private static final String RELOAD_SUCCESS_KEY = "messages.reload.success";
    private static final String DEFAULT_SUCCESS_MSG = "<color:#38F200>[✔] Configuration reloaded!";
    private final BukkitAudiences audiences;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ReloadCommand(Haven plugin, BukkitAudiences audiences) {
        this.plugin = plugin;
        this.audiences = audiences;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        return reload(sender);
    }

    public boolean reload(CommandSender sender) {
        if (!hasReloadPermission(sender)) {
            sendNoPermissionMessage(sender);
            return true;
        }

        try {
            performReload();
            sendSuccessMessage(sender);
            return true;

        } catch (Exception e) {
            handleReloadError(sender, e);
            return false;
        }
    }

    private boolean hasReloadPermission(CommandSender sender) {
        return sender.hasPermission(PERMISSION);
    }

    private void sendNoPermissionMessage(CommandSender sender) {
        String message = plugin.getMessage("messages.no-permissions", "<color:#38F200>[✘] You don't have permission to execute this command!");
        Component parsed = mm.deserialize(message);
        audiences.sender(sender).sendMessage(parsed);
    }

    private void performReload() throws Exception {
        plugin.reloadConfig();
        plugin.getLocalization().reload();

        plugin.getLogger().info("Configuration and localization files reloaded successfully");
    }

    private void sendSuccessMessage(CommandSender sender) {
        String message = plugin.getMessage(RELOAD_SUCCESS_KEY, DEFAULT_SUCCESS_MSG);
        Component parsed = mm.deserialize(message);
        audiences.sender(sender).sendMessage(parsed);
    }

    private void handleReloadError(CommandSender sender, Exception e) {
        String errorMessage = ChatColor.RED + "An error occurred while reloading the configuration!";
        sender.sendMessage(errorMessage);

        plugin.getLogger().severe("Reload failed: " + e.getMessage());
        plugin.getLogger().severe("Stack trace: ");
        e.printStackTrace();
    }
}