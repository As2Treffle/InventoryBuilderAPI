package fr.as2treffle.inventorybuilderapi.commands;

import fr.as2treffle.inventorybuilderapi.inventory.InventoryBuilder;
import fr.as2treffle.inventorybuilderapi.manager.Addon;
import fr.as2treffle.inventorybuilderapi.manager.AddonManager;
import fr.as2treffle.inventorybuilderapi.manager.ErrorType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class IBCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if(args.length == 1) {
                if (player.hasPermission("inventorybuilderapi.command.addons")) {
                    if (args[0].equalsIgnoreCase("addons")) {

                        player.sendMessage("§8[§aInventoryBuilderAPI§8] §6Addons List:");
                        for (String addon : AddonManager.addons.keySet()) {
                            player.sendMessage("- §a" + addon);
                        }
                    }
                }
            }
            else if (args.length == 2) {
                if (player.hasPermission("inventorybuilderapi.command.addons")) {

                    if (args[0].equalsIgnoreCase("help")) {

                        Addon addon = AddonManager.addons.get(args[1]);

                        if (addon == null) {
                            player.sendMessage("§c" + ErrorType.ADDON_NOT_FOUND.getMessage().replace("%arg%", args[1]));
                            return true;
                        }
                        player.sendMessage("§8[§aInventoryBuilderAPI§8] §6" + args[1] + " help:");
                        player.sendMessage("§6Actions Methods:");
                        for (String action : addon.getActions()) {
                            player.sendMessage("- " + action);
                        }
                        player.sendMessage("§6Conditions Methods:");
                        for (String condition : addon.getConditions()) {
                            player.sendMessage("- " + condition);
                        }
                    }
                }

                if (player.hasPermission("inventorybuilderapi.command.view")) {
                    if (args[0].equalsIgnoreCase("view")) {

                        String file_name = args[1];
                        Inventory inventory = InventoryBuilder.buildInventory(player,"/" + file_name);

                        if(inventory != null) {
                            player.openInventory(inventory);

                        }
                    }
                }
            }
        }
        return true;
    }
}
