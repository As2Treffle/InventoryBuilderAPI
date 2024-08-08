package fr.as2treffle.inventorybuilderapi.commands;

import fr.as2treffle.inventorybuilderapi.InventoryBuilderAPI;
import fr.as2treffle.inventorybuilderapi.manager.AddonManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class IBCommandTabCompleter implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        ArrayList<String> tab = new ArrayList<>();
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (args.length == 1) {
                if (player.hasPermission("inventorybuilderapi.command.view")) {
                    tab.add("view");
                }

                if (player.hasPermission("inventorybuilderapi.command.addons")) {
                    tab.add("addons");
                    tab.add("help");
                }
            }
            else if (args.length == 2) {
                if (player.hasPermission("inventorybuilderapi.command.view")) {
                    if (args[0].equals("view")) {
                        tab.addAll(getInventoryFiles());
                    }
                }

                if (player.hasPermission("inventorybuilderapi.command.addons")) {
                    if (args[0].equals("help")) {
                        tab.addAll((Collection<? extends String>) AddonManager.addons.keySet());
                    }
                }
            }
        }

        return tab;
    }

    @SuppressWarnings("all")
    private ArrayList<String> getInventoryFiles() {
        ArrayList<String> files = new ArrayList<>();
        Path path = InventoryBuilderAPI.instance.getDataFolder().toPath();

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".yml")) {
                        if (!file.getParent().getFileName().toString().equals("InventoryBuilderAPI")) {
                            files.add(file.getParent().getFileName().toString() + "/" + file.getFileName().toString());
                        }
                        else {
                            files.add(file.getFileName().toString());
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }
}
