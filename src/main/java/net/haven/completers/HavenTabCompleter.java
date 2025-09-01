package net.haven.completers;

import net.haven.Haven;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HavenTabCompleter implements TabCompleter {

    public HavenTabCompleter(Haven haven) {
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 0 || args[0].isEmpty()) {
            return getMainCommands(sender);
        }

        if (args.length == 1) {
            List<String> completions = new ArrayList<>(getMainCommands(sender));
            return filterCompletions(completions, args[0]);
        }

        String subCommand = args[0].toLowerCase();

        if (!hasAccessToSubcommand(sender, subCommand)) {
            return new ArrayList<>();
        }

        return switch (subCommand) {
            case "setspawn" -> handleSetSpawnArgs(args);
            case "delspawn" -> handleDelSpawnArgs(args);
            case "reload" -> handleReloadArgs(args);
            case "help" -> handleHelpArgs(sender, args);
            case "control" -> handleControlArgs();
            default -> new ArrayList<>();
        };
    }

    private List<String> getMainCommands(CommandSender sender) {
        List<String> commands = new ArrayList<>();
        boolean hasFullAccess = sender.hasPermission("haven.*");

        if (hasFullAccess || sender.hasPermission("haven.reload")) {
            commands.add("reload");
        }
        if (hasFullAccess || sender.hasPermission("haven.command.delspawn")) {
            commands.add("delspawn");
        }
        if (hasFullAccess || sender.hasPermission("haven.command.setspawn")) {
            commands.add("setspawn");
        }
        if (hasFullAccess || sender.hasPermission("haven.command.help")) {
            commands.add("help");
        }
        if (hasFullAccess || sender.hasPermission("haven.command.control")) {
            commands.add("control");
        }

        return commands;
    }

    private boolean hasAccessToSubcommand(CommandSender sender, String subCommand) {
        boolean hasFullAccess = sender.hasPermission("haven.*");

        return switch (subCommand) {
            case "setspawn" -> hasFullAccess || sender.hasPermission("haven.command.setspawn");
            case "delspawn" -> hasFullAccess || sender.hasPermission("haven.command.delspawn");
            case "reload" -> hasFullAccess || sender.hasPermission("haven.command.reload");
            case "help" -> hasFullAccess || sender.hasPermission("haven.command.help");
            case "control" -> hasFullAccess || sender.hasPermission("haven.command.control");

            default -> false;
        };
    }

    private List<String> handleSetSpawnArgs(String[] args) {
        List<String> completions = new ArrayList<>();

        return filterCompletions(completions, args[args.length - 1]);
    }

    private List<String> handleDelSpawnArgs(String[] args) {
        List<String> completions = new ArrayList<>();

        return filterCompletions(completions, args[args.length - 1]);
    }

    private List<String> handleReloadArgs(String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 2) {
            completions.add("config");
            completions.add("messages");
            completions.add("all");
        }

        return filterCompletions(completions, args[1]);
    }

    private List<String> handleHelpArgs(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 2) {
            boolean hasFullAccess = sender.hasPermission("haven.*");

            completions.add("setspawn");
            completions.add("delspawn");
            completions.add("reload");

            return completions.stream()
                    .filter(cmd -> hasFullAccess || hasPermissionForHelp(sender, cmd))
                    .filter(cmd -> cmd.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    private @NotNull List<String> handleControlArgs() {
        return List.of();
    }

    private boolean hasPermissionForHelp(CommandSender sender, String command) {
        return switch (command) {
            case "setspawn" -> sender.hasPermission("haven.command.setspawn");
            case "delspawn" -> sender.hasPermission("haven.command.delspawn");
            case "reload" -> sender.hasPermission("haven.command.reload");
            default -> false;
        };
    }

    private List<String> filterCompletions(List<String> completions, String input) {
        if (completions.isEmpty() || input.isEmpty()) {
            return completions;
        }

        return completions.stream()
                .filter(comp -> comp.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }
}