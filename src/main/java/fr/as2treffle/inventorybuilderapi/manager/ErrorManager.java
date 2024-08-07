package fr.as2treffle.inventorybuilderapi.manager;

import fr.as2treffle.inventorybuilderapi.itemstack.LeatherArmorColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;

public class ErrorManager {

    public static void sendErrorMessageToPlayer(Player player, ErrorType type) {
        if (player != null) {
            player.sendMessage("§c" + type.getMessage());
        }
    }

    public static void sendErrorMessageToPlayer(Player player, ArrayList<Object> messages) {
        if (player != null) {
            for (Object message : messages) {
                if (message instanceof String) {
                    player.sendMessage("- §c" + (String) message);
                }
            }
        }
    }

    public static boolean isAnInventoryType(String inventoryType) {
        for (InventoryType type : InventoryType.values()) {
            if (type.name().equals(inventoryType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAnMaterial(String s) {
        for (Material type : Material.values()) {
            if (type.name().equals(s)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("all")
    public static boolean isAnEnchantment(String s) {
        for (Enchantment enchant : Enchantment.values()) {
            if (enchant.getName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAnItemFlag(String s) {
        for (ItemFlag flag : ItemFlag.values()) {
            if (flag.name().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAnColor(String s) {
        for (LeatherArmorColor color : LeatherArmorColor.values()) {
            if (color.name().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkSchematic(ArrayList<String> schematic, InventoryType type, Integer size) {
        if (type == InventoryType.CHEST) {
            if (schematic.size() == size / 9) {
                for (String line : schematic) {
                    if (line.length() != 9) {
                        return false;
                    }
                }

                return true;
            }
        }
        else {
            return schematic.get(0).length() == size;
        }

        return false;
    }

    @SuppressWarnings("all")
    public static ArrayList<Object> checkItemStack(YamlConfiguration inventory_file, String name, Player player) {
        ArrayList<Object> errors = new ArrayList<>();

        if (inventory_file.contains(name + ".animation")) {
            return errors;
        }

        if (!inventory_file.contains(name + ".type")) {
            errors.add(ErrorType.ITEMSTACK_NOT_DEFINED.getMessage().replace("%arg%", name));
        }
        else {
            String type = inventory_file.getString(name + ".type");
            type = DataManager.replaceData(player, type).toUpperCase();

            if (!ErrorManager.isAnMaterial(type)) {
                errors.add(ErrorType.MATERIAL_ERROR.getMessage().replace("%arg%", name));
            }

            if (!inventory_file.contains(name + ".amount")) {
                errors.add(ErrorType.ITEMSTACK_AMOUNT_NOT_DEFINED.getMessage().replace("%arg%", name));
            }
        }

        return errors;
    }
}
