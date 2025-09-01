package net.haven.listeners;

import net.haven.gui.ControlGUIHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MenuListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof ControlGUIHolder)) {
            return;
        }

        event.setCancelled(true);

        if (event.getCurrentItem() != null) {
            //events
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof ControlGUIHolder) {
            event.setCancelled(true);
        }
    }

}
