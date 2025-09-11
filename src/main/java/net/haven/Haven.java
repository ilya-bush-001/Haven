package net.haven;

import net.haven.commands.SpawnCommand;
import net.haven.config.ConfigLocalization;
import net.haven.commands.handlers.CommandHandler;
import net.haven.completers.HavenTabCompleter;
import net.haven.gui.ControlGUIHolder;
import net.haven.listeners.MenuListener;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;
import java.util.logging.Level;

import org.jetbrains.annotations.NotNull;

public final class Haven extends JavaPlugin {

    private BukkitAudiences audiences;

    private static final String PLUGIN_NAME = "Haven";
    private static final String MAIN_COMMAND = "hv";
    private static final String SPAWN_COMMAND = "spawn";

    private static final long STATS_UPDATE_DELAY = 20L;
    private static final long STATS_UPDATE_PERIOD = 20L;

    private ConfigLocalization localization;
    private CommandHandler commandHandler;
    private MenuListener menuListener;
    private BukkitTask statsUpdateTask;

    @Override
    public void onEnable() {

        this.audiences = BukkitAudiences.create(this);

        try {
            initializePlugin();
            registerCommands();
            registerListeners();
            startBackgroundTasks();

            getLogger().info(PLUGIN_NAME + " has been enabled successfully!");

        } catch (Exception e) {
            handleEnableError(e);
        }
    }

    @Override
    public void onDisable() {
        try {
            stopBackgroundTasks();
            getLogger().info(PLUGIN_NAME + " has been disabled.");

        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Error during plugin disable: ", e);
        }
    }

    private void initializePlugin() {
        saveDefaultConfig();
        reloadConfig();

        localization = new ConfigLocalization(this);
        localization.loadLanguages();

        SpawnCommand spawnCommand = new SpawnCommand(this, audiences);
        this.menuListener = new MenuListener(spawnCommand);
        this.commandHandler = new CommandHandler(this, menuListener, audiences);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand(MAIN_COMMAND)).setExecutor(commandHandler);
        Objects.requireNonNull(getCommand(MAIN_COMMAND)).setTabCompleter(new HavenTabCompleter());

        Objects.requireNonNull(getCommand(SPAWN_COMMAND)).setExecutor(new SpawnCommand(this, audiences));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(menuListener, this);
    }

    private void startBackgroundTasks() {
        startStatsUpdateTask();
    }

    private void startStatsUpdateTask() {
        statsUpdateTask = Bukkit.getScheduler().runTaskTimer(this, this::updateControlPanelStats,
                STATS_UPDATE_DELAY, STATS_UPDATE_PERIOD);

        getLogger().info("Started control panel stats update task");
    }

    private void updateControlPanelStats() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isViewingControlPanel(player)) {
                updatePlayerControlPanel(player);
            }
        }
    }

    private boolean isViewingControlPanel(Player player) {
        return player.getOpenInventory().getTopInventory().getHolder() instanceof ControlGUIHolder;
    }

    private void updatePlayerControlPanel(Player player) {
        try {
            ControlGUIHolder holder = (ControlGUIHolder) player.getOpenInventory().getTopInventory().getHolder();
            holder.updateStatsItems();
        } catch (Exception e) {
            getLogger().warning("Failed to update control panel for player: " + player.getName());
            getLogger().warning("Error: " + e.getMessage());
        }
    }

    private void stopBackgroundTasks() {
        if (statsUpdateTask != null) {
            statsUpdateTask.cancel();
            statsUpdateTask = null;
            getLogger().info("Stopped control panel stats update task");
        }
    }

    private void handleEnableError(Exception e) {
        getLogger().log(Level.SEVERE, "Failed to enable " + PLUGIN_NAME + "!", e);
        getLogger().severe("The plugin will not function properly.");

        getServer().getPluginManager().disablePlugin(this);
    }

    public @NotNull String getMessage(String path, Object... replacements) {
        String message = localization.getMessage(path);

        if (replacements != null && replacements.length > 0) {
            for (int i = 0; i < replacements.length; i++) {
                String replacement = String.valueOf(replacements[i]);
                message = message.replace("{" + i + "}", replacement);
            }
        }

        return message;
    }

    public ConfigLocalization getLocalization() {
        return localization;
    }

    public java.util.List<String> getMessageList(String path) {
        return localization.getMessageList(path);
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public MenuListener getMenuListener() {
        return menuListener;
    }

    public BukkitTask getStatsUpdateTask() {
        return statsUpdateTask;
    }

    public boolean reloadPlugin() {
        try {
            reloadConfig();
            localization.reload();
            getLogger().info("Plugin configuration reloaded successfully");
            return true;

        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to reload plugin configuration: ", e);
            return false;
        }
    }
}