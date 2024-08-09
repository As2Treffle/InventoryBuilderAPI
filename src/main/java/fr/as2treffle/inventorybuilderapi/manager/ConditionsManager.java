package fr.as2treffle.inventorybuilderapi.manager;

import fr.as2treffle.inventorybuilderapi.inventory.InventoryBuilder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;

public class ConditionsManager {

    @SuppressWarnings("all")
    public static String getID(YamlConfiguration config, Player player, Inventory inventory, Integer slot) {

        String symbol = InventoryBuilder.getSymbolFromSlot(config, slot);
        symbol =  getID(config, player, inventory, symbol, slot);

        HashMap<Integer, String> symbols = InventoryBuilder.items.get(player.getUniqueId());
        symbols.put(slot, symbol);
        InventoryBuilder.items.put(player.getUniqueId(), symbols);

        return symbol;
    }

    public static String getID(YamlConfiguration config, Player player, Inventory inventory, String symbol, Integer slot) {

        if (!config.contains(symbol + ".if")){
            return symbol;
        }

        ArrayList<String> conditions = (ArrayList<String>) config.getStringList(symbol + ".if");

        for (String condition_s : conditions) {
            String[] split = condition_s.split(" -> ");
            String[] conditions_and = split[0].split(" AND ");
            String[] conditions_or = split[0].split(" OR ");

            if (conditions_and.length == 1 && conditions_or.length == 1) {

                String[] details = split[0].split("=");
                if (!details[0].startsWith("!")) {
                    if (callConditionMethod(config, player, inventory, details[0], details[1], slot)) {
                        return split[1];
                    }
                }
                else {
                    details[0] = details[0].substring(1);
                    if (!callConditionMethod(config, player, inventory, details[0], details[1], slot)) {
                        return split[1];
                    }
                }
            }

            if (conditions_or.length >  1 && conditions_and.length == 1) {

                ArrayList<Boolean> results = new ArrayList<Boolean>();

                for (String condition_or_s : conditions_or) {
                    String[] details = condition_or_s.split("=");
                    if (!details[0].startsWith("!")) {
                        results.add(callConditionMethod(config, player, inventory, details[0], details[1], slot));
                    }
                    else {
                        details[0] = details[0].substring(1);
                        results.add(!callConditionMethod(config, player, inventory, details[0], details[1], slot));
                    }
                }

                if (oneTrue(results)) {
                    return split[1];
                }
            }

            if (conditions_and.length >  1 && conditions_or.length == 1) {

                ArrayList<Boolean> results = new ArrayList<Boolean>();

                for (String condition_and_s : conditions_and) {
                    String[] details = condition_and_s.split("=");
                    if (!details[0].startsWith("!")) {
                        results.add(callConditionMethod(config, player, inventory, details[0], details[1], slot));
                    }
                    else {
                        details[0] = details[0].substring(1);
                        results.add(!callConditionMethod(config, player, inventory, details[0], details[1], slot));
                    }
                }

                if (allTrue(results)) {
                    return split[1];
                }
            }
        }

        return symbol;
    }

    public static boolean allTrue(ArrayList<Boolean> results) {

        for (Boolean result : results) {
            if (!result) {
                return false;
            }
        }

        return true;
    }

    public static boolean oneTrue(ArrayList<Boolean> results) {

        for (Boolean result : results) {
            if (result) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("all")
    public static boolean callConditionMethod(YamlConfiguration file, Player player, Inventory inventory, String condition, String args, Integer slot) {

        if (file.contains("addons")) {
            ArrayList<String> addons = (ArrayList<String>) file.getStringList("addons");
            for (String addon_f : addons) {
                String[] split = addon_f.split(" == ");

                split[1] = split[1].replace(" ", "");

                if (!condition.startsWith(split[1] + ".")) {
                    String addon_name = "InventoryBuilderAPI";
                    Addon addon = AddonManager.addons.get(addon_name);
                    if (addon != null) {
                        args = DataManager.replaceData(player, args);
                        return addon.checkCondition(player, inventory, condition, args, slot);
                    }
                }
                else {
                    String addon_name = split[0];

                    Addon addon = AddonManager.addons.get(addon_name);
                    String[] split1 = condition.split(split[1] + ".");
                    if (addon != null) {
                        args = DataManager.replaceData(player, args);
                        return addon.checkCondition(player, inventory, condition, args, slot);
                    }
                }
            }
        }
        else {
            String addon_name = "InventoryBuilderAPI";
            Addon addon = AddonManager.addons.get(addon_name);

            if (addon != null) {
                args = DataManager.replaceData(player, args);
                return addon.checkCondition(player, inventory, condition, args, slot);
            }
        }

        return false;
    }

    @SuppressWarnings("all")
    public static Boolean compare(Player player, String v, String comparator_s, String value_s) {

        Comparator comparator = Comparator.getBySymbol(comparator_s);

        if (comparator == Comparator.NOT_FOUND) {
            return false;
        }

        if (DataManager.canBeInteger(v)) {
            Integer value = Integer.valueOf(value_s);
            Integer var = Integer.valueOf(v);

            switch (comparator) {
                case INFERIOR:
                    return var < value;
                case SUPERIOR:
                    return var > value;
                case EQUALS_OR_INFERIOR:
                    return var <= value;
                case EQUALS_OR_SUPERIOR:
                    return var >= value;
                case EQUALS:
                    return var == value;
                case NOT_EQUALS:
                    return var != value;
                default:
                    return false;
            }
        }
        else if (DataManager.canBeDouble(v)) {
            Double value = Double.valueOf(value_s);
            Double var = Double.valueOf(v);

            switch (comparator) {
                case INFERIOR:
                    return var < value;
                case SUPERIOR:
                    return var > value;
                case EQUALS_OR_INFERIOR:
                    return var <= value;
                case EQUALS_OR_SUPERIOR:
                    return var >= value;
                case EQUALS:
                    return var == value;
                case NOT_EQUALS:
                    return var != value;
                default:
                    return false;
            }
        }
        else {

            if (comparator == Comparator.STARTS_WITH) {
                return v.startsWith(value_s);
            }

            if (comparator == Comparator.ENDS_WITH) {
                return v.endsWith(value_s);
            }

            if (comparator == Comparator.CONTAINS) {
                return v.endsWith(value_s);
            }

            if (comparator == Comparator.EQUALS) {
                return v.equals(value_s);
            }
        }

        return false;
    }
}
