package net.haven.commands;

import net.haven.Haven;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements CommandExecutor {

    private final Haven plugin;
    private static final String PERMISSION = "haven.command.help";
    private static final List<String> DEFAULT_HELP = createDefaultHelp();

    public HelpCommand(Haven plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission(PERMISSION)) {
                sender.sendMessage(plugin.getMessage("messages.no-permissions",
                        "&cYou don't have permission to execute this command!"));
                return true;
            }
        }

        showHelp(sender);
        return true;
    }

    public void showHelp(CommandSender sender) {
        List<String> helpLines = getHelpLines();

        for (String line : helpLines) {
            sender.sendMessage(line);
        }
    }

    private List<String> getHelpLines() {
        List<String> configuredHelp = plugin.getMessageList("messages.help");

        if (configuredHelp == null || configuredHelp.isEmpty()) {
            return DEFAULT_HELP;
        }

        return configuredHelp;
    }

    private static List<String> createDefaultHelp() {
        List<String> help = new ArrayList<>();

        help.add(ChatColor.GOLD + "=== " + ChatColor.GREEN + "Haven Commands" + ChatColor.GOLD + " ===");
        help.add(ChatColor.YELLOW + "/hv setspawn" + ChatColor.GRAY + " - Set spawnpoint");
        help.add(ChatColor.YELLOW + "/hv delspawn" + ChatColor.GRAY + " - Delete spawnpoint");
        help.add(ChatColor.YELLOW + "/hv reload" + ChatColor.GRAY + " - Reload configuration");
        help.add(ChatColor.YELLOW + "/hv help" + ChatColor.GRAY + " - Show this help");
        help.add(ChatColor.YELLOW + "/hv control" + ChatColor.GRAY + " - Open control panel");
        help.add(ChatColor.YELLOW + "/spawn" + ChatColor.GRAY + " - Teleport to spawn");
        help.add(ChatColor.GOLD + "==========================");

        return help;
    }
}