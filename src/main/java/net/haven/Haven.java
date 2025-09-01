package net.haven;

import net.haven.commands.SpawnCommand;
import net.haven.config.ConfigLocalization;
import net.haven.commands.handlers.CommandHandler;
import net.haven.completers.HavenTabCompleter;
import net.haven.listeners.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

public final class Haven extends JavaPlugin {
    private ConfigLocalization localization;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();

        localization = new ConfigLocalization(this);
        localization.loadLanguages();

        this.commandHandler = new CommandHandler(this);

        Objects.requireNonNull(this.getCommand("hv")).setExecutor(new CommandHandler(this));
        Objects.requireNonNull(this.getCommand("hv")).setTabCompleter(new HavenTabCompleter());
        Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new SpawnCommand(this));

        getServer().getPluginManager().registerEvents(new MenuListener(commandHandler.getSpawnCommand()), this);

        this.getCommand("hv").setExecutor(commandHandler);
        this.getCommand("hv").setTabCompleter(new HavenTabCompleter());

        getLogger().info("Haven enabled!");
    }

    public String getMessage(String path, Object... replacements) {
        String message = localization.getMessage(path);

        for (int i = 0; i < replacements.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(replacements[i]));
        }

        return message;
    }

    public ConfigLocalization getLocalization() {
        return localization;
    }

    public List<String> getMessageList(String path) {
        return localization.getMessageList(path);
    }

}