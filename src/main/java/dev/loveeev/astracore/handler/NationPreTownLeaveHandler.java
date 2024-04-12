package dev.loveeev.astracore.handler;

import com.palmergames.bukkit.towny.event.nation.NationPreTownLeaveEvent;
import dev.loveeev.astracore.data.AstraWarsData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Iterator;

public class NationPreTownLeaveHandler implements Listener {
    public NationPreTownLeaveHandler() {
    }

    @EventHandler
    public void onNationPreTownLeaveEvent(NationPreTownLeaveEvent event) {
        if (AstraWarsData.hasEnabledWar) {
            Iterator var2 = AstraWarsData.warList.iterator();

            while(var2.hasNext()) {
                War war = (War)var2.next();
                if (war.getNationList().contains(event.getNation())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

    }
}
