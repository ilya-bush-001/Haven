package net.haven.commands;

import net.haven.Haven;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeleteSpawnCommand implements CommandExecutor {

    private final Haven plugin;
    private final BukkitAudiences audiences;
    private static final String PERMISSION = "haven.command.deletespawn";
    private final MiniMessage mm = MiniMessage.miniMessage();

    public DeleteSpawnCommand(Haven plugin, BukkitAudiences audiences) {
        this.plugin = plugin;
        this.audiences = audiences;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {

            String message = plugin.getMessage("messages.not-player", "<color:#F2000E>[✘] You don't have permission to execute this command!");
            Component parsed = mm.deserialize(message);
            audiences.sender(sender).sendMessage(parsed);

            return true;
        }

        if (!player.hasPermission(PERMISSION)) {
            String message = plugin.getMessage("messages.no-permissions", "<color:#F2000E>[✘] You must be a player to use this command!");
            Component parsed = mm.deserialize(message);
            audiences.sender(sender).sendMessage(parsed);
            return true;
        }

        if (!isSpawnPointSet()) {
            String message = plugin.getMessage("messages.delspawn.not-set", "<color:#F2000E>[✘] Spawn point is not set!");
            Component parsed = mm.deserialize(message);
            audiences.sender(sender).sendMessage(parsed);
            return true;
        }

        return deleteSpawnPoint(sender);
    }

    private boolean isSpawnPointSet() {
        return plugin.getConfig().contains("spawn.world");
    }

    private boolean deleteSpawnPoint(CommandSender sender) {
        try {
            plugin.getConfig().set("spawn", null);
            plugin.saveConfig();

            String message = plugin.getMessage("messages.delspawn.success", "<color:#38F200>[✘] &aSpawn deleted!");
            Component parsed = mm.deserialize(message);
            audiences.sender(sender).sendMessage(parsed);
            return true;

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to delete spawn point: " + e.getMessage());

            String message = plugin.getMessage("messages.error", "<color:#38F200>[✘] Failed to delete spawn point! Check console for details.!");
            Component parsed = mm.deserialize(message);
            audiences.sender(sender).sendMessage(parsed);
            return false;
        }
    }
}