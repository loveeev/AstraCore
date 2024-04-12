package dev.loveeev.astracore.util;

import dev.loveeev.astracore.data.AstraWarsData;
import dev.loveeev.astracore.handler.War;

import java.util.Iterator;

public class DataUtil {
    public DataUtil() {
    }

    public static War getWarByName(String name) {
        War war = null;
        Iterator var2 = AstraWarsData.warList.iterator();

        while(var2.hasNext()) {
            War currentWar = (War)var2.next();
            if (currentWar.getName().equalsIgnoreCase(name)) {
                war = currentWar;
                break;
            }
        }

        return war;
    }

    public static War getFirstEnabledWar() {
        War war = null;
        Iterator var1 = AstraWarsData.warList.iterator();

        while(var1.hasNext()) {
            War currentWar = (War)var1.next();
            if (currentWar.isEnable()) {
                war = currentWar;
                break;
            }
        }

        return war;
    }
}