package dev.loveeev.astracore.database;

import dev.loveeev.astracore.data.Gun;
import dev.loveeev.astracore.data.TownData;
import org.mineacademy.fo.Common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Этот класс управляет таблицей, содержащей статистику произведенного оружия
 */
public class GunsDatabaseManager {

    /**
     * Создать таблицу towny_gun_stats с колонками townName, gun, produced в базе данных
     * townName - название города, для которого ведется статистика
     * gun - название оружия
     * produced - сколько оружия уже было произведено
     */
    public static void createTable(){
        try{
            PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("CREATE TABLE IF NOT EXISTS towny_gun_stats(" +
                    "townName VARCHAR(255) NOT NULL, gun VARCHAR(50) NOT NULL, produced INT NOT NULL, PRIMARY KEY(townName, gun))"
            );
            st.executeUpdate();
        } catch (SQLException e){
            Common.error(e, "Failed to create database table towny_gun_stats");
        }
    }

    /**
     * Этот метод загружают статистику произведенного оружия для всех городов.
     * Он должен запускаться только после того, как все города загружены в плагин,
     * иначе вся статистика по городам будет удалена.
     */
    public static void loadGunStatistics(){
        try{
            PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("SELECT * FROM towny_gun_stats");
            ResultSet set = st.executeQuery();

            while (set.next()){
                String townName = set.getString("townName");
                TownData data = TownData.getByName(townName);
                if (data == null){
                    // Удалить статистику несуществующего города
                    resetGunStats(townName);
                    Common.warning("Town " + townName + " does not exist, deleting its gun statistics...");
                    return;
                }

                String gunName = set.getString("gun");
                int stat = set.getInt("produced");

                data.setGunStatistic(gunName, stat);
            }
        } catch (SQLException e){
            Common.error(e, "Failed to load gun statistics from database table towny_gun_stats.");
        }
    }

    /**
     * Установить новое значение статистики произведенного оружия для данного города и данного оружия
     * @param townName название города
     * @param gun оружие
     * @param newValue новое значение
     */
    public static void setGunStat(String townName, Gun gun, Integer newValue){
        if (newValue == null) return;

        Common.runAsync(() -> {
            try{
                PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("INSERT INTO towny_gun_stats (townName, gun, produced) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE produced = ?");
                st.setString(1, townName);
                st.setString(2, gun.getName());
                st.setInt(3, newValue);
                st.setInt(4, newValue);

                st.executeUpdate();
            } catch (SQLException e){
                Common.error(e, "Failed to update database table towny_gun_stats with 'gun' and 'produced'");
            }
        });
    }

    /**
     * Сбросить всю статистику произведенного оружия для города с указанным названием
     * @param townName название города
     */
    public static void resetGunStats(String townName){
        Common.runAsync(() -> {
            try{
                PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("DELETE FROM towny_gun_stats WHERE townName = ?");
                st.setString(1, townName);
                st.executeUpdate();
            } catch (SQLException e){
                Common.error(e, "Failed to reset gun statistics on database table towny_gun_stats for town " + townName + ".");
            }
        });
    }

}
