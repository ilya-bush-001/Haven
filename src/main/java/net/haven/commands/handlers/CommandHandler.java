package net.haven.commands.handlers;

import net.haven.Haven;
import net.haven.commands.*;
import net.haven.commands.config.ConfigLocalization;
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

    public CommandHandler(Haven plugin) {
        this.plugin = plugin;
        this.setSpawnCommand = new SetSpawnCommand(plugin);
        this.deleteSpawnCommand = new DeleteSpawnCommand(plugin);
        this.reloadCommand = new ReloadCommand(plugin);
        this.helpCommand = new HelpCommand(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 0) {
            helpCommand.showHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "setspawn":
                return setSpawnCommand.onCommand(sender, cmd, label, getSubArgs(args));
            case "delspawn":
                return deleteSpawnCommand.onCommand(sender, cmd, label, getSubArgs(args));
            case "reload":
                return reloadCommand.onCommand(sender, cmd, label, getSubArgs(args));
            case "help":
                helpCommand.showHelp(sender);
                return true;
            default:
                sender.sendMessage(plugin.getMessage("messages.unknown-command"));
                return true;
        }
    }

    private String[] getSubArgs(String[] args) {
        if (args.length <= 1) {
            return new String[0];
        }
        String[] subArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subArgs, 0, subArgs.length);
        return subArgs;
    }
}
