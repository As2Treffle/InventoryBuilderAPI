package fr.as2treffle.inventorybuilderapi.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Condition {
    private final Player player;
    private final Inventory inventory;
    private final String condition;
    private final String args;
    private final Integer slot;

    public Condition(Player player, Inventory inventory, String condition, String args, Integer slot) {
        this.player = player;
        this.inventory = inventory;
        this.condition = condition;
        this.args = args;
        this.slot = slot;
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getArgs() {
        return args;
    }

    public Integer getSlot() {
        return slot;
    }

    public String getCondition() {
        return condition;
    }
}

