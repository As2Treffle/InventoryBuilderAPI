package fr.as2treffle.inventorybuilderapi.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ActionsManager {

    @SuppressWarnings("all")
    public static void performsActions(YamlConfiguration file, Player player, String c, Inventory inv, ItemStack stack, Integer slot, ClickType click) {

        if (file.contains(c + ".actions.all")) {
            ArrayList<String> actions = (ArrayList<String>) file.getStringList(c + ".actions.all");
            for (String action : actions) {
                String[] split = action.split("=");

                if (split.length == 2) {
                    callAction(file, inv, click, split[0], split[1], player, stack, slot, c);
                }
                else {
                    callAction(file, inv, click, split[0], "", player, stack, slot, c);
                }
            }
        }

        if (file.contains(c + ".actions.right")) {
            if (click == ClickType.RIGHT) {
                ArrayList<String> actions = (ArrayList<String>) file.getStringList(c + ".actions.right");
                for (String action : actions) {
                    String[] split = action.split("=");

                    if (split.length == 2) {
                        callAction(file, inv, click, split[0], split[1], player, stack, slot, c);
                    }
                    else {
                        callAction(file, inv, click, split[0], "", player, stack, slot, c);
                    }
                }
            }
        }

        if (file.contains(c + ".actions.left")) {
            if (click == ClickType.LEFT) {
                ArrayList<String> actions = (ArrayList<String>) file.getStringList(c + ".actions.left");
                for (String action : actions) {
                    String[] split = action.split("=");

                    if (split.length == 2) {
                        callAction(file, inv, click, split[0], split[1], player, stack, slot, c);
                    }
                    else {
                        callAction(file, inv, click, split[0], "", player, stack, slot, c);
                    }
                }
            }
        }

        if (file.contains(c + ".actions.middle")) {
            if (click == ClickType.MIDDLE) {
                ArrayList<String> actions = (ArrayList<String>) file.getStringList(c + ".actions.middle");
                for (String action : actions) {
                    String[] split = action.split("=");

                    if (split.length == 2) {
                        callAction(file, inv, click, split[0], split[1], player, stack, slot, c);
                    }
                    else {
                        callAction(file, inv, click, split[0], "", player, stack, slot, c);
                    }
                }
            }
        }

        if (file.contains(c + ".actions.shift-left")) {
            if (click == ClickType.SHIFT_LEFT) {
                ArrayList<String> actions = (ArrayList<String>) file.getStringList(c + ".actions.shift-left");
                for (String action : actions) {
                    String[] split = action.split("=");

                    if (split.length == 2) {
                        callAction(file, inv, click, split[0], split[1], player, stack, slot, c);
                    }
                    else {
                        callAction(file, inv, click, split[0], "", player, stack, slot, c);
                    }
                }
            }
        }

        if (file.contains(c + ".actions.shift-right")) {
            if (click == ClickType.SHIFT_RIGHT) {
                ArrayList<String> actions = (ArrayList<String>) file.getStringList(c + ".actions.shift-right");
                for (String action : actions) {
                    String[] split = action.split("=");

                    if (split.length == 2) {
                        callAction(file, inv, click, split[0], split[1], player, stack, slot, c);
                    }
                    else {
                        callAction(file, inv, click, split[0], "", player, stack, slot, c);
                    }
                }
            }
        }
    }

    @SuppressWarnings("all")
    public static void performsActions(YamlConfiguration file, Player player, Inventory inv, ActionCause cause) {

        ArrayList<String> actions = new ArrayList<>();

        if (cause == ActionCause.CLOSING) {

            if (file.contains("when-closed")) {
                actions = (ArrayList<String>) file.getStringList("when-closed");
            }
        }

        if (cause == ActionCause.OPENING) {

            if (file.contains("when-opened")) {
                actions = (ArrayList<String>) file.getStringList("when-opened");
            }
        }

        for (String action : actions) {
            String[] split = action.split("=");

            if (split.length == 2) {
                callAction(file, inv, null, split[0], split[1], player, null, null, null);
            }
            else {
                callAction(file, inv, null, split[0], "", player, null, null, null);
            }
        }
    }

    @SuppressWarnings("all")
    public static void performsActions(YamlConfiguration file, Player player, ArrayList<String> actions, Inventory inv) {

        for (String action : actions) {
            String[] split = action.split("=");

            if (split.length == 2) {
                callAction(file, inv, null, split[0], split[1], player, null, null, null);
            }
            else {
                callAction(file, inv, null, split[0], "", player, null, null, null);
            }
        }
    }

    @SuppressWarnings("all")
    private static void callAction(YamlConfiguration file, Inventory inv, ClickType click, String action_name, String args, Player player, ItemStack stack, Integer slot, String id) {

        if (file.contains("addons")) {
            ArrayList<String> addons = (ArrayList<String>) file.getStringList("addons");
            for (String addon_f : addons) {
                String[] split = addon_f.split(" == ");

                split[1] = split[1].replace(" ", "");

                if (!action_name.startsWith(split[1] + ".")) {
                    String addon_name = "InventoryBuilderAPI";
                    Addon addon = AddonManager.addons.get(addon_name);
                    if (addon != null) {
                        args = ListManager.parseListValue(args, ListManager.getList(player, id), slot);
                        args = DataManager.replaceData(player, args);
                        addon.performAction(player, inv, click, stack, action_name, args, slot);
                    }
                }
                else {
                    String addon_name = split[0];

                    Addon addon = AddonManager.addons.get(addon_name);
                    String[] split1 = action_name.split(split[1] + ".");
                    if (addon != null) {
                        args = ListManager.parseListValue(args, ListManager.getList(player, id), slot);
                        args = DataManager.replaceData(player, args);
                        addon.performAction(player, inv, click, stack, split1[1], args, slot);
                    }
                }
            }
        }
        else {
            String addon_name = "InventoryBuilderAPI";
            Addon addon = AddonManager.addons.get(addon_name);

            if (addon != null) {
                args = ListManager.parseListValue(args, ListManager.getList(player, id), slot);
                args = DataManager.replaceData(player, args);
                addon.performAction(player, inv, click, stack, action_name, args, slot);
            }
        }
    }
}
