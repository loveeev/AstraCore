package dev.loveeev.astracore.data;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.database.BuildsDatabase;
import dev.loveeev.astracore.database.MySQL;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BaseBuilds {

    @Getter
    private static final Map<String, BaseBuilds> baseBuildsMap = new HashMap<>();
    @Getter
    private final String townName;
    @Getter
    private boolean isfactorywar;
    @Getter
    private boolean isPort;
    @Getter
    private boolean isBank;
    @Getter
    private boolean isteleg;
    @Getter
    private boolean ismayorbank;
    @Getter
    private boolean isfuelcraft;
    @Getter
    private boolean isfuel;
    @Getter
    private boolean issteelfactory;
    @Getter
    private boolean isleather;
    @Getter
    private boolean isyard;
    @Getter
    private boolean isyardplus;
    @Getter
    private boolean ispredplus;
    private BaseBuilds(String townName) {
        this(townName, false, false, false, false, true, false, false, false, false, true,false,false);
    }

    /**
     * Этот конструктор нужен, чтобы создавать экземпляр TownData из данных, которые пришли из БД
     */
    public BaseBuilds(String townName, Boolean isfactorywar, Boolean isPort, Boolean isBank, Boolean isteleg, Boolean ismayorbank, Boolean isfuelcraft, Boolean isfuel, Boolean issteelfactory, Boolean isleather, Boolean isyard,Boolean isyardplus,Boolean ispredplus) {
        this.townName = townName;
        this.isfactorywar = isfactorywar;
        this.isPort = isPort;
        this.isBank = isBank;
        this.isteleg = isteleg;
        this.ismayorbank = ismayorbank;
        this.isfuelcraft = isfuelcraft;
        this.isfuel = isfuel;
        this.issteelfactory = issteelfactory;
        this.isleather = isleather;
        this.isyard = isyard;
        this.isyardplus = isyardplus;
        this.ispredplus = ispredplus;
        baseBuildsMap.put(townName, this);
    }

    public void setFactoryWar(Boolean tf) {
        isfactorywar = tf;
        updatedatabase("cargw","waramount",tf);
    }

    public void setPort(Boolean tf) {
        isPort = tf;
        updatedatabase("carg","portamount",tf);
    }

    public void setBank(Boolean tf) {
        isBank = tf;
        updatedatabase("bank","bankamount",tf);
    }

    public void setTelegraph(Boolean tf) {
        isteleg = tf;
        updatedatabase("bc","bcamount",tf);
    }

    public void setMayorBank(Boolean tf) {
        ismayorbank = tf;
        updatedatabase("mayorbank","bmamount",tf);
    }

    public void setFuelCraft(Boolean tf) {
        isfuelcraft = tf;
        updatedatabase("cargfuelcraft","fuelcraftamount",tf);
    }

    public void setFuel(Boolean tf) {
        isfuel = tf;
        updatedatabase("cargfuel","fuelamount",tf);
    }

    public void setSteelFactory(Boolean tf) {
        issteelfactory = tf;
        updatedatabase("stazavod","staproduction",tf);
    }

    public void setLeather(Boolean tf) {
        isleather = tf;
        updatedatabase("cargfabric","fabricamount",tf);
    }

    public void setYard(Boolean tf) {
        isyard = tf;
        updatedatabase("MoneyYard","MoneyYardAmount",tf);
    }
    public void setYardplus(Boolean tf) {
        isyardplus = tf;
        updatedatabase("MoneyYardPlus","MoneyYardAmount",tf);
    }
    public void setPradplus(Boolean tf) {
        ispredplus = tf;
        updatedatabase("MoneyPredPlus","bmamount",tf);
    }
    private boolean isDatabaseConnected(){
        return MySQL.getInstance().isConnected();
    }
    public static BaseBuilds getOrCreate(Player player) throws NotRegisteredException {
        Town town = TownyAPI.getInstance().getTown(player);
        assert town != null;
        return BaseBuilds.getOrCreate(town.getName());
    }

    /**
     * Получить TownData по названию города, или создать, если оно еще не существует.
     * @param name название города
     * @return экземпляр TownData
     */
    public static BaseBuilds getOrCreate(String name){
        if (baseBuildsMap.containsKey(name)){
            return baseBuildsMap.get(name);
        }
        return new BaseBuilds(name);
    }
    public void updatedatabase(String column,String col,boolean tf) {
        if (isDatabaseConnected()) {
            BuildsDatabase.setBuilds(townName,column,col, tf);
        }
    }
}
