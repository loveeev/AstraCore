package dev.loveeev.astracore.gui.impl;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.BaseBuilds;
import dev.loveeev.astracore.data.Building;
import dev.loveeev.astracore.data.BuildingData;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.gui.RAFTMENU;
import dev.loveeev.astracore.gui.builds.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.AdvancedMenuPagged;
import org.mineacademy.fo.menu.MenuSlots;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.List;

public class BuildingMenu extends AdvancedMenuPagged<BuildingData> {
    public BuildingMenu(Player player) {
        super(player);
    }

    @Override
    protected List<BuildingData> getElements() {
        return Building.getBUILDING_DATA();
    }

    @Override
    protected ItemStack convertToItemStack(BuildingData building) {
        return ItemCreator.of(building.getMaterial())
                .name(building.getName())
                .lore(building.getLore())
                .make();
    }

    @Override
    protected void setup() {
        setSize(9 * 5);
        setTitle("Государственные здания");
        setWrapper(CompMaterial.fromMaterial(Material.BLACK_STAINED_GLASS_PANE));
        setLockedSlots(MenuSlots.Shape.BOUNDS);
        setPreviousButtonItem(ItemCreator.of(CompMaterial.ARROW).name("&#FFDAA4Предыдущая страница").hideTags(true).make());
        setNextButtonItem(ItemCreator.of(CompMaterial.ARROW).name("&#FFDAA4Следующая страница").hideTags(true).make());
        setPageButtonsAlwaysEnabled(true);
    }

    @Override
    protected void onElementClick(Player player, BuildingData buildingData, int slot, ClickType clickType) {
        try {
            TownData data = TownData.getOrCreate(getPlayer());
            BaseBuilds baseBuilds = BaseBuilds.getOrCreate(getPlayer());
            Main plugin = Main.getInstance();
            String message = " не разблокирован";
            switch (buildingData.getId()) {
                case 1:
                    if(baseBuilds.isIsfactorywar()) {
                        new WarMenu(player, data).display();
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, buildingData.getName() + message);
                    }
                    break;
                case 2:
                    if(data.isPremium()) {
                        new WarMenu(player, data).display();
                    }else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player,buildingData.getName() + message);
                    }
                    break;
                case 3:
                    if(baseBuilds.isPort()) {
                        new RAFTMENU(player, plugin).display();
                    }else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player,buildingData.getName() + message);
                    }
                    break;
                case 4:
                    Main.getInstance().getChatUtility().sendSuccessNotification(player,"После разблокировки данного здания будет приносить по 1 валюте нации каждому игроку в час");
                    break;
                case 5:
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /telegraph текст");
                    break;
                case 6:
                    if(baseBuilds.isIsmayorbank()) {
                        if(data.isNationExist()) {
                            new mayorbankgui(player, plugin).display();
                        }else{
                            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас нету нации.");
                        }
                    }else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player,buildingData.getName() + message);
                    }
                    break;
                case 7:
                    if(baseBuilds.isIsfuelcraft()) {
                        new fuelcraftzavod(player, data, plugin).display();
                    }else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player,buildingData.getName() + message);
                    }
                    break;
                case 8:
                    if(baseBuilds.isIsfuel()) {
                        new fuelzavod(player, data, plugin).display();
                    }else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player,buildingData.getName() + message);
                    }
                    break;
                case 9:
                    if(baseBuilds.isIssteelfactory()) {
                        new stalzavod(plugin).create(player).open(player);
                    }else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player,buildingData.getName() + message);
                    }
                    break;
                case 10:
                    if(baseBuilds.isIsleather()) {
                        if(data.isNationExist()) {
                            new stanok(player, plugin,data).display();
                        }else{
                            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас нету нации.");
                        }
                    }else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player,buildingData.getName() + message);
                    }
                    break;
                case 11:
                    if(baseBuilds.isIsyard()) {
                        if(data.isNationExist()) {
                            new MoneyYardGui(player, plugin).display();
                        }else{
                            Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас нету нации.");
                        }
                    }else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player,buildingData.getName() + message);
                    }
                    break;
                default:
                    Main.getInstance().getChatUtility().sendSuccessNotification(player,"Ошибка. Обратитесь к администрации.");
                    break;
            }

        } catch (NotRegisteredException e) {
            throw new RuntimeException(e);
        }
    }

}
