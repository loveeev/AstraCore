//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.loveeev.astracore.handler;

import com.palmergames.bukkit.towny.event.PreDeleteTownEvent;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class TownPreDeleteHandler implements Listener {
    public TownPreDeleteHandler() {
    }

    PreparedStatement ps;
    ResultSet rs;
    MySQL mySQL = MySQL.getInstance();
    @EventHandler
    public void onPreDeleteTownEvent(PreDeleteTownEvent event) {
        mySQL.selectstring("TOWNY_RESIDENTS", "name", "loveeev", "provin", "integer");
        int resulttf;
        resulttf = (Integer)Main.getInstance().getseceltinteger();
        if (resulttf == 0) {
            Town town = event.getTown();
            Resident mayor = town.getMayor();
            if (!mayor.getName().startsWith("NPC")) {
                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ta set mayor " + town.getName() + " npc");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"ta town " + town.getName() + " set perm on");
                    commandwr("pvp",town);
                    commandwr("forcepvp",town);
                    commandwr("explosion",town);
                    commandwr("fire",town);
                    commandwr("mobs",town);
                });
                this.removeResidents(town);
            }
            event.setCancelled(true);
        }
    }
    public void commandwr(String p,Town town){
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"ta town " + town.getName() + " toggle " + p + " on");
    }

    private void removeResidents(Town town) {
        List<Resident> residents = town.getResidents();

        for(int i = 0; i < residents.size(); ++i) {
            Resident resident = (Resident)residents.get(i);
            if (!resident.getName().startsWith("NPC")) {
                resident.removeTown();
                Player player = resident.getPlayer();
                if (player != null) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Ваш город распался. Вы были исключены");
                }
            }
        }

    }
}
