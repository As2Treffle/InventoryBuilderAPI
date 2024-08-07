package fr.as2treffle.inventorybuilderapi.manager;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class DataManager {

    public static HashMap<UUID, ArrayList<Data>> data = new HashMap<>();

    @SuppressWarnings("all")
    public static void initializeData(Player player, YamlConfiguration config) {

        if (config.contains("data")) {
            Set<String> ids = config.getConfigurationSection("data").getKeys(false);

            ArrayList<Data> datas = new ArrayList<>();

            for (String id : ids) {
                DataType type = DataType.valueOf(config.getString("data." + id + ".type").toUpperCase());
                Object value = null;

                if (type == DataType.STRING) {
                    value = config.getString("data." + id + ".default");
                    value = PlaceholderAPI.setPlaceholders(player, (String)value);

                }
                else if (type == DataType.DOUBLE) {
                    value = config.getDouble("data." + id + ".default");
                }
                else if (type == DataType.INTEGER) {
                    value = config.getInt("data." + id + ".default");
                }

                datas.add(new Data(id, type, value));
            }

            data.put(player.getUniqueId(), datas);
        }
    }

    @SuppressWarnings("all")
    public static String replaceData(Player player, String s) {

        if (!data.containsKey(player.getUniqueId())) {
            return s;
        }

        ArrayList<Data> datas = data.get(player.getUniqueId());

        for (Data data : datas) {
            if (s.contains("[data." + data.getId() + "]")) {
                s = s.replace("[data." + data.getId() + "]", "" + data.getValue());
            }
        }

        return s;
    }

    @SuppressWarnings("all")
    public static void setNewValue(Player player, String id, String value) {

        if (!data.containsKey(player.getUniqueId())) {
            return;
        }

        ArrayList<Data> datas = data.get(player.getUniqueId());

        for (Data data : datas) {
            if (id.equals(data.getId())) {

                if (data.getType() == DataType.STRING) {
                    data.setValue(value);
                }
                else if (data.getType() == DataType.DOUBLE) {
                    data.setValue(Double.valueOf(value));
                }
                else if (data.getType() == DataType.INTEGER) {
                    data.setValue(Integer.valueOf(value));
                }
            }
        }

        data.put(player.getUniqueId(), datas);
    }

    public static void performOperation(Player player, String id, String operation, String value_s) {

        Object value = value_s;

        if (!data.containsKey(player.getUniqueId())) {
            return;
        }

        ArrayList<Data> datas = data.get(player.getUniqueId());

        for (Data data : datas) {
            if (id.equals(data.getId())) {
                if (data.getType() == DataType.STRING) {
                    return;
                }
                else if (data.getType() == DataType.DOUBLE) {
                    value = Double.valueOf(value_s);

                    if (operation.equals("+")) {
                        data.setValue((Double)data.getValue() + (Double)value);
                    }
                    else if (operation.equals("-")) {
                        data.setValue((Double)data.getValue() - (Double)value);
                    }
                    else if (operation.equals("*")) {
                        data.setValue((Double)data.getValue() * (Double)value);
                    }
                    else if (operation.equals("/")) {
                        data.setValue((Double)data.getValue() / (Double)value);
                    }
                }
                else if (data.getType() == DataType.INTEGER) {
                    value = Integer.valueOf(value_s);

                    if (operation.equals("+")) {
                        data.setValue((Integer)data.getValue() + (Integer)value);
                    }
                    else if (operation.equals("-")) {
                        data.setValue((Integer)data.getValue() - (Integer)value);
                    }
                    else if (operation.equals("*")) {
                        data.setValue((Integer)data.getValue() * (Integer)value);
                    }
                    else if (operation.equals("/")) {
                        data.setValue((Integer)data.getValue() / (Integer)value);
                    }
                }
            }
        }
    }

    public static boolean canBeInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean canBeDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
