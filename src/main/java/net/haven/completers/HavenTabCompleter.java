package net.haven.completers;

import net.haven.Haven;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
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

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(getMainCommands(sender));
            return filterCompletions(completions, args[0]);
        }

        String subCommand = args[0].toLowerCase();

        if (!hasAccessToSubcommand(sender, subCommand)) {
            return new ArrayList<>();
        }

        switch (subCommand) {
            case "setspawn":
                return handleSetSpawnArgs(sender, args);
            case "delspawn":
                return handleDelSpawnArgs(sender, args);
            case "reload":
                return handleReloadArgs(sender, args);
            case "help":
                return handleHelpArgs(sender, args);
            default:
                return new ArrayList<>();
        }
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

        return commands;
    }

    private boolean hasAccessToSubcommand(CommandSender sender, String subCommand) {
        boolean hasFullAccess = sender.hasPermission("haven.*");

        switch (subCommand) {
            case "setspawn":
                return hasFullAccess || sender.hasPermission("haven.command.setspawn");
            case "delspawn":
                return hasFullAccess || sender.hasPermission("haven.command.delspawn");
            case "reload":
                return hasFullAccess || sender.hasPermission("haven.command.reload");
            case "help":
                return hasFullAccess || sender.hasPermission("haven.command.help");
            default:
                return false;
        }
    }

    private List<String> handleSetSpawnArgs(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 2) {
            completions.add("[name]");
            completions.add("--world");
            completions.add("--public");
            completions.add("--private");
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("--world")) {
                return Bukkit.getWorlds().stream()
                        .map(world -> world.getName())
                        .filter(name -> name.toLowerCase().startsWith(args[2].toLowerCase()))
                        .collect(Collectors.toList());
            } else if (args[1].equalsIgnoreCase("--public") || args[1].equalsIgnoreCase("--private")) {
                completions.add("true");
                completions.add("false");
            }
        }

        return filterCompletions(completions, args[args.length - 1]);
    }

    private List<String> handleDelSpawnArgs(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 2) {
            completions.add("[spawn-name]");
            completions.add("--all");
            completions.add("--world");
        } else if (args.length == 3 && args[1].equalsIgnoreCase("--world")) {
            return Bukkit.getWorlds().stream()
                    .map(world -> world.getName())
                    .filter(name -> name.toLowerCase().startsWith(args[2].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return filterCompletions(completions, args[args.length - 1]);
    }

    private List<String> handleReloadArgs(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 2) {
            completions.add("config");
            completions.add("messages");
            completions.add("all");
        }

        return filterCompletions(completions, args[1]);
    }

    private List<String> handleSpawnArgs(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 2) {
            completions.add("[spawn-name]");
            completions.add("list");
            completions.add("info");
        } else if (args.length == 3 && args[1].equalsIgnoreCase("info")) {
            boolean hasFullAccess = sender.hasPermission("haven.*");
            if (hasFullAccess || sender.hasPermission("haven.spawn.info.others")) {
                return getOnlinePlayerNames(args[2]);
            }
        }

        return filterCompletions(completions, args[args.length - 1]);
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

    private boolean hasPermissionForHelp(CommandSender sender, String command) {
        switch (command) {
            case "setspawn":
                return sender.hasPermission("haven.command.setspawn");
            case "delspawn":
                return sender.hasPermission("haven.command.delspawn");
            case "reload":
                return sender.hasPermission("haven.command.reload");
            default:
                return false;
        }
    }

    private List<String> getOnlinePlayerNames(String input) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
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