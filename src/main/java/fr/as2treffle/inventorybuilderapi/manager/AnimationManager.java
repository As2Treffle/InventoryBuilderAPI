package fr.as2treffle.inventorybuilderapi.manager;

import fr.as2treffle.inventorybuilderapi.InventoryBuilderAPI;
import fr.as2treffle.inventorybuilderapi.inventory.InventoryBuilder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class AnimationManager {

    public static HashMap<UUID, ArrayList<Animation>> animations = new HashMap<>();

    public static void createAnimations(YamlConfiguration config, Player player) {

        if (config.contains("animations")) {

            HashMap<Integer, String> symbols = InventoryBuilder.items.get(player.getUniqueId());
            ArrayList<Animation> animations_list = new ArrayList<>();

            for (Integer slot : symbols.keySet()) {
                String symbol = symbols.get(slot);

                if (config.contains(symbol + ".animation.id")) {
                    String id = config.getString(symbol + ".animation.id");

                    if (config.contains("animations." + id)) {

                        Set<String> frames_file = Objects.requireNonNull(config.getConfigurationSection("animations." + id)).getKeys(false);
                        Integer nb_frames = frames_file.size();
                        Integer speed = config.getInt(symbol + ".animation.speed");
                        ItemStack stack = player.getOpenInventory().getTopInventory().getItem(slot);

                        Animation anim = new Animation(config, id, nb_frames, speed, player, slot, stack);
                        animations_list.add(anim);
                    }
                }
            }
            animations.put(player.getUniqueId(), animations_list);
        }
    }

    public static void playAnimations(UUID uuid) {

        ArrayList<Animation> animations_list = animations.get(uuid);

        if (animations_list != null) {
            for (Animation animation : animations_list) {
                Integer speed = animation.getSpeed();
                animation.runTaskTimer(InventoryBuilderAPI.instance, 0, speed);
            }
        }
    }

    public static void stopAnimations(UUID uuid) {
        ArrayList<Animation> animations_list = animations.get(uuid);

        if (animations_list != null) {
            for (Animation animation : animations_list) {
                animation.cancel();
            }
        }

        animations.remove(uuid);
    }
}
