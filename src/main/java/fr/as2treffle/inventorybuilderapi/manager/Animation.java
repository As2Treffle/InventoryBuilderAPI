package fr.as2treffle.inventorybuilderapi.manager;

import fr.as2treffle.inventorybuilderapi.inventory.InventoryBuilder;
import fr.as2treffle.inventorybuilderapi.itemstack.ItemStackBuilder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Animation extends BukkitRunnable {

    private final Integer nb_frames;
    private final String id;
    private final Integer speed;
    private final YamlConfiguration file;
    private final Player player;
    private final Integer slot;
    private Integer current_frame = 0;
    private ItemStack stack;

    public Animation(YamlConfiguration file, String id, Integer nb_frames, Integer speed, Player player, Integer slot, ItemStack itemStack) {
        this.id = id;
        this.file = file;
        this.nb_frames = nb_frames;
        this.speed = speed;
        this.player = player;
        this.slot = slot;
        this.stack = itemStack;
    }

    public Integer getSpeed() {
        return speed;
    }

    public YamlConfiguration getFile() {
        return file;
    }

    public Player getPlayer() {
        return player;
    }


    @Override
    public void run() {

        if (current_frame < nb_frames) {
            ItemStack frame = ItemStackBuilder.modifyItemStack(file, "animations." + id + "." + current_frame, player, stack);
            player.getOpenInventory().getTopInventory().setItem(slot, frame);

            if (file.contains("animations." + id + "." + current_frame + ".when-appears")) {
                ArrayList<String> actions = (ArrayList<String>) file.getStringList("animations." + id + "." + current_frame + ".when-appears");
                ActionsManager.performsActions(file, player, actions, player.getOpenInventory().getTopInventory());
            }

            current_frame++;
        }

        if (current_frame.equals(nb_frames)) {
            current_frame = 0;
        }
    }
}
