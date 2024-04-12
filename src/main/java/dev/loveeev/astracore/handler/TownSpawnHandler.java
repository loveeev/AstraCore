package dev.loveeev.astracore.handler;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.event.TownSpawnEvent;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.AstraWarsData;
import dev.loveeev.astracore.util.DataUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownSpawnHandler implements Listener {
    public TownSpawnHandler() {
    }

    @EventHandler
    public void onTownSpawnEvent(TownSpawnEvent event) {
        if (AstraWarsData.hasEnabledWar) {
            War war = DataUtil.getFirstEnabledWar();
            Player player = event.getPlayer();
            Town playerTown = TownyAPI.getInstance().getTown(player);
            if (playerTown != null && playerTown == event.getToTown() && war.getOccupiedTowns().contains(playerTown)) {
                Main.getInstance().send(player, "Ваш город был оккупирован. Вы не можете возвращаться на t spawn");
                event.setCancelled(true);
            }
        }

    }
}