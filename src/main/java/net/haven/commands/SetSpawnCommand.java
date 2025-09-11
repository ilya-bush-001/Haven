package net.haven.commands;

import net.haven.Haven;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetSpawnCommand implements CommandExecutor {

    private final Haven plugin;
    private static final String PERMISSION = "haven.command.setspawn";
    private static final String SUCCESS_KEY = "messages.setspawn.success";
    private static final String DEFAULT_SUCCESS_MSG = "<color:#38F200>[✔] Spawn point successfully set!";
    private final BukkitAudiences audiences;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public SetSpawnCommand(Haven plugin, BukkitAudiences audiences) {
        this.plugin = plugin;
        this.audiences = audiences;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sendPlayerOnlyMessage(sender);
            return true;
        }

        if (!hasSetSpawnPermission(player)) {
            sendNoPermissionMessage(sender);
            return true;
        }

        return setSpawnLocation(player, sender);
    }

    private void sendPlayerOnlyMessage(CommandSender sender) {
        String message = plugin.getMessage("messages.not-player", "<color:#38F200>[✘] You must be a player to use this command!");
        Component parsed = mm.deserialize(message);
        audiences.sender(sender).sendMessage(parsed);
    }

    private boolean hasSetSpawnPermission(Player player) {
        return player.hasPermission(PERMISSION);
    }

    private void sendNoPermissionMessage(CommandSender sender) {
        String message = plugin.getMessage("messages.no-permissions", "<color:#38F200>[✘] You don't have permission to execute this command!");
        Component parsed = mm.deserialize(message);
        audiences.sender(sender).sendMessage(parsed);
    }

    private boolean setSpawnLocation(Player player, CommandSender sender) {
        try {
            Location location = player.getLocation();
            saveSpawnToConfig(location);

            plugin.getLocalization().reload();

            sendSuccessMessage(sender);
            return true;

        } catch (Exception e) {
            handleSpawnSetError(sender, e);
            return false;
        }
    }

    private void saveSpawnToConfig(Location location) {
        plugin.getConfig().set("spawn.world", location.getWorld().getName());
        plugin.getConfig().set("spawn.x", location.getX());
        plugin.getConfig().set("spawn.y", location.getY());
        plugin.getConfig().set("spawn.z", location.getZ());
        plugin.getConfig().set("spawn.yaw", location.getYaw());
        plugin.getConfig().set("spawn.pitch", location.getPitch());

        plugin.saveConfig();
    }

    private void sendSuccessMessage(CommandSender sender) {
        String message = plugin.getMessage(SUCCESS_KEY, DEFAULT_SUCCESS_MSG);
        Component parsed = mm.deserialize(message);
        audiences.sender(sender).sendMessage(parsed);
    }

    private void handleSpawnSetError(CommandSender sender, Exception e) {
        String message = plugin.getMessage("messages.setspawn.error", "<color:#38F200>[✘] Failed to set spawn point! Check console for details.");
        Component parsed = mm.deserialize(message);
        audiences.sender(sender).sendMessage(parsed);

        plugin.getLogger().severe("Failed to set spawn point: " + e.getMessage());
        e.printStackTrace();
    }
}