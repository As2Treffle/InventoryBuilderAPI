package fr.as2treffle.inventorybuilderapi.manager;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;

public class InventoryContainer {

    private final YamlConfiguration inventory_file;
    private final ArrayList<Data> data;

    public InventoryContainer(YamlConfiguration inventoryFile, ArrayList<Data> data) {
        inventory_file = inventoryFile;
        this.data = data;
    }

    public YamlConfiguration getInventoryFile() {
        return inventory_file;
    }

    public ArrayList<Data> getData() {
        return data;
    }
}
