package net.haven.commands.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HavenTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            boolean hasFullAccess = sender.hasPermission("haven.*");

            completions.add("spawn");

            if (hasFullAccess || sender.hasPermission("haven.command.reload")) {
                completions.add("reload");
            }
            if (hasFullAccess || sender.hasPermission("haven.command.delspawn")) {
                completions.add("delspawn");
            }
            if (hasFullAccess || sender.hasPermission("haven.command.setspawn")) {
                completions.add("setspawn");
            }
            if (hasFullAccess || sender.hasPermission("haven.command.help")) {
                completions.add("help");
            }

            return filterCompletions(completions, args[0]);

        }

        String subCommand = args[0].toLowerCase();

        boolean hasAccessToCommand = hasAccessToSubcommand(sender, subCommand);

        if (!hasAccessToCommand) {
            return new ArrayList<>();
        }

        switch (subCommand) {
            case "setspawn":
                return handleSetSpawnArgs(sender, args);
            case "delspawn":
                return handleDelSpawnArgs(sender, args);
            case "reload":
                return handleReloadArgs(sender, args);
            case "spawn":
                return handleSpawnArgs(sender, args);
            case "help":
                return handleHelpArgs(sender, args);
            default:
                return new ArrayList<>();
        }
    }

    private boolean hasAccessToSubcommand(CommandSender sender, String subCommand) {
        boolean hasFullAccess = sender.hasPermission("haven.*");

        switch (subCommand) {
            case "setspawn":
                return hasAccessToCommand(sender, args);
            case "delspawn":
                return hasAccessToCommand(sender, args);
            case "reload":
                return hasAccessToCommand(sender, args);
            case "spawn":
                return hasAccessToCommand(sender, args);
            case "help":
                return hasAccessToCommand(sender, args);
        }
    }
}
