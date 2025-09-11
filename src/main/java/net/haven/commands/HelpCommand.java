package net.haven.commands;

import net.haven.Haven;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand implements CommandExecutor {

    private final Haven plugin;
    private static final String PERMISSION = "haven.command.help";
    private static final List<Component> DEFAULT_HELP = createDefaultHelp();
    private final BukkitAudiences audiences;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public HelpCommand(Haven plugin, BukkitAudiences audiences) {
        this.plugin = plugin;
        this.audiences = audiences;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission(PERMISSION)) {
                String message = plugin.getMessage("messages.no-permissions", "<color:#38F200>[✘] You don't have permission to execute this command!");
                Component parsed = mm.deserialize(message);
                audiences.sender(sender).sendMessage(parsed);
                return true;
            }
        }

        showHelp(sender);
        return true;
    }

    public void showHelp(CommandSender sender) {
        List<Component> helpLines = getHelpLines();

        for (Component line : helpLines) {
            audiences.sender(sender).sendMessage(line);
        }
    }

    private List<Component> getHelpLines() {
        List<String> configuredHelp = plugin.getMessageList("messages.help");

        if (configuredHelp == null || configuredHelp.isEmpty()) {
            return createDefaultHelp();
        }

        return configuredHelp.stream()
                .map(mm::deserialize)
                .collect(Collectors.toList());
    }

    private static List<Component> createDefaultHelp() {
        MiniMessage mm = MiniMessage.miniMessage();

        List<Component> help = new ArrayList<>();

        help.add(mm.deserialize("<gold>=== <green>Haven Commands<gold> ==="));
        help.add(mm.deserialize("<yellow>/hv setspawn<gray> - Set spawnpoint"));
        help.add(mm.deserialize("<yellow>/hv delspawn<gray> - Delete spawnpoint"));
        help.add(mm.deserialize("<yellow>/hv reload<gray> - Reload configuration"));
        help.add(mm.deserialize("<yellow>/hv help<gray> - Show this help"));
        help.add(mm.deserialize("<yellow>/hv control<gray> - Open control panel"));
        help.add(mm.deserialize("<yellow>/spawn<gray> - Teleport to spawn"));
        help.add(mm.deserialize("<gold>=========================="));

        return help;
    }
}