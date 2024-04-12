package dev.loveeev.astracore.database;

import org.mineacademy.fo.Common;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RaftMenuManage {

    public static final String TABLE_NAME = "RaftMenu";

    public static void createTable() {
        try {
            PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                    "nationallow TEXT NOT NULL, " +
                    "nation TEXT NOT NULL, id INT NULL)"
            );
            st.executeUpdate();
        } catch (SQLException e) {
            Common.error(e, "Failed to create database table Raft");
        }
    }
}
