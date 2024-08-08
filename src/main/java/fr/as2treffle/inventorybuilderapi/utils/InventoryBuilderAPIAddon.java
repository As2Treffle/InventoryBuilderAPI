package fr.as2treffle.inventorybuilderapi.utils;

import fr.as2treffle.inventorybuilderapi.InventoryBuilderAPI;
import fr.as2treffle.inventorybuilderapi.inventory.InventoryBuilder;
import fr.as2treffle.inventorybuilderapi.itemstack.ItemStackBuilder;
import fr.as2treffle.inventorybuilderapi.manager.*;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class InventoryBuilderAPIAddon implements Addon {

    @Override
    @SuppressWarnings("all")
    public void performAction(Player player, Inventory inventory, ClickType clickType, ItemStack itemStack, String action_name, String args, Integer slot) {

        if (action_name.equals("close")) {
            player.closeInventory();
        }

        if (action_name.equals("sendMessage")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', args));
        }

        if (action_name.equals("open")) {

            AnimationManager.stopAnimations(player.getUniqueId());

            player.closeInventory();
            Inventory inv = InventoryBuilder.buildInventory(player, "/" + args);

            if (inv != null) {
                player.openInventory(inv);
            }
        }

        if (action_name.equals("playerCommand")) {
            args = PlaceholderAPI.setPlaceholders(player, args);
            player.performCommand(args);
        }

        if (action_name.equals("consoleCommand")) {
            args = PlaceholderAPI.setPlaceholders(player, args);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), args);
        }

        if (action_name.equals("playSound")) {
            String[] split = args.split(", ");

            if (split.length == 3) {
                String note = split[0];
                float volume = Float.parseFloat(split[1]);
                float pitch = Float.parseFloat(split[2]);

                player.playSound(player.getEyeLocation(), Sound.valueOf(note), volume, pitch);
            }
        }

        if (action_name.equals("setProperty")) {
            String[] split = args.split(", ");

            if (split.length == 2) {
                InventoryView.Property property = InventoryView.Property.valueOf(split[0]);
                int volume = Integer.parseInt(split[1]);

                player.getOpenInventory().setProperty(property, volume);
                player.getOpenInventory().setProperty(InventoryView.Property.TICKS_FOR_CURRENT_FUEL, 100);
                player.getOpenInventory().setProperty(InventoryView.Property.TICKS_FOR_CURRENT_SMELTING, 100);
            }
        }

        if (action_name.equals("refresh")) {

            if (InventoryBuilderAPI.inv_opened.containsKey(player.getUniqueId())) {
                if (args.equalsIgnoreCase("")) {
                    YamlConfiguration config = InventoryBuilderAPI.inv_opened.get(player.getUniqueId());

                    String symbol = ConditionsManager.getID(config, player, inventory, slot);
                    ItemStack stack = ItemStackBuilder.buildItemStack(config, symbol, player);
                    player.getOpenInventory().setItem(slot, stack);

                }
                else if (args.startsWith("list:")) {
                    String id = args.replace("list:", "");
                    List list = ListManager.getList(player, id);

                    ArrayList<Integer> slots = list.getSlots();

                    for (int i : slots) {

                        YamlConfiguration config = InventoryBuilderAPI.inv_opened.get(player.getUniqueId());
                        ItemStack current = player.getOpenInventory().getItem(i);
                        ListManager.initializeLists(config, player, inventory);
                        ItemStack stack = ItemStackBuilder.buildListItem(config, player, i, id);

                        player.getOpenInventory().setItem(i, stack);
                    }
                }
                else {
                    String[] split = args.split(", ");

                    for (String slot_s : split) {
                        YamlConfiguration config = InventoryBuilderAPI.inv_opened.get(player.getUniqueId());

                        String symbol = ConditionsManager.getID(config, player, inventory, Integer.parseInt(slot_s));
                        ItemStack stack = ItemStackBuilder.buildItemStack(config, symbol, player);
                        player.getOpenInventory().setItem(Integer.parseInt(slot_s), stack);
                    }
                }
            }
        }

        if (action_name.equals("data")) {
            String[] split = args.split(", ");

            if (split.length == 2) {
                String id = split[0];
                String value = split[1];

                DataManager.setNewValue(player, id, value);
            }

            if (split.length == 3) {
                DataManager.performOperation(player, split[0], split[1], split[2]);
            }
        }

        if (action_name.equals("openPrevious")) {

            if (!InventoryBuilderAPI.previous_inv.containsKey(player.getUniqueId())){
                player.closeInventory();
            }

            InventoryContainer inventoryContainer = InventoryBuilderAPI.previous_inv.get(player.getUniqueId());
            player.openInventory(InventoryBuilder.buildInventory(player, inventoryContainer.getInventoryFile(), inventoryContainer.getData()));
        }
    }

    @Override
    public boolean checkCondition(Player player, Inventory inventory, String condition, String args, Integer slot) {

        if (condition.equals("hasPermission")) {
            return player.hasPermission(args);
        }

        if (condition.equals("hasEnough")) {
            String[] split = args.split(", ");
            if (split.length == 2) {
                Material type = Material.valueOf(split[0]);
                int amount = Integer.parseInt(split[1]);
                return player.getInventory().contains(type, amount);
            }
        }

        if (condition.equals("isEmpty")) {
            int a = Integer.parseInt(args);
            return inventory.getItem(a) == null;
        }

        if (condition.equals("compare")) {

            args = PlaceholderAPI.setPlaceholders(player, args);
            String[] split = args.split(", ");

            return ConditionsManager.compare(player, split[0], split[1], split[2]);
        }

        return false;
    }

    @Override
    public ItemStack getCustomItemStack(Player player, Inventory inventory, ClickType clickType, ItemStack itemStack, String method, String args) {
        return null;
    }

    @Override
    public ArrayList<HashMap<String, Object>> getCustomList(Player player, Inventory inventory, String method, String args) {

        ArrayList<HashMap<String, Object>> values = new ArrayList<>();

        if (method.equals("getTypes")) {
            String[] split = args.split(", ");

            int starting_index = Integer.parseInt(split[0]);

            for (Material m : Material.values()) {
                HashMap<String, Object> value = new HashMap<>();

                if (m.isFuel()) {
                    value.put("item", new ItemStack(m));
                    value.put("type", m.toString().toLowerCase());
                    values.add(value);
                }
            }

            if (starting_index > 0) {
                values.subList(0, starting_index).clear();
            }
        }

        return values;
    }

    @Override
    public java.util.List<String> getActions() {
        java.util.List<String> actions = new ArrayList<>(java.util.List.of(
                "close=",
                "sendMessage=<msg>",
                "open=<yaml_file>",
                "playerCommand=<command>",
                "consoleCommand=<console>",
                "playSound=<sound_name>, <volume>, <pitch>",
                "setProperty=<InventoryView.Property, <value_in_%>",
                "refresh=",
                "refresh=<slot n°1>, <slot n°2>, ..., <slot n°k>",
                "refresh=list:<id>",
                "data=<id>, <+-*/%>, <value>",
                "data=<id>, <value>",
                "openPrevious="
        ));

        Collections.sort(actions);
        return actions;
    }

    @Override
    public java.util.List<String> getConditions() {
        java.util.List<String> conditions = new ArrayList<>(java.util.List.of(
                "hasPermission=<permission>",
                "hasEnough=<type>, <amount>",
                "compare=%some_placeholder%, <comparator>, <value>",
                "compare=[data.id], <comparator>, <value>",
                "compare=size:<ListMethod>, <comparator>, <value>",
                "isEmpty=<slot>",
                "List of Comparator: <, >, <=, >=, ==, !=, startsWith, endsWith, contains"
        ));

        Collections.sort(conditions);
        return conditions;
    }

    @Override
    public java.util.List<String> getItemStackMethods() {
        return java.util.List.of();
    }

    @Override
    public java.util.List<String> getListMethods() {
        return java.util.List.of();
    }
}
