package dev.loveeev.astracore.database;

import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostOfficeDatabaseManager {

    public PostOfficeDatabaseManager() {
    }
    public static final String TABLE_NAME = "PostOfficeBase";

    public static void createTable() {
        try {
            PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                    "player VARCHAR(255) NOT NULL, " +
                    "amount INT NULL," +
                    "data VARCHAR(255) NULL)"
            );
            st.executeUpdate();
        } catch (SQLException e) {
            Common.error(e, "Failed to create database table Raft");
        }
    }

    public void addMessage(String player, Integer amount, String data) {
        MySQL.getInstance().executeUpdate("INSERT INTO `PostOfficeBase` (`player`,`amount`,`data`) VALUES (?, ?, ?)", player, amount, data);
    }

    public void removeMessage(String player,Integer amount,String data){
        MySQL.getInstance().executeUpdate("DELETE FROM `PostOfficeBase` WHERE `player` = ? AND `amount` = ? AND data = ?",player,amount,data);
    }


    public int getColumnCountForPlayer(Player player) {
        try {
            Connection connection = MySQL.getInstance().getCon();
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS column_count FROM `PostOfficeBase` WHERE player = ?");
            statement.setString(1, player.getName());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("column_count");
            } else {
                return 0; // Возвращаем 0, если не было результатов
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String getdata(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return currentDateTime.format(formatter);
    }
}
