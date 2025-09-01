package net.haven.gui;

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

    public ControlGUIHolder() {
        this.inventory = Bukkit.createInventory(this, 27, "Control Panel");
        setupInventory();
    }

    private void setupInventory() {
        ItemStack panelGlass = createPanelGlass();

        for (int slot : Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26)) {
            inventory.setItem(slot, panelGlass);
        }

        inventory.setItem(11, createBookItem());
        // inventory.setItem(13, createTestItem1);
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

    private ItemStack createBookItem() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Test Info");
            meta.setLore(Arrays.asList(
                    "This is first lore line in Test Info",
                    "This is second lore line in Test Info"
            ));
            book.setItemMeta(meta);
        }
        return book;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public void setItem(int slot, ItemStack item) {
        if (slot == 13 || slot == 15) {
            inventory.setItem(slot, item);
        }
    }

    public ItemStack getItem(int slot) {
        return inventory.getItem(slot);
    }
}
