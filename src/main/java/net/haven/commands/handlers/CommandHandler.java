package net.haven.commands.handlers;

import net.haven.Haven;
import net.haven.commands.*;
import net.haven.listeners.MenuListener;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandHandler implements CommandExecutor {

    private final Haven plugin;
    private final SetSpawnCommand setSpawnCommand;
    private final DeleteSpawnCommand deleteSpawnCommand;
    private final ReloadCommand reloadCommand;
    private final HelpCommand helpCommand;
    private final ControlCommand controlCommand;
    private final SpawnCommand spawnCommand;
    private final MenuListener menuListener;
    private final BukkitAudiences audiences;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public CommandHandler(Haven plugin, MenuListener menuListener, BukkitAudiences audiences) {
        this.plugin = plugin;
        this.menuListener = menuListener;

        this.setSpawnCommand = new SetSpawnCommand(plugin, audiences);
        this.deleteSpawnCommand = new DeleteSpawnCommand(plugin, audiences);
        this.reloadCommand = new ReloadCommand(plugin, audiences);
        this.helpCommand = new HelpCommand(plugin, audiences);
        this.spawnCommand = new SpawnCommand(plugin, audiences);
        this.audiences = audiences;
        this.controlCommand = new ControlCommand(plugin, menuListener, reloadCommand, audiences);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        if (args.length == 0) {
            helpCommand.showHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        String[] subArgs = getSubArgs(args);

        return switch (subCommand) {
            case "setspawn" -> setSpawnCommand.onCommand(sender, cmd, label, subArgs);
            case "delspawn" -> deleteSpawnCommand.onCommand(sender, cmd, label, subArgs);
            case "spawn" -> spawnCommand.onCommand(sender, cmd, label, subArgs);
            case "reload" -> reloadCommand.onCommand(sender, cmd, label, subArgs);
            case "control" -> controlCommand.onCommand(sender, cmd, label, subArgs);
            case "help" -> {
                helpCommand.showHelp(sender);
                yield true;
            }
            default -> {

                String message = plugin.getMessage("messages.unknown-command", "<color:#F2000E>[✘] Unknown command! Use</color><gradient:#F27200:#F29900>/hv help</gradient>");
                Component parsed = mm.deserialize(message);
                audiences.sender(sender).sendMessage(parsed);
                yield true;
            }
        };
    }

    private String[] getSubArgs(String[] args) {
        if (args.length <= 1) {
            return new String[0];
        }

        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        return subArgs;
    }

    public SpawnCommand getSpawnCommand() {
        return spawnCommand;
    }
}