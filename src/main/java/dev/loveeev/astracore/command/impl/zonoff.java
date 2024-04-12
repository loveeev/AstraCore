package dev.loveeev.astracore.command.impl;

import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class zonoff implements TabExecutor {
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        String town = "loveeev";
        if(strings[0].equalsIgnoreCase("off")){
            try {
                // создайте PreparedStatement без try-with-resources
                PreparedStatement statement = MySQL.getInstance().getCon().prepareStatement("UPDATE `TOWNY_RESIDENTS` SET provin = 1 WHERE name = ?");
                statement.setString(1, town);
                // выполнение запроса
                statement.executeUpdate();
                // не забывайте закрыть PreparedStatement после использования
                statement.close();

                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы отключили плагин на провинции.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(strings[0].equalsIgnoreCase("on")) {
            try {
                // создайте PreparedStatement без try-with-resources
                PreparedStatement statement = MySQL.getInstance().getCon().prepareStatement("UPDATE `TOWNY_RESIDENTS` SET provin = 0 WHERE name = ?");
                statement.setString(1, town);
                // выполнение запроса
                statement.executeUpdate();
                // не забывайте закрыть PreparedStatement после использования
                statement.close();

                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы включили плагин на провинции.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 0){
            return getPartialMatches(args[0], List.of("off","on"));
        }
        return new ArrayList<>();
    }
    private List<String> getPartialMatches(String arg, List<String> options) {
        return options.stream().filter(option -> option.startsWith(arg)).collect(Collectors.toList());
    }
}
