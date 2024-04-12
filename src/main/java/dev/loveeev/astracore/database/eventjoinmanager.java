package dev.loveeev.astracore.database;

import org.mineacademy.fo.Common;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class eventjoinmanager {

    public static final String TABLE_NAME = "eventjoin";

    public static void createTable() {
        try {
            PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                    "dud TEXT NULL, st INT NULL)"
            );
            st.executeUpdate();
        } catch (SQLException e) {
            Common.error(e, "Failed to create database table event");
        }
    }
}
