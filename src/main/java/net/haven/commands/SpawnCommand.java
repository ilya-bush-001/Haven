package net.haven.commands;

import net.haven.Haven;
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
    private static final String DEFAULT_SUCCESS_MSG = "&aTeleported to spawn!";
    private static final String NOT_SET_KEY = "messages.spawn.not-set";
    private static final String DEFAULT_NOT_SET_MSG = "&cSpawn point not set!";

    public SpawnCommand(Haven plugin) {
        this.plugin = plugin;
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
        sender.sendMessage(plugin.getMessage("messages.error", "&cYou must be a player to use this command!"));
    }

    private boolean hasSpawnPermission(Player player) {
        return player.hasPermission(PERMISSION);
    }

    private void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(plugin.getMessage("messages.no-permissions", "&cYou don't have permission to execute this command!"));
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
        sender.sendMessage(plugin.getMessage(NOT_SET_KEY, DEFAULT_NOT_SET_MSG));
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
        sender.sendMessage(plugin.getMessage("messages.error", "&cConfigured spawn point is invalid! Please set a new spawn."));
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
        sender.sendMessage(plugin.getMessage(SUCCESS_KEY, DEFAULT_SUCCESS_MSG));
    }

    private void handleTeleportError(CommandSender sender, Exception e) {
        sender.sendMessage(plugin.getMessage("messages.error", "&cFailed to teleport to spawn! Check console for details."));

        plugin.getLogger().severe("Teleport failed: " + e.getMessage());
        e.printStackTrace();
    }
}