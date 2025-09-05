package net.haven.commands.handlers;

import net.haven.Haven;
import net.haven.commands.*;
import net.haven.listeners.MenuListener;
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

    public CommandHandler(Haven plugin, MenuListener menuListener) {
        this.plugin = plugin;
        this.menuListener = menuListener;

        this.setSpawnCommand = new SetSpawnCommand(plugin);
        this.deleteSpawnCommand = new DeleteSpawnCommand(plugin);
        this.reloadCommand = new ReloadCommand(plugin);
        this.helpCommand = new HelpCommand(plugin);
        this.spawnCommand = new SpawnCommand(plugin);
        this.controlCommand = new ControlCommand(plugin, menuListener, reloadCommand);
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
                sender.sendMessage(plugin.getMessage("messages.unknown-command"));
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