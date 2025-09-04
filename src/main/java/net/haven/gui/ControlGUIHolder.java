package net.haven.gui;

import net.haven.commands.ReloadCommand;
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
    private final MenuListener menuListener;
    private final ReloadCommand reloadCommand;

    public ControlGUIHolder(MenuListener listener, ReloadCommand reloadCommand) {
        this.menuListener = listener;
        this.reloadCommand = reloadCommand;
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
        inventory.setItem(15, createReloadItem());
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
            meta.setLore(menuListener.getServerStatsLore());
            book.setItemMeta(meta);
        }
        return book;
    }

    private ItemStack createReloadItem() {
        ItemStack reload = new ItemStack(Material.REDSTONE);
        ItemMeta meta = reload.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "Reload the plugin");
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Click to reload plugin configuration"
            ));
            reload.setItemMeta(meta);
        }
        return reload;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public void updateStatsItems() {
        ItemStack statsItem = inventory.getItem(13);
        if (statsItem != null && statsItem.getType() == Material.BOOK) {
            menuListener.updateStatsItem(statsItem);
        }
    }

    public ReloadCommand getReloadCommand() {
        return reloadCommand;
    }
}