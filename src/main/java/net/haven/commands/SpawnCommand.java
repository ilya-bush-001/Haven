package net.haven.commands;

import net.haven.Haven;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {

    private final Haven plugin;
    private static final String PERMISSION = "haven.command.spawn";
    private static final String SUCCESS_KEY = "messages.spawn.success";
    private static final String DEFAULT_SUCCESS_MSG = "<color:#38F200>[✔] Spawn point successfully set!";
    private static final String DEFAULT_NOT_SET_MSG = "<color:#F2000E>[✘] Spawn point not set!";
    private static final String NOT_SET_KEY = "messages.spawn.not-set";
    private final BukkitAudiences audiences;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public SpawnCommand(Haven plugin, BukkitAudiences audiences) {
        this.plugin = plugin;
        this.audiences = audiences;
    }

    public Haven getPlugin() {
        return plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sendPlayerOnlyMessage(sender);
            return true;
        }

        if (!hasSpawnPermission(player)) {
            sendNoPermissionMessage(sender);
            return true;
        }

        return teleportToSpawn(player, sender);
    }

    private void sendPlayerOnlyMessage(CommandSender sender) {
        String message = plugin.getMessage("messages.not-player", "<color:#38F200>[✘] You must be a player to use this command!");
        Component parsed = mm.deserialize(message);
        audiences.sender(sender).sendMessage(parsed);
    }

    private boolean hasSpawnPermission(Player player) {
        return player.hasPermission(PERMISSION);
    }

    private void sendNoPermissionMessage(CommandSender sender) {
        String message = plugin.getMessage("messages.no-permissions", "<color:#38F200>[✘] You don't have permission to execute this command!");
        Component parsed = mm.deserialize(message);
        audiences.sender(sender).sendMessage(parsed);
    }

    private boolean teleportToSpawn(Player player, CommandSender sender) {
        if (!isSpawnConfigured()) {
            sendSpawnNotSetMessage(sender);
            return true;
        }

        Location spawnLocation = getSpawnLocation();
        if (spawnLocation == null || spawnLocation.getWorld() == null) {
            sendInvalidSpawnMessage(sender);
            return true;
        }

        return performTeleport(player, sender, spawnLocation);
    }

    private boolean isSpawnConfigured() {
        return plugin.getConfig().contains("spawn.world");
    }

    private void sendSpawnNotSetMessage(CommandSender sender) {
        String message = plugin.getMessage(NOT_SET_KEY, DEFAULT_NOT_SET_MSG);
        Component parsed = mm.deserialize(message);
        audiences.sender(sender).sendMessage(parsed);
    }

    private Location getSpawnLocation() {
        String worldName = plugin.getConfig().getString("spawn.world");
        if (worldName == null) {
            return null;
        }

        return new Location(
                Bukkit.getWorld(worldName),
                plugin.getConfig().getDouble("spawn.x"),
                plugin.getConfig().getDouble("spawn.y"),
                plugin.getConfig().getDouble("spawn.z"),
                (float) plugin.getConfig().getDouble("spawn.yaw"),
                (float) plugin.getConfig().getDouble("spawn.pitch")
        );
    }

    private void sendInvalidSpawnMessage(CommandSender sender) {
        String message = plugin.getMessage("messages.not-player", "<color:#38F200>[✘] You must be a player to use this command!");
        Component parsed = mm.deserialize(message);
        audiences.sender(sender).sendMessage(parsed);

        sender.sendMessage(plugin.getMessage("messages.spawn.error", "Configured spawn point is invalid! Please set a new spawn."));
    }

    private boolean performTeleport(Player player, CommandSender sender, Location spawnLocation) {
        try {
            player.teleport(spawnLocation);
            sendSuccessMessage(sender);
            return true;

        } catch (Exception e) {
            handleTeleportError(sender, e);
            return false;
        }
    }

    private void sendSuccessMessage(CommandSender sender) {
        String message = plugin.getMessage(SUCCESS_KEY, DEFAULT_SUCCESS_MSG);
        Component parsed = mm.deserialize(message);
        audiences.sender(sender).sendMessage(parsed);
    }

    private void handleTeleportError(CommandSender sender, Exception e) {
        String message = plugin.getMessage("messages.error", "<color:#38F200>[✘] Failed to teleport to spawn! Check console for details.");
        Component parsed = mm.deserialize(message);
        audiences.sender(sender).sendMessage(parsed);

        plugin.getLogger().severe("Teleport failed: " + e.getMessage());
        e.printStackTrace();
    }
}