package fr.as2treffle.inventorybuilderapi.manager;

import fr.as2treffle.inventorybuilderapi.InventoryBuilderAPI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class FileManager {

    public static YamlConfiguration getInventoryFile(Player player, String inventory_file_path) {
         File file;

        file = new File(InventoryBuilderAPI.instance.getDataFolder() + inventory_file_path);

        if (!file.exists()) {
            ErrorManager.sendErrorMessageToPlayer(player, ErrorType.INVENTORY_FILE_NOT_FOUND);
        }

        return YamlConfiguration.loadConfiguration(file);
    }
}
