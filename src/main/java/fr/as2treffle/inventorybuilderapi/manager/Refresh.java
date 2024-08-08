package fr.as2treffle.inventorybuilderapi.manager;

import fr.as2treffle.inventorybuilderapi.inventory.InventoryBuilder;
import fr.as2treffle.inventorybuilderapi.itemstack.ItemStackBuilder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Refresh extends BukkitRunnable {

    private final String id;
    private final YamlConfiguration file;
    private final Integer speed;
    private final Integer slot;
    private final Player player;
    private final Inventory inventory;

    public Refresh(String id, YamlConfiguration file, Integer speed, Integer slot, Player player, Inventory inventory) {
        this.id = id;
        this.file = file;
        this.speed = speed;
        this.slot = slot;
        this.player = player;
        this.inventory = inventory;
    }

    public String getId() {
        return id;
    }

    public YamlConfiguration getInventoryFile() {
        return file;
    }

    public Integer getSpeed() {
        return speed;
    }

    public Integer getSlot() {
        return slot;
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void run() {

        ItemStack current = player.getOpenInventory().getItem(slot);

        String symbol = ConditionsManager.getID(file, player, inventory, slot);
        ItemStack stack = ItemStackBuilder.buildItemStack(file, symbol, player);

        if (current != stack) {

            player.getOpenInventory().setItem(slot, stack);
        }

    }
}
