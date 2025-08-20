package net.haven.commands;

import net.haven.Haven;
import net.haven.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HavenCommand implements CommandExecutor {

    private final Haven plugin;
    private final MessageUtils messages;

    public HavenCommand(Haven plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessageUtils();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            messages.sendMessage(sender, "messages.usage");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "setspawn":
                return setSpawnCommand(sender, args);
            case "spawn":
                return spawnCommand(sender, args);
            case "delspawn":
                return delSpawnCommand(sender, args);
            case "reload":
                return reloadCommand(sender, args);
            default:
                messages.sendMessage(sender, "messages.unknown-command");
                return true;
        }
    }

    private boolean delSpawnCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("haven.command.delspawn")) {
            messages.sendMessage(sender, "messages.no-permission");
            return true;
        }

        if (!plugin.getConfig().contains == null) {
            messages.sendMessage(sender, "messages.delspawn.not-set");
            return true;
        }

        player.teleport(location);
        messages.sendMessage(sender, "messages.delspawn.success");
        return true;
    }

    private boolean setSpawnCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("haven.command.setspawn")) {
            messages.sendMessage(sender, "messages.no-permission");
            return true;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();

        if (location == null) {
            messages.sendMessage(sender, "messages.spawn.not-set");
            return true;
        }

        player.teleport(location);
        messages.sendMessage(sender, "messages.spawn.spawn-success");
        return true;
    }

    private boolean reloadCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("haven.command.reload")) {
            messages.sendMessage(sender, "messages.no-permission");
            return true;
        }

        plugin.reloadConfig();
        plugin.loadSpawnLocation();
        plugin.reloadConfig();
    }

}
