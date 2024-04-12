package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WarProcGive implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) commandSender;
        if(args.length < 1){
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Используйте /warproc set/info ....");
            return true;
        }

        if(args[0].equalsIgnoreCase("info")) {
            if (args.length < 2) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /warproc info <город>");
                return true;
            }
            String o = args[1];
            Town town = TownyAPI.getInstance().getTown(o);
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),() -> {
            if(town != null){
                try {
                    ResultSet resultSet = MySQL.getInstance().executeQuery("SELECT * FROM `TOWNY_TOWNS` WHERE name = ? ", town.getName());
                    Integer integer = resultSet.getInt("WarProc");

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                Main.getInstance().getChatUtility().sendSuccessNotification(player,"Такого город не существует.");
            }
        });
        }
        if(args[0].equalsIgnoreCase("set")) {
            if (args.length < 3) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /warproc set <город> <процент>");
                return true;
            }
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                String o = args[1];
                Town town = TownyAPI.getInstance().getTown(o);
                if (town != null) {
                    int p = Integer.parseInt(args[2]);
                    if (p <= 150) {
                        MySQL.getInstance().executeUpdate("UPDATE `TOWNY_TOWNS` SET WarProc = ? WHERE name = ?", p, town.getName());
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Теперь у военного завода города &#ffdaa4" + town.getName() + " &fпроцент производства равен &#ffdaa4" + p);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Слишком много, укажите меньше 150");
                    }
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Такого город не существует.");
                }

            });
        }
        if(args[0].equalsIgnoreCase("nationset")){
            if (args.length < 3) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /warproc set <нация> <процент>");
                return true;
            }
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),() -> {
            String o = args[1];
            Nation nation = TownyAPI.getInstance().getNation(o);
            Integer p = Integer.parseInt(args[2]);
            if(nation != null){
                Connection connection = MySQL.getInstance().getCon();
                PreparedStatement statement = null;
                try {
                    statement = connection.prepareStatement("SELECT * FROM TOWNY_TOWNS WHERE nation = ?");
                    statement.setString(1, nation.getName());

                    ResultSet rs = statement.executeQuery();

                    while (rs.next()) {
                        String townName = rs.getString("name");
                        Town town = TownyAPI.getInstance().getTown(townName);
                        if(town != null){
                            MySQL.getInstance().executeUpdate("UPDATE `TOWNY_TOWNS` SET WarProc = ? WHERE name = ?",p,town.getName());
                            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Успешно!");
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Такой нации не существует.");
            }
        });
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length < 1){
            return getPartialMatches(args[0],List.of("info","nationset","set"));
        }
        if(args.length < 2) {
            if (args[0].equalsIgnoreCase("set") | args[0].equalsIgnoreCase("info")) {
                return getPartialMatches(args[1], TownyAPI.getInstance().getTowns().stream().map(town -> town.getName()).collect(Collectors.toList()));
            }else {
                return getPartialMatches(args[1],TownyAPI.getInstance().getNations().stream().map(nation -> nation.getName()).collect(Collectors.toList()));
            }
        }
        return new ArrayList<>();
    }
    private List<String> getPartialMatches(String arg, List<String> options) {
        return options.stream().filter(option -> option.startsWith(arg)).collect(Collectors.toList());
    }
}
