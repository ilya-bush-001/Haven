package net.haven.gui;

import net.haven.listeners.MenuListener;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ControlGUIHolder implements InventoryHolder {

    private final Inventory inventory;
    private final MenuListener listener;

    public ControlGUIHolder(MenuListener listener) {
        this.listener = listener;
        this.inventory = Bukkit.createInventory(this, 27, "Control Panel");
        setupInventory();
    }

    private void setupInventory() {
        ItemStack panelGlass = createPanelGlass();

        for (int slot : Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 12, 14, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26)) {
            inventory.setItem(slot, panelGlass);
        }

        inventory.setItem(11, createEnderPearlItem());
        inventory.setItem(13, createBookItem());
        // inventory.setItem(15, createTestItem2);
    }

    private ItemStack createPanelGlass() {
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = glass.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            glass.setItemMeta(meta);
        }
        return glass;
    }

    private ItemStack createEnderPearlItem() {
        ItemStack enderPearl = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = enderPearl.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "Teleport to spawn");
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Teleport you to spawn."
            ));
            enderPearl.setItemMeta(meta);
        }
        return enderPearl;
    }

    private ItemStack createBookItem() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Statistics");
            meta.setLore(listener.getServerStatsLore());
            book.setItemMeta(meta);
        }
        return book;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public void updateStatsItems() {
        ItemStack statsItem = inventory.getItem(13);
        if (statsItem != null && statsItem.getType() == Material.BOOK) {
            listener.updateStatsItem(statsItem);
        }
    }
}