package fr.as2treffle.inventorybuilderapi.listeners;

import fr.as2treffle.inventorybuilderapi.manager.AddonManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerListeners implements Listener {

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {

        for (String addon_name : AddonManager.addons.keySet()) {
            Bukkit.getConsoleSender().sendMessage("[InventoryBuilderAPI] " + addon_name + " addon successfully registered!");
        }

        Bukkit.getConsoleSender().sendMessage("§a[InventoryBuilderAPI] " + AddonManager.addons.keySet().size() + " addons registered!");
    }

}
