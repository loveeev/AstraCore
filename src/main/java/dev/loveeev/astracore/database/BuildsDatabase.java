package dev.loveeev.astracore.database;

import dev.loveeev.astracore.data.BaseBuilds;
import org.mineacademy.fo.Common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuildsDatabase {


    public static void setBuilds(String townName, String columnname, String columnnameamount, boolean isPremium) {
        Common.runAsync(() -> {
            try {
                PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("UPDATE TOWNY_TOWNS SET "+columnname+" = ? WHERE name = ?");
                st.setBoolean(1, isPremium);
                st.setString(2, townName);
                st.executeUpdate();
                ResultSet resultSet = MySQL.getInstance().executeQuery("SELECT * FROM `TOWNY_NATIONS` WHERE name = ?", townName);
                int i = resultSet.getInt(columnnameamount);
                if (isPremium) {
                    MySQL.getInstance().executeUpdate("UPDATE `TOWNY_NATIONS` SET "+columnnameamount + " = ? WHERE name = ?", i+1, townName);
                } else {
                    MySQL.getInstance().executeUpdate("UPDATE `TOWNY_NATIONS` SET "+columnnameamount + " = ? WHERE name = ?", i-1, townName);
                }
            } catch (SQLException e) {
                Common.error(e, "Failed to update database table builds with premium flag.");
            }
        });
    }
    public static int moenybank(String townname) {
        try {
            BaseBuilds baseBuilds = BaseBuilds.getOrCreate(townname);
            ResultSet resultSet = MySQL.getInstance().executeQuery("SELECT * FROM `TOWNY_TOWNS` WHERE name = ?", townname);

            if (resultSet.next()) { // Перемещаем курсор на первую строку
                int spec = resultSet.getInt("spec");

                if (!baseBuilds.isIspredplus()) {
                    return spec;
                } else {
                    return (int) Math.ceil(spec * 1.5);
                }
            } else {
                throw new SQLException("No data found for town: " + townname);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadBuilddata(){
        try{
            PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("SELECT * FROM TOWNY_TOWNS");
            ResultSet set = st.executeQuery();

            while (set.next()){
                String townName = set.getString("name");
                boolean factorywar = set.getBoolean("cargw");
                boolean carg = set.getBoolean("carg");
                boolean bank = set.getBoolean("bank");
                boolean bc = set.getBoolean("bc");
                boolean cargfuelcraft = set.getBoolean("cargfuelcraft");
                boolean cargfuel = set.getBoolean("cargfuel");
                boolean stazavod =set.getBoolean("stazavod");
                boolean cargfabric = set.getBoolean("cargfabric");
                boolean MoneyYardPlus = set.getBoolean("MoneyYardPlus");
                boolean MoneyPredPlus = set.getBoolean("MoneyPredPlus");
                new BaseBuilds(townName,factorywar,carg,bank,bc,true,cargfuelcraft,cargfuel,stazavod,cargfabric,true,MoneyYardPlus,MoneyPredPlus);
            }
        } catch (SQLException e){
            Common.error(e, "Failed to load database town data from table builds");
        }
    }
}
