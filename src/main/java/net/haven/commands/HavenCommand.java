package net.haven.commands;

import net.haven.Haven;
import net.haven.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
    }

}
