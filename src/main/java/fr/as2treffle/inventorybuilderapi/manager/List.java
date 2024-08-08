package fr.as2treffle.inventorybuilderapi.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class List {

    private final Player player;
    private final Inventory inventory;
    private final String method;
    private final ArrayList<Integer> slots;
    private final String id;
    private final YamlConfiguration file;
    private final ArrayList<HashMap<String, Object>> values;

    public List(Player player, Inventory inventory, String method, ArrayList<Integer> slots, String id, YamlConfiguration file, ArrayList<HashMap<String, Object>> values) {
        this.player = player;
        this.inventory = inventory;
        this.method = method;
        this.slots = slots;
        this.id = id;
        this.file = file;
        this.values = values;
    }

    public Player getPlayer() {
        return player;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Integer> getSlots() {
        return slots;
    }

    public String getMethod() {
        return method;
    }

    public YamlConfiguration getFile() {
        return file;
    }

    public ArrayList<HashMap<String, Object>> getValues() {
        return values;
    }
}
