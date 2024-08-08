package fr.as2treffle.inventorybuilderapi.manager;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Addon {

    void performAction(Player player, Inventory inventory, ClickType clickType, ItemStack itemStack, String action_name, String args, Integer slot);

    boolean checkCondition(Player player, Inventory inventory, String condition, String args, Integer slot);

    ItemStack getCustomItemStack(Player player, Inventory inventory, ClickType clickType, ItemStack itemStack, String method, String args);

    ArrayList<HashMap<String, Object>> getCustomList(Player player, Inventory inventory, String method, String args);

    List<String> getActions();

    List<String> getConditions();

    List<String> getItemStackMethods();

    List<String> getListMethods();
}
