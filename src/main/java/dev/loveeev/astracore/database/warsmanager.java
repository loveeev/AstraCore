package dev.loveeev.astracore.database;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import dev.loveeev.astracore.data.AstraWarsData;
import dev.loveeev.astracore.handler.War;
import dev.loveeev.astracore.util.DataUtil;
import org.mineacademy.fo.Common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class warsmanager {

    public static void createColumns() {

    }

    public int getCapNation(Nation nation){
        try {
            ResultSet resultSet = MySQL.getInstance().executeQuery("SELECT * FROM TOWNY_NATIONS WHERE name = ?", nation.getName());
            if(resultSet.next()) {
                return resultSet.getInt("CapProcent");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static void loadwars(){
        try{
            PreparedStatement st = MySQL.getInstance().getCon().prepareStatement("SELECT * FROM wars");
            ResultSet set = st.executeQuery();
            while (set.next()) {
                String namewar = set.getString("warname");
                String nation1 = set.getString("nation1");
                Nation nation = TownyAPI.getInstance().getNation(nation1);
                String nation2 = set.getString("nation2");
                Nation nationtwo = TownyAPI.getInstance().getNation(nation2);
                Integer startstop = set.getInt("startstop");
                Integer ocup = set.getInt("ocup");
                AstraWarsData.warList.add(new War(namewar));
                War war = DataUtil.getWarByName(namewar);
                List<Resident> soldiers = nation.getResidents().stream().filter(resident -> resident.getNationRanks().contains("soldier")).collect(Collectors.toList());
                war.getNationList().add(nation);
                war.getMemberList().addAll(soldiers.stream().map(soldier -> UUID.fromString(String.valueOf(soldier.getUUID()))).collect(Collectors.toList()));
                nation.getResidents();
                List<Resident> soldiersf = nationtwo.getResidents().stream().filter(resident -> resident.getNationRanks().contains("soldier")).collect(Collectors.toList());
                war.getNationList().add(nationtwo);
                war.getMemberList().addAll(soldiersf.stream().map(soldier -> UUID.fromString(String.valueOf(soldier.getUUID()))).collect(Collectors.toList()));
                nationtwo.getResidents();
                if (startstop == 1) {
                    war.setEnable(true);
                    AstraWarsData.hasEnabledWar = true;
                }
                if (startstop == 0) {
                    war.setEnable(false);
                    AstraWarsData.hasEnabledWar = false;
                }
                return;
            }
        } catch (SQLException e){
            Common.error(e, "Failed to loadwars");
        }
    }
}
