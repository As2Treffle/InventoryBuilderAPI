package fr.as2treffle.inventorybuilderapi.manager;

import fr.as2treffle.inventorybuilderapi.InventoryBuilderAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class AddonManager {

    public static HashMap<String, Addon> addons = new HashMap<>();

    public static void registerAddon(Plugin plugin, Addon addon) {
        addons.put(plugin.getName(), addon);
        Bukkit.getConsoleSender().sendMessage("§a" + plugin.getName() + " added !");
    }
}
