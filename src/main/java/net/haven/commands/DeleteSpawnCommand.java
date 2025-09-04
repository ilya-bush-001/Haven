package net.haven.commands;

import net.haven.Haven;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeleteSpawnCommand implements CommandExecutor {

    private final Haven plugin;
    private static final String PERMISSION = "haven.command.deletespawn";

    public DeleteSpawnCommand(Haven plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd,
                             @NotNull String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMessage("messages.error", "&cYou must be a player to use this command!"));
            return true;
        }

        if (!player.hasPermission(PERMISSION)) {
            sender.sendMessage(plugin.getMessage("messages.no-permissions",
                    "&cYou don't have permission to execute this command!"));
            return true;
        }

        if (!isSpawnPointSet()) {
            sender.sendMessage(plugin.getMessage("messages.delspawn.not-set",
                    "&cSpawn point is not set!"));
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

            sender.sendMessage(plugin.getMessage("messages.delspawn.success",
                    "&aSpawn point deleted!"));
            return true;

        } catch (Exception e) {
            plugin.getLogger().severe("Failed to delete spawn point: " + e.getMessage());
            sender.sendMessage(plugin.getMessage("messages.error",
                    "&cFailed to delete spawn point! Check console for details."));
            return false;
        }
    }
}