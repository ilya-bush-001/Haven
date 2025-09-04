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
import java.util.List;

public class ControlGUIHolder implements InventoryHolder {

    private static final int INVENTORY_SIZE = 27;
    private static final String INVENTORY_TITLE = "Control Panel";

    private static final int TELEPORT_SLOT = 11;
    private static final int STATS_SLOT = 13;
    private static final int RELOAD_SLOT = 15;

    private static final List<Integer> GLASS_PANEL_SLOTS = Arrays.asList(
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26
    );

    private final Inventory inventory;
    private final MenuListener menuListener;
    private final ReloadCommand reloadCommand;

    public ControlGUIHolder(MenuListener menuListener, ReloadCommand reloadCommand) {
        this.menuListener = menuListener;
        this.reloadCommand = reloadCommand;
        this.inventory = createInventory();
        setupInventory();
    }

    private Inventory createInventory() {
        return Bukkit.createInventory(this, INVENTORY_SIZE, INVENTORY_TITLE);
    }

    private void setupInventory() {
        setupDecorationPanels();
        setupFunctionalItems();
    }

    private void setupDecorationPanels() {
        ItemStack panelGlass = createPanelGlass();
        for (int slot : GLASS_PANEL_SLOTS) {
            inventory.setItem(slot, panelGlass);
        }
    }

    private void setupFunctionalItems() {
        inventory.setItem(TELEPORT_SLOT, createTeleportItem());
        inventory.setItem(STATS_SLOT, createStatsItem());
        inventory.setItem(RELOAD_SLOT, createReloadItem());
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

    private ItemStack createTeleportItem() {
        ItemStack enderPearl = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = enderPearl.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "Teleport to Spawn");
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Click to teleport to the spawn point",
                    ChatColor.DARK_GRAY + "Requires: haven.command.spawn"
            ));
            enderPearl.setItemMeta(meta);
        }

        return enderPearl;
    }

    private ItemStack createStatsItem() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Server Statistics");
            meta.setLore(menuListener.getServerStatsLore());
            book.setItemMeta(meta);
        }

        return book;
    }

    private ItemStack createReloadItem() {
        ItemStack reloadItem = new ItemStack(Material.REDSTONE);
        ItemMeta meta = reloadItem.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GREEN + "Reload Configuration");
            meta.setLore(Arrays.asList(
                    ChatColor.GRAY + "Click to reload plugin configuration",
                    ChatColor.DARK_GRAY + "Requires: haven.command.reload"
            ));
            reloadItem.setItemMeta(meta);
        }

        return reloadItem;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public void updateStatsItems() {
        ItemStack statsItem = inventory.getItem(STATS_SLOT);
        if (isValidStatsItem(statsItem)) {
            menuListener.updateStatsItem(statsItem);
        }
    }

    private boolean isValidStatsItem(ItemStack item) {
        return item != null && item.getType() == Material.BOOK && item.hasItemMeta();
    }

    public ReloadCommand getReloadCommand() {
        return reloadCommand;
    }

    public int getTeleportSlot() {
        return TELEPORT_SLOT;
    }

    public int getStatsSlot() {
        return STATS_SLOT;
    }

    public int getReloadSlot() {
        return RELOAD_SLOT;
    }
}