package net.haven.listeners;

import net.haven.commands.SpawnCommand;
import net.haven.gui.ControlGUIHolder;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class MenuListener implements Listener {

    private static final Material TELEPORT_MATERIAL = Material.ENDER_PEARL;
    private static final Material RELOAD_MATERIAL = Material.REDSTONE;

    private static final double EXCELLENT_TPS = 18.0;
    private static final double GOOD_TPS = 15.0;

    private final SpawnCommand spawnCommand;
    private final BukkitAudiences audiences;
    private final MiniMessage mm;

    public MenuListener(SpawnCommand spawnCommand, BukkitAudiences audiences, MiniMessage mm) {
        this.spawnCommand = spawnCommand;
        this.audiences = spawnCommand.getPlugin().getAudiences();
        this.mm = mm;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isControlPanelInventory(event)) {
            return;
        }

        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ControlGUIHolder holder = (ControlGUIHolder) event.getInventory().getHolder();

        handleItemClick(player, clickedItem, holder, event.getSlot());
    }

    private boolean isControlPanelInventory(InventoryClickEvent event) {
        return event.getInventory().getHolder() instanceof ControlGUIHolder;
    }

    private void handleItemClick(Player player, ItemStack clickedItem, ControlGUIHolder holder, int slot) {
        Material itemType = clickedItem.getType();

        if (itemType == TELEPORT_MATERIAL && slot == holder.getTeleportSlot()) {
            handleTeleportClick(player);
        } else if (itemType == RELOAD_MATERIAL && slot == holder.getReloadSlot()) {
            handleReloadClick(player, holder);
        }
    }

    private void handleTeleportClick(Player player) {
        player.closeInventory();
        teleportToSpawn(player);
    }

    private void handleReloadClick(Player player, ControlGUIHolder holder) {
        if (player.hasPermission("haven.command.reload")) {
            holder.getReloadCommand().reload(player);
        } else {
            player.sendMessage(ChatColor.RED + "You don't have permission to reload the configuration!");
        }
    }

    private void teleportToSpawn(Player player) {
        Location spawnLocation = getSpawnLocation();

        if (spawnLocation == null) {
            sendSpawnNotSetMessage(player);
            return;
        }

        if (!player.hasPermission("haven.command.spawn")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to teleport to spawn!");
            return;
        }

        performSafeTeleport(player, spawnLocation);
    }

    private Location getSpawnLocation() {
        String worldName = spawnCommand.getPlugin().getConfig().getString("spawn.world");
        if (worldName == null) {
            return null;
        }

        return new Location(
                Bukkit.getWorld(worldName),
                spawnCommand.getPlugin().getConfig().getDouble("spawn.x"),
                spawnCommand.getPlugin().getConfig().getDouble("spawn.y"),
                spawnCommand.getPlugin().getConfig().getDouble("spawn.z"),
                (float) spawnCommand.getPlugin().getConfig().getDouble("spawn.yaw"),
                (float) spawnCommand.getPlugin().getConfig().getDouble("spawn.pitch")
        );
    }

    private void sendSpawnNotSetMessage(Player player) {
        String rawMessage = spawnCommand.getPlugin().getMessage("messages.spawn.not-set", "<color:#F2000E>[✘] Spawn point not set!");
        Component parsed = mm.deserialize(rawMessage);
        audiences.sender(player).sendMessage(parsed);
    }

    private void performSafeTeleport(Player player, Location spawnLocation) {
        try {
            player.teleport(spawnLocation);
            String rawMessage = spawnCommand.getPlugin().getMessage("messages.spawn.success",
                    "<color:#38F200>Teleported to spawn!");
            Component parsed = mm.deserialize(rawMessage);
            audiences.sender(player).sendMessage(parsed);
        } catch (Exception e) {
            String rawMessage = spawnCommand.getPlugin().getMessage("messages.error",
                    "<color:#F2000E>Failed to teleport to spawn!");
            Component parsed = mm.deserialize(rawMessage);
            audiences.sender(player).sendMessage(parsed);

            spawnCommand.getPlugin().getLogger().warning("Teleport failed for " + player.getName() + ": " + e.getMessage());
        }
    }

    public List<String> getServerStatsLore() {
        List<String> lore = new ArrayList<>();

        addTpsInfo(lore);
        addMemoryInfo(lore);
        addPlayerInfo(lore);
        addUptimeInfo(lore);

        return lore;
    }

    private void addTpsInfo(List<String> lore) {
        double[] tps = Bukkit.getTPS();
        lore.add(ChatColor.GOLD + "TPS: " + getFormattedTPS(tps));
    }

    private void addMemoryInfo(List<String> lore) {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long allocatedMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = allocatedMemory - freeMemory;
        int usagePercentage = (int) ((usedMemory * 100) / maxMemory);

        lore.add(ChatColor.GREEN + "RAM: " + ChatColor.WHITE + usedMemory + "MB/" + maxMemory + "MB");
        lore.add(ChatColor.GREEN + "Used: " + ChatColor.WHITE + usagePercentage + "%");
    }

    private void addPlayerInfo(List<String> lore) {
        lore.add(ChatColor.BLUE + "Players: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size());
    }

    private void addUptimeInfo(List<String> lore) {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;
        lore.add(ChatColor.AQUA + "Uptime: " + ChatColor.WHITE + formatUptime(uptime));
    }

    public String getFormattedTPS(double[] tps) {
        StringBuilder tpsString = new StringBuilder();
        for (int i = 0; i < tps.length; i++) {
            double currentTps = Math.min(tps[i], 20.0);
            ChatColor color = getTPSColor(currentTps);
            tpsString.append(color).append(String.format("%.2f", currentTps));
            if (i < tps.length - 1) {
                tpsString.append(ChatColor.GRAY).append(", ");
            }
        }
        return tpsString.toString();
    }

    public ChatColor getTPSColor(double tps) {
        if (tps >= EXCELLENT_TPS) return ChatColor.GREEN;
        if (tps >= GOOD_TPS) return ChatColor.YELLOW;
        return ChatColor.RED;
    }

    public String formatUptime(long seconds) {
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;

        if (days > 0) {
            return days + "d " + hours + "h " + minutes + "m";
        } else if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }

    public void updateStatsItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setLore(getServerStatsLore());
            item.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof ControlGUIHolder) {
            event.setCancelled(true);
        }
    }

    public SpawnCommand getSpawnCommand() {
        return spawnCommand;
    }
}