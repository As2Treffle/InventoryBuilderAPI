package fr.as2treffle.inventorybuilderapi.manager;

import fr.as2treffle.inventorybuilderapi.utils.Placeholder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholdersManager {

    public static String replacePlaceholders(Player player, YamlConfiguration file, Inventory inventory, String s) {
        Pattern pattern = Pattern.compile("%(.*?)%");
        Matcher matcher = pattern.matcher(s);

        if (!matcher.find()) {
            return s;
        }

        String[] split = matcher.group(1).split("=");
        String method = split[0];
        String args = split[1];

        String result = callPlaceholderMethod(file, player, inventory, method, args);

        return s.replace("%" + matcher.group(1) + "%", result);
    }

    @SuppressWarnings("all")
    public static String callPlaceholderMethod(YamlConfiguration file, Player player, Inventory inventory, String method, String args) {

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

                        Placeholder placeholder = new Placeholder(player, inventory, method, args, file);
                        return addon.getPlaceholder(placeholder);
                    }
                }
                else {
                    String addon_name = split[0];

                    Addon addon = AddonManager.addons.get(addon_name);
                    String[] split1 = method.split(split[1] + ".");
                    if (addon != null) {
                        args = DataManager.replaceData(player, args);

                        Placeholder placeholder = new Placeholder(player, inventory, method, args, file);
                        return addon.getPlaceholder(placeholder);
                    }
                }
            }
        }
        else {
            String addon_name = "InventoryBuilderAPI";
            Addon addon = AddonManager.addons.get(addon_name);

            if (addon != null) {
                args = DataManager.replaceData(player, args);

                Placeholder placeholder = new Placeholder(player, inventory, method, args, file);
                return addon.getPlaceholder(placeholder);
            }
        }

        return "";
    }
}
