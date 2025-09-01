package net.haven.listeners;

import net.haven.commands.SpawnCommand;
import net.haven.gui.ControlGUIHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

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

        if (clickedItem.getType() == Material.ENDER_PEARL) {
            player.closeInventory();
            teleportToSpawn(player);
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

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof ControlGUIHolder) {
            event.setCancelled(true);
        }
    }

}
