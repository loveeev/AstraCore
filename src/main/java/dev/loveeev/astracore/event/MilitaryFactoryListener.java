package dev.loveeev.astracore.event;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.event.DeleteTownEvent;
import com.palmergames.bukkit.towny.event.NewTownEvent;
import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import com.palmergames.bukkit.towny.event.time.NewHourEvent;
import com.palmergames.bukkit.towny.event.time.dailytaxes.NewDayTaxAndUpkeepPreCollectionEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.*;
import dev.loveeev.astracore.database.GunsDatabaseManager;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.gui.builds.AuctionMenu;
import dev.loveeev.astracore.gui.builds.GunsMenu;
import dev.loveeev.astracore.gui.builds.MilitaryStatisticsMenu;
import dev.loveeev.astracore.gui.builds.WarMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.event.MenuOpenEvent;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.sql.ResultSet;
import java.sql.SQLException;

@AutoRegister
public final class MilitaryFactoryListener implements Listener {

    /**
     * Вызывается, когда в Towny наступает новый день и собираются налоги с города.
     * В этот момент мы производим оружие.
     */
    @EventHandler
    public void onNewDay(NewDayTaxAndUpkeepPreCollectionEvent event){
        MilitaryManager.produceWeapons();
        FuelManager.produceInkSacs();
        YardManager.produceGold();
    }

    @EventHandler
    public void onNewHour(NewHourEvent event){
        for (TownData townData : TownData.getTownsData().values()) {
            if (!townData.isTownStillExist()) continue;
            Town town = TownyAPI.getInstance().getTown(townData.getTownName());
            BaseBuilds baseBuilds = BaseBuilds.getOrCreate(townData.getTownName());
            if(!baseBuilds.isBank()) continue;
            assert town != null;
            for (Resident resident : town.getResidents()) {
                if(resident.isOnline()) {
                    try {
                        ResultSet resultSet = MySQL.getInstance().executeQuery("SELECT * FROM `AstraEconomy` WHERE nation = ?", townData.isNation().getName());
                        if (resultSet.next()) {
                            String name = resultSet.getString("name");
                            int modeldata = resultSet.getInt("modeldata");
                            if (name != null) {
                                Player player = resident.getPlayer();
                                assert player != null;
                                player.getInventory().addItem(ItemCreator.of(CompMaterial.SCULK)
                                        .name(name)
                                        .modelData(modeldata)
                                        .make());
                                Main.getInstance().getChatUtility().sendSuccessNotification(player,"Вы получили 1 " + name + " из банка.");
                            }

                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }



    /**
     * Вызывается, когда в Towny удаляется город.
     * В этот момент мы сбрасываем все данные города, включая инвентарь военного завода и статистику произведенного оружия.
     */
    @EventHandler
    public void onTownRemove(DeleteTownEvent event){
        TownData data = TownData.getByName(event.getTownName());
        if (data != null){
            data.resetTown();
        }
    }

    /**
     * Вызывается, когда кто-то создает новый город.
     * Здесь мы на всякий случай еще раз обнуляем статистику и все данные города, который пытается создать игрок
     */
    @EventHandler
    public void onTownCreate(NewTownEvent event){
        GunsDatabaseManager.resetGunStats(event.getTown().getName());
        TownData data = TownData.getByName(event.getTown().getName());
        if (data != null){
            data.resetTown();
        }
    }

    /**
     * Вызывается, когда игрок закрывает меню инвентаря военного завода.
     * Здесь мы сохраняем все предметы военного завода в базу данных.
     */
    @EventHandler
    public void onMenuClose(InventoryCloseEvent event){
        if (!(event.getPlayer() instanceof Player plr)) return;

        try{
            TownData data = TownData.getOrCreate(plr);

            // Если в городе игрока открыто меню военного инвентаря, и его просматривает этот игрок,
            // значит игрок выходит именно из этого меню. Сохраняем инвентарь военного завода.
            if (data.isMilitaryFactoryMenuOpened() && data.getMilitaryInventoryMenu().getPlayer().equals(plr)){
                data.getMilitaryInventoryMenu().onMenuClose(MilitaryManager.itemsArrayToMap(event.getInventory().getContents()));
                data.setMilitaryInventoryMenu(null);
            }
            if (data.isMoneyYardOpened() && data.getMoneyinv().getPlayer().equals(plr)){
                data.getMoneyinv().onMenuClose(YardManager.itemsArrayToMap(event.getInventory().getContents()));
                data.setMoneyinv(null);
            }

        } catch (NotRegisteredException ignored){
            // Игрок не находится ни в каком городе
            return;
        }
    }


    @EventHandler
    public void onResidentKick(TownRemoveResidentEvent event){
        TownData data = TownData.getByName(event.getTown().getName());
        if (data == null) return;
        if (data.isMilitaryFactoryMenuOpened()){
            data.getMilitaryInventoryMenu().closeMenu();
        }
        if(data.isFuelMenuOpened()){
            data.getInvfuel().closeMenu();
        }
        if(data.isMoneyYardOpened()){
            data.getMoneyinv().closeMenu();
        }
    }

    /**
     * Вызывается, когда игрок пытается открыть какое-то меню.
     * Когда он открывает меню, связанное с городом, но не состоит в городе, система не даст ему открыть это меню.
     */
    @EventHandler
    public void onMenuOpen(MenuOpenEvent event){
        if (event.getMenu() instanceof AuctionMenu){
            AuctionMenu.getViewers().add(event.getPlayer());
        }

        if (!TownData.isPlayerInTown(event.getPlayer())){
            if (event.getMenu() instanceof WarMenu || event.getMenu() instanceof GunsMenu || event.getMenu() instanceof MilitaryStatisticsMenu){
                event.setCancelled(true);
                Main.getInstance().getChatUtility().sendColoredMessage(event.getPlayer(), "&cВы не состоите в городе.");
            }
        }
    }

}
