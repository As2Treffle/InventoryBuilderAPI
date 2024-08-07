package fr.as2treffle.inventorybuilderapi.manager;

import fr.as2treffle.inventorybuilderapi.InventoryBuilderAPI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class RefreshManager {

    public static HashMap<UUID, ArrayList<Refresh>> refresh = new HashMap<UUID, ArrayList<Refresh>>();
    public static HashMap<UUID, ArrayList<RefreshList>> refresh_of_list = new HashMap<UUID, ArrayList<RefreshList>>();

    @SuppressWarnings("all")
    public static void initializeRefresh(YamlConfiguration file, Player player, Inventory inventory) {

        ArrayList<String> schematic = (ArrayList<String>) file.getStringList("inventory");
        ArrayList<Refresh> refreshes = new ArrayList<>();
        ArrayList<RefreshList> refreshLists = new ArrayList<>();

        for (int i = 0; i < schematic.size(); i++) {
            for (int j = 0; j < schematic.get(0).length(); j++) {
                String c = schematic.get(i).charAt(j) + "";

                if (file.contains(c + ".refresh")) {

                    if (!file.contains(c + ".list")) {
                        Integer speed = file.getInt(c + ".refresh");
                        refreshes.add(new Refresh(c, file, speed, 9 * i + j, player, inventory));
                    }
                }
            }
        }

        if (ListManager.lists.containsKey(player.getUniqueId())) {

            for (List list : ListManager.lists.get(player.getUniqueId())) {
                if (file.contains(list.getId() + ".refresh")) {
                    Integer speed = file.getInt(list.getId() + ".refresh");
                    RefreshList refreshList = new RefreshList(list.getId(), file, speed, list.getSlots(), player, inventory);
                    refreshLists.add(refreshList);
                }
            }
        }

        refresh.put(player.getUniqueId(), refreshes);
        refresh_of_list.put(player.getUniqueId(), refreshLists);
    }

    public static RefreshList getRefreshListObject(Player player, String id) {

        if (!refresh_of_list.containsKey(player.getUniqueId())) {
            return null;
        }

        ArrayList<RefreshList> refreshLists = refresh_of_list.get(player.getUniqueId());

        for (RefreshList refresh : refreshLists) {
            if (Objects.equals(refresh.getId(), id)) {
                return refresh;
            }
        }

        return null;
    }

    public static Refresh getRefreshObject(Player player, String id) {

        if (!refresh.containsKey(player.getUniqueId())) {
            return null;
        }

        ArrayList<Refresh> refreshLists = refresh.get(player.getUniqueId());

        for (Refresh refresh : refreshLists) {
            if (Objects.equals(refresh.getId(), id)) {
                return refresh;
            }
        }

        return null;
    }

    public static void playRefresh(UUID uuid) {

        ArrayList<Refresh> refresh_list = refresh.get(uuid);
        ArrayList<RefreshList> refreshLists = refresh_of_list.get(uuid);

        if (refresh_list != null) {
            for (Refresh refresh : refresh_list) {
                Integer speed = refresh.getSpeed();
                refresh.runTaskTimer(InventoryBuilderAPI.instance, 0, speed);
            }
        }

        if (refreshLists != null) {
            for (RefreshList refresh : refreshLists) {
                Integer speed = refresh.getSpeed();
                refresh.runTaskTimer(InventoryBuilderAPI.instance, 0, speed);
            }
        }
    }

    public static void stopRefresh(UUID uuid) {

        ArrayList<Refresh> refresh_list = refresh.get(uuid);
        ArrayList<RefreshList> refreshLists = refresh_of_list.get(uuid);

        if (refresh_list != null) {
            for (Refresh refresh : refresh_list) {
                refresh.cancel();
            }
        }

        if (refreshLists != null) {
            for (RefreshList refresh : refreshLists) {
                refresh.cancel();
            }
        }

        refresh.remove(uuid);
        refresh_of_list.remove(uuid);
    }

}
