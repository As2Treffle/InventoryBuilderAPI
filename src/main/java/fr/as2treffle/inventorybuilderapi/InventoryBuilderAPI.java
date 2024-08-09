package fr.as2treffle.inventorybuilderapi;

import fr.as2treffle.inventorybuilderapi.commands.IBCommand;
import fr.as2treffle.inventorybuilderapi.commands.IBCommandTabCompleter;
import fr.as2treffle.inventorybuilderapi.listeners.InventoryListeners;
import fr.as2treffle.inventorybuilderapi.listeners.ServerListeners;
import fr.as2treffle.inventorybuilderapi.manager.AddonManager;
import fr.as2treffle.inventorybuilderapi.manager.InventoryContainer;
import fr.as2treffle.inventorybuilderapi.utils.InventoryBuilderAPIAddon;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class InventoryBuilderAPI extends JavaPlugin {

    public static InventoryBuilderAPI instance;

    public static HashMap<UUID, YamlConfiguration> inv_opened = new HashMap<>();
    public static HashMap<UUID, InventoryContainer> previous_inv = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getConsoleSender().sendMessage("InventoryBuilderAPI has been enabled!");

        createPluginFolder();
        registerCommands();
        AddonManager.registerAddon(this, new InventoryBuilderAPIAddon());
        Bukkit.getPluginManager().registerEvents(new InventoryListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ServerListeners(), this);
    }

    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage("InventoryBuilderAPI disabled!");
    }

    private void createPluginFolder() {
        if (!getDataFolder().exists()) {
            Bukkit.getConsoleSender().sendMessage("No folder exists at " + getDataFolder().getAbsolutePath() + ": Let's created one !");
            if(getDataFolder().mkdir()) {
                Bukkit.getConsoleSender().sendMessage("Folder created!");
            }
            else {
                Bukkit.getConsoleSender().sendMessage("Error creating folder!");
            }
        }
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("ib")).setExecutor(new IBCommand());
        Objects.requireNonNull(getCommand("ib")).setTabCompleter(new IBCommandTabCompleter());
    }
}
