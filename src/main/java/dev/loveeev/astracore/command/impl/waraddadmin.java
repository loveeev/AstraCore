package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class waraddadmin implements CommandExecutor {

    PreparedStatement ps;
    ResultSet rs;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        String nation = args[1];
        String gamer = args[2];
        if(args[0].equalsIgnoreCase("Снять-Военного")) {
            try {
                ps = MySQL.getInstance().getCon().prepareStatement("SELECT * FROM `TOWNY_NATIONS` WHERE `name` = ?"); // Обязательно нужно делать с ?, на строчке ниже ты передаешь аргументы которые над вставить
                ps.setString(1, nation);
                rs = ps.executeQuery();
                if (!rs.next()) { // rs.next() - тут проверяется есть ли результат такой в БД
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Ошибка базы данных, обратитесь за помощью к администратору.");
                } else {
                    Resident resident = TownyAPI.getInstance().getResident(gamer);
                    String soldier = "soldier";
                        int limit = rs.getInt("soldiers");
                        if (resident.hasNationRank(soldier)) {
                            try {
                                int sol = limit - 1;
                                String updateQuery = "UPDATE TOWNY_NATIONS SET soldiers = " + sol + " WHERE name = ?";
                                PreparedStatement statement = MySQL.getInstance().getCon().prepareStatement(updateQuery);
                                statement.setString(1, nation);
                                statement.executeUpdate();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            resident.removeNationRank(soldier);
                            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Игрок " + gamer + " больше не военный в нации " + nation);
                        } else {
                            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Игрок не военный.");
                        }
                    }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(args[0].equalsIgnoreCase("Назначить-военного")){
            try {
                ps = MySQL.getInstance().getCon().prepareStatement("SELECT * FROM `TOWNY_NATIONS` WHERE `name` = ?"); // Обязательно нужно делать с ?, на строчке ниже ты передаешь аргументы которые над вставить
                ps.setString(1, nation);
                rs = ps.executeQuery();
                if (!rs.next()) { // rs.next() - тут проверяется есть ли результат такой в БД
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Ошибка базы данных, обратитесь за помощью к администратору.");
                } else {
                    Resident resident = TownyAPI.getInstance().getResident(gamer);
                    String soldier = "soldier";
                        int limit = rs.getInt("soldiers");
                        int l = rs.getInt("limits");
                        if (!resident.hasNationRank(soldier)) {
                            if(l > limit) {
                                try {
                                    int sol = limit + 1;
                                    String updateQuery = "UPDATE TOWNY_NATIONS SET soldiers = " + sol + " WHERE name = ?";
                                    PreparedStatement statement = MySQL.getInstance().getCon().prepareStatement(updateQuery);
                                    statement.setString(1, nation);
                                    statement.executeUpdate();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                resident.addNationRank(soldier);
                                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Игрок " + player.getName() + " теперь военный в нации " + nation);
                            }else {
                                Main.getInstance().getChatUtility().sendSuccessNotification(player, "У этой нации лимит военных");
                            }
                        } else {
                            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Игрок не военный.");
                        }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
