package dev.loveeev.astracore.database;

import java.sql.*;

public class nationdatabaseManager {

    public static void createColumns() {
        createColumn("warlimit");
        createColumn("bcamount");
        createColumn("farmamount");
        createColumn("bmamount");
        createColumn("bankamount");
        createColumn("stamount");
        createColumn("waramount");
        createColumn("portamount");
        createColumn("limits");
        createColumn("soldiers");
        createColumn("fuelamount");
        createColumn("fuelcraftamount");
        createColumn("trainamount");
        createColumn("raftamount");
        createColumn("tankamount");
        createColumn("planeamount");
        createColumn("drillamount");
        createColumn("caramount");
        createColumn("stazavodamount");
        createColumn("staproduction");
        createColumn("setnation");
        createColumn("setnationc");
        createColumn("setnationl");
        createColumn("fabricamount");
        createColumn("setnationbkomand");
        createColumn("setnationlkomand");
        createColumn("setnationckomand");
        createColumn("setnationkomand");
        createColumn("MoneyYardAmount");
        createColumn("CapProcent");
    }

    private static void createColumn(String columnName) {
        MySQL mySQL = MySQL.getInstance();
        try {
            // Получаем соединение с базой данных
            Connection connection = mySQL.getCon();

            // Проверяем, существует ли указанный столбец в таблице TOWNY_NATIONS
            DatabaseMetaData md = connection.getMetaData();
            ResultSet column = md.getColumns(null, null, "TOWNY_NATIONS", columnName);

            if (!column.next()) {
                // Создаем запрос на добавление столбца
                String query = "ALTER TABLE TOWNY_NATIONS ADD COLUMN " + columnName + " INT";

                // Выполняем запрос
                try (PreparedStatement st = connection.prepareStatement(query)) {
                    st.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
