package dev.loveeev.astracore.database;

import org.mineacademy.fo.Common;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class eventdatabaseManager {

    public static final String TABLE_NAME = "event";

    public static void createTable() {
        try {
            MySQL mySQL = MySQL.getInstance(); // Убедитесь, что у вас есть корректная реализация
            PreparedStatement st = mySQL.getCon().prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username TEXT NOT NULL, " +
                    "town TEXT NOT NULL)"
            );
            st.executeUpdate();
        } catch (SQLException e) {
            Common.error(e, "Failed to create database table event");
        }
    }
}
