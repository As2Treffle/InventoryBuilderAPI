package fr.as2treffle.inventorybuilderapi.manager;

import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;
import com.mojang.authlib.GameProfile;

public class HeadsManager {

    @SuppressWarnings("all")
    public static ItemMeta applyTextureOnPlayerHead(YamlConfiguration file, String id) {

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        if (!file.contains("heads." + id)) {
            return head.getItemMeta();
        }

        String value = file.getString("heads." + id);

        SkullMeta meta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        profile.getProperties().put("textures", new Property("textures", value));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            Bukkit.getLogger().warning("Failed to set base64 skull value!");
        }

        return meta;
    }
}
