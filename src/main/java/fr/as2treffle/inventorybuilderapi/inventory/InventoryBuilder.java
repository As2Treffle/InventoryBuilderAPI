package fr.as2treffle.inventorybuilderapi.inventory;

import fr.as2treffle.inventorybuilderapi.InventoryBuilderAPI;
import fr.as2treffle.inventorybuilderapi.itemstack.ItemStackBuilder;
import fr.as2treffle.inventorybuilderapi.manager.*;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class InventoryBuilder {

    private static YamlConfiguration inventory_file;
    public static HashMap<UUID, HashMap<Integer, String>> items = new HashMap<>();

    @SuppressWarnings("all")
    public static Inventory buildInventory(Player player, String file_path) {
        inventory_file = FileManager.getInventoryFile(player, file_path);

        HashMap<Boolean, ArrayList<Object>> result = getInventorySettings();
        DataManager.initializeData(player, inventory_file);

        if (result.containsKey(false)) {
            ArrayList<Object> messages = result.get(false);
            player.sendMessage("§8[§aInventoryBuilderAPI§8] §c" + messages.size() +  " Critical Errors found:");
            player.sendMessage(" ");
            ErrorManager.sendErrorMessageToPlayer(player, messages);
            return null;
        }

        ArrayList<Object> settings = result.get(true);
        Inventory inventory;

        if (!ErrorManager.checkSchematic((ArrayList<String>) inventory_file.getStringList("inventory"), (InventoryType) settings.get(0), (Integer) settings.get(1))) {
            ErrorManager.sendErrorMessageToPlayer(player, ErrorType.INVENTORY_BAD_SCHEMATIC_ERROR);
            return null;
        }

        String title = PlaceholderAPI.setPlaceholders(player, (String) settings.get(2));
        title = DataManager.replaceData(player, title);
        title = ChatColor.translateAlternateColorCodes('&', title);

        if (settings.get(0) == InventoryType.CHEST) {
            inventory = Bukkit.createInventory(null, (Integer) settings.get(1), title);
        }
        else if (settings.get(0).toString().contains("FURNACE") || settings.get(0).toString().contains("SMOKER")) {
            inventory = (FurnaceInventory) Bukkit.createInventory(null, (InventoryType) settings.get(0), title);
        }
        else {
            inventory = Bukkit.createInventory(null, (InventoryType) settings.get(0), title);
        }

        ListManager.initializeLists(inventory_file, player, inventory);
        ArrayList<String> schematic = (ArrayList<String>) inventory_file.getStringList("inventory");

        HashMap<Integer, String> symbols = new HashMap<>();

        for (int i = 0; i < schematic.size(); i++) {
            for (int j = 0; j < schematic.get(0).length(); j++) {
                String c = schematic.get(i).charAt(j) + "";

                if (inventory_file.contains(c + ".list")) {
                    inventory.setItem(9 * i + j, ItemStackBuilder.buildListItem(inventory_file, player, 9 * i + j, c));
                    symbols.put(9 * i + j, c + ".for-each");
                }
                else {
                    String symbol = ConditionsManager.getID(inventory_file, player, inventory, c, 9 * i + j);
                    inventory.setItem(9 * i + j, ItemStackBuilder.buildItemStack(inventory_file, symbol, player));
                    symbols.put(9 * i + j, symbol);
                }
            }
        }

        items.put(player.getUniqueId(), symbols);
        InventoryBuilderAPI.inv_opened.put(player.getUniqueId(), inventory_file);

        return inventory;

    }

    @SuppressWarnings("unchecked")
    private static HashMap<Boolean, ArrayList<Object>> getInventorySettings() {
        HashMap<Boolean, ArrayList<Object>> result = new HashMap<>();
        ArrayList<Object> settings = new ArrayList<>();
        ArrayList<Object> errors = new ArrayList<>();

        /*
        Settings:
            - 0 = InventoryType
            - 1 = Size
            - 2 = Title
            - 3 = Schematic
        */

        if (!inventory_file.contains("type")) {
            errors.add(ErrorType.INVENTORY_TYPE_ERROR.getMessage());
        }
        else {
            String type = Objects.requireNonNull(inventory_file.getString("type")).toUpperCase();

            if (!ErrorManager.isAnInventoryType(type)) {
                errors.add(ErrorType.INVENTORY_TYPE_SYNTAX_ERROR.getMessage());
            }

            settings.add(InventoryType.valueOf(type));
        }

        if (!inventory_file.contains("size")) {
            errors.add(ErrorType.INVENTORY_SIZE_ERROR.getMessage());
        }
        else {
            int size = inventory_file.getInt("size");

            if (settings.get(0) == InventoryType.CHEST) {
                ArrayList<Integer> sizes = new ArrayList<>(
                        Arrays.asList(9, 18, 27, 36, 45, 54)
                );

                if (!sizes.contains(size)) {
                    errors.add(ErrorType.INVENTORY_SIZE_VALUE_ERROR.getMessage());
                }
            }

            settings.add(size);
        }

        if (!inventory_file.contains("title")) {
            errors.add(ErrorType.INVENTORY_TITLE_ERROR.getMessage());
            settings.add("Inventory Title not defined!");
        }
        else {
            settings.add(inventory_file.getString("title"));
        }

        if (!inventory_file.contains("inventory")) {
            errors.add(ErrorType.INVENTORY_SCHEMATIC_ERROR.getMessage());
        }
        else {
            if (inventory_file.contains("type")) {
                if(ErrorManager.checkSchematic((ArrayList<String>) inventory_file.getList("inventory"), (InventoryType) settings.get(0), (Integer) settings.get(1))) {
                    settings.add(inventory_file.getList("inventory"));
                }
                else {
                    errors.add(ErrorType.INVENTORY_SCHEMATIC_ERROR.getMessage());
                }
            }
        }

        if (errors.isEmpty()) {
            result.put(true, settings);
        }
        else {
            result.put(false, errors);
        }

        return result;
    }

    public static String getSymbolFromSlot(YamlConfiguration file, Integer slot) {

        int line = slot / 9;
        int index = slot % 9;

        InventoryType type = InventoryType.valueOf(file.getString("type"));
        ArrayList<String> schematic = (ArrayList<String>) file.getStringList("inventory");

        if (type == InventoryType.CHEST) {
            return schematic.get(line).charAt(index) + "";
        }

        return schematic.get(0).charAt(index) + "";
    }
}
