package net.haven.listeners;

import net.haven.commands.ControlCommand;
import net.haven.commands.SpawnCommand;
import net.haven.gui.ControlGUIHolder;
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

    private final SpawnCommand spawnCommand;

    public MenuListener(SpawnCommand spawnCommand) {
        this.spawnCommand = spawnCommand;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof ControlGUIHolder)) {
            return;
        }

        event.setCancelled(true);

        if (event.getCurrentItem() == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        ControlGUIHolder holder = (ControlGUIHolder) event.getInventory().getHolder();

        if (clickedItem.getType() == Material.ENDER_PEARL) {
            player.closeInventory();
            teleportToSpawn(player);
        } else if (event.getSlot() == 15 && clickedItem.getType() == Material.REDSTONE) {
            holder.getReloadCommand().reload(player);
        }
    }

    private void teleportToSpawn(Player player) {
        String worldName = spawnCommand.getPlugin().getConfig().getString("spawn.world");
        if (worldName == null) {
            player.sendMessage(spawnCommand.getPlugin().getMessage("messages.spawn.not-set", "&cSpawn not set!"));
            return;
        }

        Location spawnLocation = new Location(
                Bukkit.getWorld(worldName),
                spawnCommand.getPlugin().getConfig().getDouble("spawn.x"),
                spawnCommand.getPlugin().getConfig().getDouble("spawn.y"),
                spawnCommand.getPlugin().getConfig().getDouble("spawn.z"),
                (float) spawnCommand.getPlugin().getConfig().getDouble("spawn.yaw"),
                (float) spawnCommand.getPlugin().getConfig().getDouble("spawn.pitch")
        );

        player.teleport(spawnLocation);
        player.sendMessage(spawnCommand.getPlugin().getMessage("messages.spawn.success", "&aTeleported to spawn!"));
    }

    public List<String> getServerStatsLore() {
        List<String> lore = new ArrayList<>();

        double[] tps = Bukkit.getTPS();
        lore.add(ChatColor.GOLD + "TPS: " + getFormattedTPS(tps));

        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long allocatedMemory = runtime.totalMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long usedMemory = allocatedMemory - freeMemory;

        lore.add(ChatColor.GREEN + "RAM: " + ChatColor.WHITE + usedMemory + "MB/" + maxMemory + "MB");
        lore.add(ChatColor.GREEN + "Used: " + ChatColor.WHITE + (usedMemory * 100 / maxMemory) + "%");

        lore.add(ChatColor.BLUE + "Players online: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size());

        long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000;
        lore.add(ChatColor.AQUA + "Uptime: " + ChatColor.WHITE + formatUptime(uptime));

        return lore;
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
        if (tps >= 18.0) return ChatColor.GREEN;
        if (tps >= 15.0) return ChatColor.YELLOW;
        return ChatColor.RED;
    }

    public String formatUptime(long seconds) {
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;

        if (days > 0) {
            return days + "d " + hours + "h " + minutes + "min";
        } else if (hours > 0) {
            return hours + "h " + minutes + "min";
        } else {
            return minutes + "min";
        }
    }

    public void updateStatsItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        meta.setLore(getServerStatsLore());
        item.setItemMeta(meta);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof ControlGUIHolder) {
            event.setCancelled(true);
        }
    }

}