package fr.as2treffle.inventorybuilderapi.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ListManager {

    public static HashMap<UUID, ArrayList<List>> lists = new HashMap<>();

    @SuppressWarnings("all")
    public static void initializeLists(YamlConfiguration file, Player player, Inventory inventory) {

        HashMap<String, ArrayList<Integer>> slots = new HashMap<>();

        if (file.contains("inventory")) {
            ArrayList<String> schematic = (ArrayList<String>) file.getStringList("inventory");

            for (int i = 0; i < schematic.size(); i++) {
                for (int j = 0; j < schematic.get(0).length(); j++) {
                    String c = schematic.get(i).charAt(j) + "";

                    if (file.contains(c + ".list")) {

                        ArrayList<Integer> slots_list = new ArrayList<>();

                        if (slots.containsKey(c)) {
                            slots_list = slots.get(c);
                        }

                        slots_list.add(9 * i + j);
                        slots.put(c, slots_list);
                    }
                }
            }

            ArrayList<List> list_l = new ArrayList<>();

            for (String key : slots.keySet()) {

                String s = file.getString(key + ".list");
                String[] split = s.split("=");

                ArrayList<HashMap<String, Object>> values = callListMethod(file, player, inventory, split[0], split[1]);

                List l = new List(player, inventory, split[0], slots.get(key), key, file, values);
                list_l.add(l);
            }

            lists.put(player.getUniqueId(), list_l);
        }
    }

    public static ArrayList<HashMap<String, Object>> callListMethod(YamlConfiguration file, Player player, Inventory inventory, String method, String args) {

        if (file.contains("addons")) {
            ArrayList<String> addons = (ArrayList<String>) file.getStringList("addons");
            for (String addon_f : addons) {
                String[] split = addon_f.split(" == ");

                split[1] = split[1].replace(" ", "");

                if (!method.startsWith(split[1] + ".")) {
                    String addon_name = "InventoryBuilderAPI";
                    Addon addon = AddonManager.addons.get(addon_name);
                    if (addon != null) {
                        args = DataManager.replaceData(player, args);
                        return addon.getCustomList(player, inventory, method, args);
                    }
                }
                else {
                    String addon_name = split[0];

                    Addon addon = AddonManager.addons.get(addon_name);
                    String[] split1 = method.split(split[1] + ".");
                    if (addon != null) {
                        args = DataManager.replaceData(player, args);
                        return addon.getCustomList(player, inventory, split1[1], args);
                    }
                }
            }
        }
        else {
            String addon_name = "InventoryBuilderAPI";
            Addon addon = AddonManager.addons.get(addon_name);

            if (addon != null) {
                args = DataManager.replaceData(player, args);
                return addon.getCustomList(player, inventory, method, args);
            }
        }

        return null;
    }

    public static String parseListValue(String s, List list, Integer slot) {

        if (list == null) {
            return s;
        }

        ArrayList<Integer> slots = list.getSlots();
        int index = slots.indexOf(slot);

        ArrayList<HashMap<String, Object>> values = list.getValues();

        if (index >= values.size()) {
            return s;
        }
        else {

            HashMap<String, Object> v = values.get(index);

            for (String key : v.keySet()) {
                if (s.contains("{item." + key + "}")) {
                    s = s.replace("{item." + key + "}", "" + v.get(key));
                }
            }
        }

        return s;
    }

    public static List getList(Player player, String id) {

        if (!lists.containsKey(player.getUniqueId())) {
            return null;
        }

        ArrayList<List> l = lists.get(player.getUniqueId());

        for (List list : l) {

            if (Objects.equals(list.getId(), id.replace(".for-each", ""))) {
                return list;
            }
        }

        return null;
    }
}
