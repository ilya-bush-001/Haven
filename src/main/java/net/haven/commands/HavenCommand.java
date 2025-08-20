package net.haven.commands;

import net.haven.Haven;
import net.haven.utils.MessageUtils;
import org.bukkit.command.CommandExecutor;

public class HavenCommand implements CommandExecutor {

    private final Haven plugin;
    private final MessageUtils messages;

    public HavenCommand(Haven plugin) {
        this.plugin = plugin;
        this.messages = plugin.getMessageUtils();
    }

}
