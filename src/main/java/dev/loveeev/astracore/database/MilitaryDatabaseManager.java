package dev.loveeev.astracore.database;

import dev.loveeev.astracore.data.BukkitSerialization;
import dev.loveeev.astracore.data.Gun;
import dev.loveeev.astracore.data.GunStorage;
import dev.loveeev.astracore.data.TownData;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Этот класс управляет базой данных для военного завода
 */
public class MilitaryDatabaseManager {

    /**
     * Создать колонки producingGun (производимое оружие) и militaryInv (инвентарь военного завода) в таблице towny_towns
     */
        public static void createColumn(String columnName, String columnType) {
            try {
                DatabaseMetaData md = MySQL.getInstance().getCon().getMetaData();
                ResultSet resultSet = md.getColumns(null, null, "TOWNY_TOWNS", columnName);
                if (!resultSet.next()) {
                    String query = "ALTER TABLE TOWNY_TOWNS ADD COLUMN " + columnName + " " + columnType;
                    PreparedStatement st = MySQL.getInstance().getCon().prepareStatement(query);
                    st.executeUpdate();
                }
            } catch (SQLException e) {
                Common.error(e, "Failed to add column " + columnName + " to table TOWNY_TOWNS");
            }
        }

        public static void createColumns() {
            String[][] columns = {
                    {"resourcesector", "VARCHAR(50)"},
                    {"militaryInv", "TEXT"},
                    {"producingGun", "TEXT"},
                    {"oaksector", "INT"},
                    {"payer", "VARCHAR(16000)"},
                    {"farmer", "INT"},
                    {"mayorbank", "INT"},
                    {"bc", "INT"},
                    {"spec", "INT"},
                    {"cargw", "INT"},
                    {"premium", "BOOLEAN"},
                    {"carg", "INT"},
                    {"cargwar", "INT"},
                    {"cargst", "INT"},
                    {"bank", "INT"},
                    {"statfuelglow", "TEXT"},
                    {"statfuel", "TEXT"},
                    {"fuelinv", "TEXT"},
                    {"cargfuel", "INT"},
                    {"fuel", "INT"},
                    {"cargfuelcraft", "INT"},
                    {"cargtrain", "INT"},
                    {"cargraft", "INT"},
                    {"cargtank", "INT"},
                    {"cargplane", "INT"},
                    {"cargdrill", "INT"},
                    {"cargcar", "INT"},
                    {"stazavod", "TEXT"},
                    {"staproduction", "TEXT"},
                    {"cargfabric", "INT"},
                    {"procwar", "INT"},
                    {"WarProc", "INT"},
                    {"MoneyYard", "INT"},
                    {"yardinv", "TEXT"},
                    {"MoneyYardPlus","INT"},
                    {"MoneyPredPlus","INT"}
            };

            for (String[] column : columns) {
                createColumn(column[0], column[1]);
            }
        }



    /**
     * Обновить производимое оружие в БД
     * @param townName название города для которого нужно обновить оружие
     * @param gun оружие
     */
    public static void setGun(String townName, Gun gun){
        Common.runAsync(() -> {
            try{
                PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("UPDATE TOWNY_TOWNS SET producingGun = ? WHERE name = ?");
                st.setString(1, gun.getName());
                st.setString(2, townName);
                st.executeUpdate();
            } catch (SQLException e){
                Common.error(e, "Failed to update database table towny_towns with producing gun");
            }
        });
    }

    /**
     * Обновить инвентарь военного завода в БД
     * @param townName название города для которого нужно обновить инвентарь
     * @param itemStackMap инвентарь в виде СЛОТ-ПРЕДМЕТ
     */
    public static void setInventory(String townName, Map<Integer, ItemStack> itemStackMap){
        Common.runAsync(() -> {
            String serialized = BukkitSerialization.itemsMapToBase64(itemStackMap);

            try{
                PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("UPDATE TOWNY_TOWNS SET militaryInv = ? WHERE name = ?");
                st.setString(1, serialized);
                st.setString(2, townName);
                st.executeUpdate();
            } catch (SQLException e){
                Common.error(e, "Failed to update database table towny_towns with military inventory");
            }
        });
    }
    /**
     * Обновить инвентарь военного завода в БД
     * @param townName название города для которого нужно обновить инвентарь
     * @param itemStackMap инвентарь в виде СЛОТ-ПРЕДМЕТ
     */

    public static void setInventoryFuel(String townName, Map<Integer, ItemStack> itemStackMap){
        Common.runAsync(() -> {
            String serialized = BukkitSerialization.itemsMapToBase64(itemStackMap);

            try{
                PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("UPDATE TOWNY_TOWNS SET fuelinv = ? WHERE name = ?");
                st.setString(1, serialized);
                st.setString(2, townName);
                st.executeUpdate();
            } catch (SQLException e){
                Common.error(e, "Failed to update database table towny_towns with fuelinv inventory");
            }
        });
    }
    public static void setMoneyYard(String townName, Map<Integer, ItemStack> itemStackMap){
        Common.runAsync(() -> {
            String serialized = BukkitSerialization.itemsMapToBase64(itemStackMap);
            try{
                PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("UPDATE TOWNY_TOWNS SET yardinv = ? WHERE name = ?");
                st.setString(1, serialized);
                st.setString(2, townName);
                st.executeUpdate();
            } catch (SQLException e){
                Common.error(e, "Failed to update database table towny_towns with fuelinv inventory");
            }
        });
    }

    /**
     * Установить статус премиум для города с указанным названием
     * @param townName название города
     * @param isPremium статус премиум
     */
    public static void setPremium(String townName, boolean isPremium){
        Common.runAsync(() -> {
            try{
                PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("UPDATE TOWNY_TOWNS SET premium = ? WHERE name = ?");
                st.setBoolean(1, isPremium);
                st.setString(2, townName);
                st.executeUpdate();
            } catch (SQLException e){
                Common.error(e, "Failed to update database table towny_towns with premium flag.");
            }
        });
    }

    /**
     * Загрузить данные из БД.
     * Вызывается при запуске сервера, чтобы загрузить все данные из БД в оперативную память сервера.
     */
    public static void loadTownData(){
        try{
            PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("SELECT * FROM TOWNY_TOWNS");
            ResultSet set = st.executeQuery();

            while (set.next()){
                String townName = set.getString("name");
                String producingGun = set.getString("producingGun");
                String encodedInventory2 = set.getString("fuelinv");
                String encodedInventory = set.getString("militaryInv");
                String encodedInventory3 = set.getString("yardinv");
                boolean premium = set.getBoolean("premium");

                Gun gun = GunStorage.getByName(producingGun);
                Map<Integer, ItemStack> militaryInventory = new HashMap<>();
                Map<Integer, ItemStack> fuelinv = new HashMap<>();
                Map<Integer, ItemStack> yaedinv = new HashMap<>();
                if (encodedInventory != null){
                    militaryInventory = BukkitSerialization.itemsMapFromBase64(encodedInventory);
                }
                if (encodedInventory2 != null){
                    fuelinv = BukkitSerialization.itemsMapFromBase64(encodedInventory2);
                }
                if(encodedInventory3 != null){
                    yaedinv = BukkitSerialization.itemsMapFromBase64(encodedInventory3);
                }

                new TownData(townName, gun, militaryInventory, premium,fuelinv,yaedinv);
//                GunsDatabaseManager.loadGunStatistics(townName);
            }
        } catch (SQLException | IOException e){
            Common.error(e, "Failed to load database town data from table towny_military");
        }
    }

}
