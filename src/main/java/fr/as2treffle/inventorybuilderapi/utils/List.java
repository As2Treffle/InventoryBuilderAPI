package fr.as2treffle.inventorybuilderapi.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class List {

    private final Player player;
    private final Inventory inventory;
    private final String method;
    private final String args;
    private final YamlConfiguration file;

    public List(Player player, Inventory inventory, String method, String args, YamlConfiguration file) {
        this.player = player;
        this.inventory = inventory;
        this.method = method;
        this.args = args;
        this.file = file;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public String getArgs() {
        return args;
    }

    public YamlConfiguration getFile() {
        return file;
    }

    public String getMethod() {
        return method;
    }
}
