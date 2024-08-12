package fr.as2treffle.inventorybuilderapi.manager;

import fr.as2treffle.inventorybuilderapi.utils.Action;
import fr.as2treffle.inventorybuilderapi.utils.Condition;
import fr.as2treffle.inventorybuilderapi.utils.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Addon {

    void performAction(Action action);

    boolean checkCondition(Condition condition);

    String getPlaceholder(Placeholder placeholder);

    ArrayList<HashMap<String, Object>> getList(fr.as2treffle.inventorybuilderapi.utils.List list);

    List<String> getActions();

    List<String> getConditions();

    List<String> getPlaceholders();

    List<String> getLists();
}
