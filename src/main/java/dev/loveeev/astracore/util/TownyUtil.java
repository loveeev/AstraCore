package dev.loveeev.astracore.util;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownyPermission;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.warsmanager;
import dev.loveeev.astracore.handler.War;
import dev.loveeev.astracore.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class TownyUtil {
    public TownyUtil() {
    }

    public static void setWarPermissions(Town town, boolean isStart) {
        TownyPermission townyPermission = town.getPermissions();
        townyPermission.set("pvp", isStart);
        townyPermission.set("fire", isStart);
        townyPermission.set("mobs", isStart);
        townyPermission.set("explosion", isStart);
        townyPermission.set("outsiderItemUse", isStart);
        townyPermission.set("outsiderBuild", isStart);
        townyPermission.set("outsiderSwitch", isStart);
    }

    public static Nation getPlayerNation(Player player) {
        Resident resident = TownyAPI.getInstance().getResident(player);
        return resident == null ? null : resident.getNationOrNull();
    }

    public static Nation getTownNation(Town town) {
        Nation townNation = null;

        try {
            townNation = town.getNation();
        } catch (NotRegisteredException var3) {
        }

        return townNation;
    }

    public static void occupy(War war, Town town, Nation townNation, Nation attakingNation, boolean isDeOccupation) {
        warsmanager warsmanager = new warsmanager();
        town.setMapColorHexCode(attakingNation.getMapColorHexCode());
        Main var10000 = Main.getInstance();
        String var10001 = town.getName();
        var10000.sendPublic("Город " + var10001 + " был занят нацией " + attakingNation.getName());
        if (!isDeOccupation && townNation != null && calculateCapitulationPercent(war, townNation) >= warsmanager.getCapNation(townNation)) {
            Player player;
            for(Iterator var5 = Bukkit.getOnlinePlayers().iterator(); var5.hasNext(); war.getMemberList().remove(player.getUniqueId())) {
                player = (Player)var5.next();
                Nation playerNation = getPlayerNation(player);
                if (playerNation != null && playerNation == townNation) {
                    player.kickPlayer(Settings.prefix + "Ваша нация капитулировала во время войны. Возвращайтесь после её окончания");
                }
            }

            war.getNationList().remove(townNation);
            Main.getInstance().sendPublic("Нация " + townNation + " капитулировала");
        }

    }

    public static int calculateCapitulationPercent(War war, Nation nation) {
        int occupiedTowns = 0;
        Iterator var3 = war.getOccupiedTowns().iterator();

        while(var3.hasNext()) {
            Town town = (Town)var3.next();
            Nation townNation = getTownNation(town);
            if (townNation != null && nation == townNation) {
                ++occupiedTowns;
            }
        }

        return occupiedTowns * 100 / nation.getTowns().size();
    }
}
