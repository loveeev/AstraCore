package dev.loveeev.astracore.data;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import dev.loveeev.astracore.database.BuildsDatabase;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.settings.Settings;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class YardManager {

    public static int produceGold() {
        int produced = 0;
        for (TownData townData : TownData.getTownsData().values()) {
            if (!townData.isTownStillExist()) continue;
            BaseBuilds baseBuilds = BaseBuilds.getOrCreate(townData.getTownName());
            if (!baseBuilds.isIsyard()) continue;
            int dayLimit = (baseBuilds.isIsyardplus()) ? (int) (Settings.MONEYYARD * 1.5) + BuildsDatabase.moenybank(townData.getTownName()) : Settings.MONEYYARD + BuildsDatabase.moenybank(townData.getTownName());

            int firstEmptySlot = getFirstEmptySlot(townData.getMoneyInventory());

            if (firstEmptySlot == -1) {
                townData.updateMoneyInventory();
                continue;
            }

            try {
                if (!TownyAPI.getInstance().getTown(townData.getTownName()).hasNation()) {
                    // Если город не имеет нации, пропускаем его обработку
                    continue;
                }

                ResultSet resultSet = MySQL.getInstance().executeQuery("SELECT * FROM `AstraEconomy` WHERE nation = ?", TownyAPI.getInstance().getTown(townData.getTownName()).getNation().getName());

                while (dayLimit > 0 && resultSet.next()) {
                    String name = resultSet.getString("name");
                    int modeldata = resultSet.getInt("modeldata");

                    if (name == null) continue;

                    int amountToAdd = Math.min(dayLimit, 64); // Определение количества предметов для добавления в инвентарь
                    ItemStack inkSack = new ItemStack(Material.SCULK, amountToAdd);
                    ItemMeta itemMeta = inkSack.getItemMeta();
                    itemMeta.setCustomModelData(modeldata);
                    itemMeta.setDisplayName(name);
                    inkSack.setItemMeta(itemMeta);

                    townData.getMoneyInventory().put(firstEmptySlot, inkSack);
                    dayLimit -= amountToAdd;
                    firstEmptySlot = getFirstEmptySlot(townData.getMoneyInventory());
                    produced += amountToAdd; // Увеличиваем количество произведенного золота
                }
            } catch (SQLException | NotRegisteredException e) {
                throw new RuntimeException(e);
            }

            townData.updateMoneyInventory();
        }
        return produced;
    }



    public static int getFirstEmptySlot(Map<Integer, ItemStack> items) {
        for (int i = 0; i < 56; i++) {
            ItemStack item = items.get(i);
            if (item == null || item.getType() == Material.AIR) {
                return i;
            }
        }
        return -1;
    }
    public static Map<Integer, ItemStack> itemsArrayToMap(ItemStack[] array){
        Map<Integer, ItemStack> map = new HashMap<>();
        for (int i = 0; i < array.length; i++){
            if (array[i] == null) continue;
            map.put(i, array[i]);
        }
        return map;
    }
}
