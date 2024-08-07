package fr.as2treffle.inventorybuilderapi.itemstack;

import fr.as2treffle.inventorybuilderapi.manager.*;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ItemStackBuilder {

    @SuppressWarnings("all")
    public static ItemStack buildItemStack(YamlConfiguration inventory_file, String name, Player player) {
        ArrayList<Object> errors = ErrorManager.checkItemStack(inventory_file, name, player);

        if (!errors.isEmpty()) {
            ErrorManager.sendErrorMessageToPlayer(player, errors);
            return buildErrorItemStack();
        }

        if (inventory_file.contains(name + ".animation")) {
            return new ItemStack(Material.AIR);
        }

        String amount_s = inventory_file.getString(name + ".amount");
        int amount = Integer.parseInt(DataManager.replaceData(player, amount_s));

        String type = inventory_file.getString(name + ".type");
        type = DataManager.replaceData(player, type);

        ItemStack stack = new ItemStack(Material.valueOf(type.toUpperCase()), amount);

        ItemMeta meta = stack.getItemMeta();

        if (inventory_file.contains(name + ".attributes")) {

            if (inventory_file.contains(name + ".attributes.leather-color")) {
                if (stack.getType().toString().startsWith("LEATHER_")) {
                    meta = (LeatherArmorMeta) stack.getItemMeta();

                    String color = inventory_file.getString(name + ".attributes.leather-color").toUpperCase();
                    color = DataManager.replaceData(player, color);

                    if (ErrorManager.isAnColor(color)) {
                        ((LeatherArmorMeta) meta).setColor(getColor(color));
                    }
                    else {
                        String[] rgb = color.split(", ");
                        ((LeatherArmorMeta) meta).setColor(Color.fromRGB(new Integer(rgb[0]), new Integer(rgb[1]), new Integer(rgb[2])));
                    }

                    meta.addItemFlags(ItemFlag.HIDE_DYE);
                }
            }

            if (inventory_file.contains(name + ".attributes.head-texture")) {
                if (stack.getType() == Material.PLAYER_HEAD) {
                    meta = (SkullMeta) stack.getItemMeta();
                    ItemStack head = new ItemStack(Material.PLAYER_HEAD);

                    String texture = inventory_file.getString(name + ".attributes.head-texture");

                    if (texture.equals("%player%")) {
                        ((SkullMeta) meta).setOwningPlayer(player);
                    }
                    else {
                        meta = HeadsManager.applyTextureOnPlayerHead(inventory_file, texture);
                    }

                    stack.setItemMeta(meta);
                    stack = head;
                }
            }
        }

        if (inventory_file.contains(name + ".name")) {
            String display_name = PlaceholderAPI.setPlaceholders(player, inventory_file.getString(name + ".name"));
            display_name = DataManager.replaceData(player, display_name);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display_name));
        }

        if (inventory_file.contains(name + ".lores")) {
            ArrayList<String> lores = new ArrayList<>();

            for (String s : inventory_file.getStringList(name + ".lores")) {
                String lore = PlaceholderAPI.setPlaceholders(player, s);
                lore = DataManager.replaceData(player, lore);
                lores.add(ChatColor.translateAlternateColorCodes('&', lore));
            }
            meta.setLore(lores);
        }

        if (inventory_file.contains(name + ".enchants")) {

            for (String s : inventory_file.getStringList(name + ".enchants")) {
                String[] enchant_split = s.split(" ");

                if (enchant_split.length == 2) {

                    enchant_split[0] = DataManager.replaceData(player, enchant_split[0]);

                    if (ErrorManager.isAnEnchantment(enchant_split[0].toUpperCase().replace(' ', '_'))) {
                        meta.addEnchant(Enchantment.getByName(enchant_split[0].toUpperCase()), Integer.parseInt(enchant_split[1]), true);
                    }
                }
            }
        }

        if (inventory_file.contains(name + ".itemflags")) {

            for (String s : inventory_file.getStringList(name + ".itemflags")) {

                s = DataManager.replaceData(player, s);

                if (ErrorManager.isAnItemFlag(s.toUpperCase().replace(' ', '_'))) {
                    meta.addItemFlags(ItemFlag.valueOf(s.toUpperCase()));
                }
            }
        }

        stack.setItemMeta(meta);

        return stack;
    }

    @SuppressWarnings("all")
    public static ItemStack modifyItemStack(YamlConfiguration inventory_file, String name, Player player, ItemStack stack) {

        if (stack == null) {
            stack = new ItemStack(Material.STONE);
        }

        ItemMeta meta = stack.getItemMeta();

        if (inventory_file.contains(name + ".attributes")) {

            if (inventory_file.contains(name + ".attributes.leather-color")) {
                if (stack.getType().toString().startsWith("LEATHER_")) {
                    meta = (LeatherArmorMeta) stack.getItemMeta();

                    String color = inventory_file.getString(name + ".attributes.leather-color").toUpperCase();
                    color = DataManager.replaceData(player, color);

                    if (ErrorManager.isAnColor(color)) {
                        ((LeatherArmorMeta) meta).setColor(getColor(color));
                    }
                    else {
                        String[] rgb = color.split(", ");
                        ((LeatherArmorMeta) meta).setColor(Color.fromRGB(new Integer(rgb[0]), new Integer(rgb[1]), new Integer(rgb[2])));
                    }

                    meta.addItemFlags(ItemFlag.HIDE_DYE);
                }
            }

            if (inventory_file.contains(name + ".attributes.head-texture")) {
                if (stack.getType() == Material.PLAYER_HEAD) {
                    meta = (SkullMeta) stack.getItemMeta();

                    String texture = inventory_file.getString(name + ".attributes.head-texture");

                    if (texture.equals("%player%")) {
                        ((SkullMeta) meta).setOwningPlayer(player);
                    }

                    stack.setItemMeta(meta);
                    player.playNote(player.getLocation(), Instrument.BIT, Note.flat(1, Note.Tone.A));
                }
            }
        }

        if (inventory_file.contains(name + ".name")) {
            String display_name = PlaceholderAPI.setPlaceholders(player, inventory_file.getString(name + ".name"));
            display_name = DataManager.replaceData(player, display_name);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display_name));
        }

        if (inventory_file.contains(name + ".type")) {
            String type = inventory_file.getString(name + ".type").toUpperCase();
            type = DataManager.replaceData(player, type);

            if (ErrorManager.isAnMaterial(type)) {
                stack.setType(Material.valueOf(type));
            }
        }

        if (inventory_file.contains(name + ".amount")) {
            Integer amount = inventory_file.getInt(name + ".amount");

            amount = Integer.parseInt(DataManager.replaceData(player, "" + amount));

            stack.setAmount(amount);

        }

        if (inventory_file.contains(name + ".lores")) {
            ArrayList<String> lores = new ArrayList<>();

            for (String s : inventory_file.getStringList(name + ".lores")) {
                String lore = PlaceholderAPI.setPlaceholders(player, s);
                lore = DataManager.replaceData(player, lore);
                lores.add(ChatColor.translateAlternateColorCodes('&', lore));
            }
            meta.setLore(lores);
        }

        if (inventory_file.contains(name + ".enchants")) {

            for (String s : inventory_file.getStringList(name + ".enchants")) {
                String[] enchant_split = s.split(" ");

                if (enchant_split.length == 2) {

                    enchant_split[0] = DataManager.replaceData(player, enchant_split[0]);

                    if (ErrorManager.isAnEnchantment(enchant_split[0].toUpperCase().replace(' ', '_'))) {
                        meta.addEnchant(Enchantment.getByName(enchant_split[0].toUpperCase()), Integer.parseInt(enchant_split[1]), true);
                    }
                }
            }
        }

        if (inventory_file.contains(name + ".itemflags")) {

            for (String s : inventory_file.getStringList(name + ".itemflags")) {

                s = DataManager.replaceData(player, s);

                if (ErrorManager.isAnItemFlag(s.toUpperCase().replace(' ', '_'))) {
                    meta.addItemFlags(ItemFlag.valueOf(s.toUpperCase()));
                }
            }
        }

        stack.setItemMeta(meta);

        return stack;
    }

    @SuppressWarnings("all")
    public static ItemStack buildListItem(YamlConfiguration file, Player player, Integer slot, String id) {

        if (!ListManager.lists.containsKey(player.getUniqueId())) {
            return null;
        }

        ArrayList<List> L_list = ListManager.lists.get(player.getUniqueId());

        for (List list : L_list) {
            if (Objects.equals(list.getId(), id)) {
                ArrayList<HashMap<String, Object>> values = list.getValues();
                ArrayList<Integer> slots = list.getSlots();

                if (file.contains(id + ".for-each.from")) {
                    if (Objects.equals(file.getString(id + ".for-each.from"), "{item}")) {
                        int index = slots.indexOf(slot);

                        if (index < values.size()) {
                            return (ItemStack) values.get(index).get("item");
                        }
                        else {
                            return new ItemStack(Material.AIR);
                        }

                    }
                }
                else {
                    return buildListItemStack(file, player, id + ".for-each", list, slot);
                }
            }
        }

        return buildErrorItemStack();
    }

    @SuppressWarnings("all")
    public static ItemStack buildListItemStack(YamlConfiguration inventory_file, Player player, String id, List list, Integer slot) {

        String amount_s = inventory_file.getString(id + ".amount");
        amount_s = ListManager.parseListValue(amount_s, list, slot);
        int amount = Integer.parseInt(DataManager.replaceData(player, amount_s));

        String type = inventory_file.getString(id + ".type");
        type = ListManager.parseListValue(type, list, slot);
        type = DataManager.replaceData(player, type);

        if (!ErrorManager.isAnMaterial(type.toUpperCase())) {
            return new ItemStack(Material.AIR);
        }

        ItemStack stack = new ItemStack(Material.valueOf(type.toUpperCase()), amount);

        ItemMeta meta = stack.getItemMeta();

        if (inventory_file.contains(id + ".attributes")) {

            if (inventory_file.contains(id + ".attributes.leather-color")) {
                if (stack.getType().toString().startsWith("LEATHER_")) {
                    meta = (LeatherArmorMeta) stack.getItemMeta();

                    String color = inventory_file.getString(id + ".attributes.leather-color").toUpperCase();
                    color = ListManager.parseListValue(color, list, slot);
                    color = DataManager.replaceData(player, color);

                    if (ErrorManager.isAnColor(color)) {
                        ((LeatherArmorMeta) meta).setColor(getColor(color));
                    }
                    else {
                        String[] rgb = color.split(", ");
                        ((LeatherArmorMeta) meta).setColor(Color.fromRGB(new Integer(rgb[0]), new Integer(rgb[1]), new Integer(rgb[2])));
                    }

                    meta.addItemFlags(ItemFlag.HIDE_DYE);
                }
            }

            if (inventory_file.contains(id + ".attributes.head-texture")) {
                if (stack.getType() == Material.PLAYER_HEAD) {
                    meta = (SkullMeta) stack.getItemMeta();
                    ItemStack head = new ItemStack(Material.PLAYER_HEAD);

                    String texture = inventory_file.getString(id + ".attributes.head-texture");

                    if (texture.equals("%player%")) {
                        ((SkullMeta) meta).setOwningPlayer(player);
                    }
                    else {
                        meta = HeadsManager.applyTextureOnPlayerHead(inventory_file, texture);
                    }

                    stack.setItemMeta(meta);
                    stack = head;
                }
            }
        }

        if (inventory_file.contains(id + ".name")) {
            String display_name = PlaceholderAPI.setPlaceholders(player, inventory_file.getString(id + ".name"));
            display_name = ListManager.parseListValue(display_name, list, slot);
            display_name = DataManager.replaceData(player, display_name);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', display_name));
        }

        if (inventory_file.contains(id + ".lores")) {
            ArrayList<String> lores = new ArrayList<>();

            for (String s : inventory_file.getStringList(id + ".lores")) {
                String lore = PlaceholderAPI.setPlaceholders(player, s);
                lore = ListManager.parseListValue(lore, list, slot);
                lore = DataManager.replaceData(player, lore);
                lores.add(ChatColor.translateAlternateColorCodes('&', lore));
            }
            meta.setLore(lores);
        }

        if (inventory_file.contains(id + ".enchants")) {

            for (String s : inventory_file.getStringList(id + ".enchants")) {
                String[] enchant_split = s.split(" ");

                if (enchant_split.length == 2) {

                    enchant_split[0] = ListManager.parseListValue(enchant_split[0], list, slot);
                    enchant_split[0] = DataManager.replaceData(player, enchant_split[0]);

                    if (ErrorManager.isAnEnchantment(enchant_split[0].toUpperCase().replace(' ', '_'))) {
                        meta.addEnchant(Enchantment.getByName(enchant_split[0].toUpperCase()), Integer.parseInt(enchant_split[1]), true);
                    }
                }
            }
        }

        if (inventory_file.contains(id + ".itemflags")) {

            for (String s : inventory_file.getStringList(id + ".itemflags")) {

                s = ListManager.parseListValue(s, list, slot);
                s = DataManager.replaceData(player, s);

                if (ErrorManager.isAnItemFlag(s.toUpperCase().replace(' ', '_'))) {
                    meta.addItemFlags(ItemFlag.valueOf(s.toUpperCase()));
                }
            }
        }

        stack.setItemMeta(meta);

        return stack;
    }

    public static ItemStack buildErrorItemStack() {
        ItemStack stack = new ItemStack(Material.BARRIER);
        ItemMeta meta = stack.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§c§lERROR");
            ArrayList<String> lores = new ArrayList<>();
            lores.add("§c Error(s) found, please check yaml file !");
            meta.setLore(lores);
            stack.setItemMeta(meta);
        }

        return stack;
    }

    public static Color getColor(String s) {
        switch (s) {
            case "AQUA":
                return Color.AQUA;
            case "BLACK":
                return Color.BLACK;
            case "BLUE":
                return Color.BLUE;
            case "FUCHSIA":
                return Color.FUCHSIA;
            case "GRAY":
                return Color.GRAY;
            case "GREEN":
                return Color.GREEN;
            case "LIME":
                return Color.LIME;
            case "MAROON":
                return Color.MAROON;
            case "NAVY":
                return Color.NAVY;
            case "OLIVE":
                return Color.OLIVE;
            case "ORANGE":
                return Color.ORANGE;
            case "PURPLE":
                return Color.PURPLE;
            case "RED":
                return Color.RED;
            case "SILVER":
                return Color.SILVER;
            case "TEAL":
                return Color.TEAL;
            case "WHITE":
                return Color.WHITE;
            case "YELLOW":
                return Color.YELLOW;
        }

        return Color.WHITE;
    }
}
