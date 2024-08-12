package fr.as2treffle.inventorybuilderapi.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Action {

    private final Player player;
    private final Inventory inventory;
    private final ClickType click;
    private final ItemStack itemStack;
    private final String action;
    private final String args;
    private final YamlConfiguration file;
    private final ItemStack cursor;
    private final Integer slot;

    public Action(Player player, Inventory inventory, ClickType click, ItemStack itemStack, String action, String args, YamlConfiguration file, ItemStack cursor, Integer slot) {
        this.player = player;
        this.inventory = inventory;
        this.click = click;
        this.itemStack = itemStack;
        this.action = action;
        this.args = args;
        this.file = file;
        this.cursor = cursor;
        this.slot = slot;
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public YamlConfiguration getInventoryFile() {
        return file;
    }

    public ClickType getClick() {
        return click;
    }

    public Integer getSlot() {
        return slot;
    }

    public String getAction() {
        return action;
    }

    public ItemStack getCursor() {
        return cursor;
    }

    public String getArgs() {
        return args;
    }
}
