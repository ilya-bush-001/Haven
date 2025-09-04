package net.haven.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HavenTabCompleter implements TabCompleter {

    private static final String PERMISSION_PREFIX = "haven.command.";
    private static final String WILDCARD_PERMISSION = "haven.*";

    public HavenTabCompleter() {
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (args.length == 0 || args[0].isEmpty()) {
            return getAvailableCommands(sender);
        }

        if (args.length == 1) {
            return getFilteredCommandSuggestions(sender, args[0]);
        }

        return getSubCommandSuggestions(sender, args);
    }

    private List<String> getAvailableCommands(CommandSender sender) {
        List<String> commands = new ArrayList<>();
        boolean hasFullAccess = sender.hasPermission(WILDCARD_PERMISSION);

        if (hasFullAccess || sender.hasPermission(PERMISSION_PREFIX + "reload")) {
            commands.add("reload");
        }
        if (hasFullAccess || sender.hasPermission(PERMISSION_PREFIX + "delspawn")) {
            commands.add("delspawn");
        }
        if (hasFullAccess || sender.hasPermission(PERMISSION_PREFIX + "setspawn")) {
            commands.add("setspawn");
        }
        if (hasFullAccess || sender.hasPermission(PERMISSION_PREFIX + "help")) {
            commands.add("help");
        }
        if (hasFullAccess || sender.hasPermission(PERMISSION_PREFIX + "control")) {
            commands.add("control");
        }

        return commands;
    }

    private List<String> getFilteredCommandSuggestions(CommandSender sender, String input) {
        List<String> availableCommands = getAvailableCommands(sender);
        return filterCompletions(availableCommands, input);
    }

    private List<String> getSubCommandSuggestions(CommandSender sender, String[] args) {
        String subCommand = args[0].toLowerCase();

        if (!hasSubCommandAccess(sender, subCommand)) {
            return new ArrayList<>();
        }

        return switch (subCommand) {
            case "setspawn" -> handleSetSpawnArgs(args);
            case "delspawn" -> handleDelSpawnArgs(args);
            case "reload" -> handleReloadArgs(args);
            case "help" -> handleHelpArgs(sender, args);
            case "control" -> handleControlArgs(args);
            default -> new ArrayList<>();
        };
    }

    private boolean hasSubCommandAccess(CommandSender sender, String subCommand) {
        boolean hasFullAccess = sender.hasPermission(WILDCARD_PERMISSION);

        return switch (subCommand) {
            case "setspawn" -> hasFullAccess || sender.hasPermission(PERMISSION_PREFIX + "setspawn");
            case "delspawn" -> hasFullAccess || sender.hasPermission(PERMISSION_PREFIX + "delspawn");
            case "reload" -> hasFullAccess || sender.hasPermission(PERMISSION_PREFIX + "reload");
            case "help" -> hasFullAccess || sender.hasPermission(PERMISSION_PREFIX + "help");
            case "control" -> hasFullAccess || sender.hasPermission(PERMISSION_PREFIX + "control");
            default -> false;
        };
    }

    private List<String> handleSetSpawnArgs(String[] args) {
        // setspawn doesn't typically have additional arguments
        return new ArrayList<>();
    }

    private List<String> handleDelSpawnArgs(String[] args) {
        // delspawn doesn't typically have additional arguments
        return new ArrayList<>();
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
            boolean hasFullAccess = sender.hasPermission(WILDCARD_PERMISSION);

            if (hasFullAccess || hasHelpPermission(sender, "setspawn")) {
                completions.add("setspawn");
            }
            if (hasFullAccess || hasHelpPermission(sender, "delspawn")) {
                completions.add("delspawn");
            }
            if (hasFullAccess || hasHelpPermission(sender, "reload")) {
                completions.add("reload");
            }
            if (hasFullAccess || hasHelpPermission(sender, "control")) {
                completions.add("control");
            }

            return filterCompletions(completions, args[1]);
        }

        return new ArrayList<>();
    }

    private boolean hasHelpPermission(CommandSender sender, String command) {
        return sender.hasPermission(PERMISSION_PREFIX + command);
    }

    private List<String> handleControlArgs(String[] args) {
        return new ArrayList<>();
    }

    private List<String> filterCompletions(List<String> completions, String input) {
        if (completions.isEmpty() || input.isEmpty()) {
            return completions;
        }

        String lowerInput = input.toLowerCase();
        return completions.stream()
                .filter(comp -> comp.toLowerCase().startsWith(lowerInput))
                .collect(Collectors.toList());
    }
}