package fr.as2treffle.inventorybuilderapi.listeners;

import fr.as2treffle.inventorybuilderapi.InventoryBuilderAPI;
import fr.as2treffle.inventorybuilderapi.inventory.InventoryBuilder;
import fr.as2treffle.inventorybuilderapi.manager.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryListeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        Integer slot = event.getSlot();
        ItemStack stack = event.getCurrentItem();
        ClickType click = event.getClick();
        InventoryAction action = event.getAction();

        if (InventoryBuilderAPI.inv_opened.containsKey(player.getUniqueId())) {

            YamlConfiguration file = InventoryBuilderAPI.inv_opened.get(player.getUniqueId());
            String c = InventoryBuilder.items.get(player.getUniqueId()).get(slot);

            if (event.getView().getTopInventory() == inventory) {

                if (c != null) {

                    if (file.contains(c + ".actions")) {
                        ActionsManager.performsActions(file, player, c, inventory, stack, slot, click);
                    }

                    if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        event.setCancelled(!file.contains(c + ".attributes.allow-deposit"));
                        return;
                    }

                    event.setCancelled(!file.contains(c + ".attributes.allow-pickup"));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (InventoryBuilderAPI.inv_opened.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();

        if (InventoryBuilderAPI.inv_opened.containsKey(player.getUniqueId())) {

            YamlConfiguration file = InventoryBuilderAPI.inv_opened.get(player.getUniqueId());
            ActionsManager.performsActions(file, player, inventory, ActionCause.CLOSING);

            InventoryBuilderAPI.inv_opened.remove(player.getUniqueId());

            DataManager.data.remove(player.getUniqueId());

            AnimationManager.stopAnimations(player.getUniqueId());

            RefreshManager.stopRefresh(player.getUniqueId());
        }
    }

    @EventHandler
    @SuppressWarnings("all")
    public void onInventoryOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();

        if (InventoryBuilderAPI.inv_opened.containsKey(player.getUniqueId())) {

            YamlConfiguration file = InventoryBuilderAPI.inv_opened.get(player.getUniqueId());
            ActionsManager.performsActions(file, player, inventory, ActionCause.OPENING);

            new BukkitRunnable() {

                @Override
                public void run() {
                    AnimationManager.createAnimations(file, player);
                    AnimationManager.playAnimations(player.getUniqueId());

                    if (file.contains("furnace.burn-time")) {
                        int burntime = file.getInt("furnace.burn-time");
                        InventoryView view = player.getOpenInventory();
                        view.setProperty(InventoryView.Property.BURN_TIME, burntime);
                        view.setProperty(InventoryView.Property.TICKS_FOR_CURRENT_FUEL, 100);
                    }

                    if (file.contains("furnace.cook-time")) {
                        int cooktime = file.getInt("furnace.cook-time");
                        InventoryView view = player.getOpenInventory();
                        view.setProperty(InventoryView.Property.TICKS_FOR_CURRENT_SMELTING, 100);
                        view.setProperty(InventoryView.Property.COOK_TIME, cooktime);
                    }

                    RefreshManager.initializeRefresh(file, player, inventory);
                    RefreshManager.playRefresh(player.getUniqueId());

                }
            }.runTaskLater(InventoryBuilderAPI.instance, 2);
        }
    }
}
