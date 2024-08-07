package fr.as2treffle.inventorybuilderapi.manager;

import fr.as2treffle.inventorybuilderapi.itemstack.ItemStackBuilder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class RefreshList extends BukkitRunnable {

    private final String id;
    private final YamlConfiguration file;
    private final Integer speed;
    private final ArrayList<Integer> slots;
    private final Player player;
    private final Inventory inventory;

    public RefreshList(String id, YamlConfiguration file, Integer speed, ArrayList<Integer> slots, Player player, Inventory inventory) {
        this.id = id;
        this.file = file;
        this.speed = speed;
        this.slots = slots;
        this.player = player;
        this.inventory = inventory;
    }

    public String getId() {
        return id;
    }

    public YamlConfiguration getFile() {
        return file;
    }

    public Integer getSpeed() {
        return speed;
    }

    public ArrayList<Integer> getSlots() {
        return slots;
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void run() {

        for (int slot : slots) {

            ItemStack current = player.getOpenInventory().getItem(slot);
            ListManager.initializeLists(file, player, inventory);
            ItemStack stack = ItemStackBuilder.buildListItem(file, player, slot, id);

            if (stack != current) {
                player.getOpenInventory().setItem(slot, stack);
            }
        }
    }
}
