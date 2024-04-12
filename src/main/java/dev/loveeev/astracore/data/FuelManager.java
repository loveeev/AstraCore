package dev.loveeev.astracore.data;

import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class FuelManager {

    public static int produceInkSacs() {
        int produced = 0;

        for (TownData townData : TownData.getTownsData().values()) {
            if (!townData.isTownStillExist()) continue;

            MySQL mySQL = MySQL.getInstance();
            mySQL.selectstring("TOWNY_TOWNS", "name", townData.getTownName(), "cargfuel", "integer");
            Object real = Main.getInstance().getseceltinteger();
            if ((Integer)real == 0) continue;

            mySQL.selectstring("TOWNY_TOWNS", "name", townData.getTownName(), "fuel", "integer");
            Integer dayLimit = (Integer) Main.getInstance().getseceltinteger();

            int firstEmptySlot = getFirstEmptySlot(townData.getFuelinv());

            if (firstEmptySlot == -1) {
                townData.updateFuelInventory();
                continue;
            } else {
                if (dayLimit > 0) {
                    while (dayLimit > 0) {
                        // Создаем ItemStack для чернильного мешочка с максимумом 64
                        ItemStack inkSack = new ItemStack(Material.INK_SAC, Math.min((Integer)dayLimit, 64));
                        townData.getFuelinv().put(firstEmptySlot, inkSack);
                        mySQL.selectstring("TOWNY_TOWNS","name",townData.getTownName(),"statfuel","integer");
                        Integer fuel = (Integer) Main.getInstance().getseceltinteger() + dayLimit;
                        mySQL.updateint("TOWNY_TOWNS","statfuel","name",fuel,townData.getTownName());
                        dayLimit -= 64;
                        firstEmptySlot = getFirstEmptySlot(townData.getFuelinv());

                        if (firstEmptySlot == -1) {
                            townData.updateFuelInventory();
                            break;
                        }
                    }
                    produced += (dayLimit + 64);
                }
            }
            townData.updateFuelInventory();
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
}
