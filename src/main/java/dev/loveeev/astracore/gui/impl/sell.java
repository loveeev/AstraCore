package dev.loveeev.astracore.gui.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.gui.sellmenus.farmsell;
import dev.loveeev.astracore.gui.sellmenus.goldsell;
import dev.loveeev.astracore.gui.sellmenus.oaksell;
import dev.loveeev.astracore.gui.sellmenus.resourcesell;
import dev.loveeev.astracore.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

/**
 * Военное меню
 */
public class sell extends AdvancedMenu {

    Main plugin;

    private final TownData townData;

    public sell(Player player,Main plugin,TownData townData) {
        super(player);
        this.townData = townData;
        this.plugin = plugin;
    }

    @Override
    protected void setup() {
        MySQL mySQL = MySQL.getInstance();
        setSize(9*3);
        setTitle("Продажа предметов");

        // Добавить предметы и кнопки в меню
        addButton(11, getGoldButton());


        Player player = getPlayer();
        Resident resident = TownyAPI.getInstance().getResident(player);
        if(resident.hasTown()) {
            Town town = TownyAPI.getInstance().getTown(player);
            mySQL.selectstring("TOWNY_TOWNS","name",String.valueOf(town),"oaksector","integer");
            Integer value = (Integer)Main.getInstance().getseceltinteger();
            if(value == 1){
                addButton(12, getOakLogButton());
            }else {
                addButton(12,getOaknotunlock());
            }
        }else {
            addButton(12, getOak());
        }
        if(resident.hasTown()) {
            Town town = TownyAPI.getInstance().getTown(player);
            mySQL.selectstring("TOWNY_TOWNS","name",String.valueOf(town),"resourcesector","integer");
            Integer value = (Integer)Main.getInstance().getseceltinteger();
            if(value == 1){
                addButton(14, getresourceButton());
            }else {
                addButton(14, getresourcenotunlock());
            }
        }else {
            addButton(14, geresource());
        }
        if(resident.hasTown()) {
                Town town = TownyAPI.getInstance().getTown(player);
                mySQL.selectstring("TOWNY_TOWNS","name",String.valueOf(town),"farmer","integer");
                Integer value = (Integer)Main.getInstance().getseceltinteger();
                if(value == 1){
                    addButton(13,getfarmunlock());
                }else {
                    addButton(13,getfarmnotunlock());
                }
        }else {
            addButton(13, getfarm());
        }
        addButton(15, barrier());

        // Установить обертку на черную стеклянную панель
        setWrapper(CompMaterial.BLACK_STAINED_GLASS_PANE);

        // Установить закрытые слоты (на них будет обертка, которая установлена выше)
        setLockedSlots(0,1,2,3,4,5,6,7,8,9,10,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35);
    }
    /**
     * Получить кнопку, при нажатии на которую открывается меню выбора производимого оружия
     */

    private Button barrier(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player,"В следующих обновлениях.. Можете предлагать на форуме.");
            }


            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.BARRIER)
                        .name("")
                        .make();
            }
        };
    }
    private Button geresource(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player,"Вы не в городе");
            }
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.IRON_INGOT)
                        .name("&#FFDAA4Металлургический сектор")
                        .lore("","&4Вы не в городе.")
                        .make();
            }
        };
    }
    private Button getresourcenotunlock(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player,"Вы не разблокировали этот сектор.");
            }
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.IRON_INGOT)
                        .name("&#FFDAA4Металлургический сектор")
                        .lore("","&4Вы не разблокировали данный сектор.", "Цена: &#FFDAA4" + Settings.METALSECTOR)
                        .make();
            }
        };
    }
    private Button getresourceButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                new resourcesell(plugin).create(player).open(player);
            }
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.IRON_INGOT)
                        .name("&#FFDAA4Металлургический сектор")
                        .lore("","&7Нажмите, чтобы открыть.")
                        .make();
            }
        };
    }
    private Button getfarm(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player,"Вы не в городе");
            }
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.WHEAT)
                        .name("&#FFDAA4Аграрный сектор")
                        .lore("","&4Вы не в городе.")
                        .make();
            }
        };
    }
    private Button getfarmnotunlock(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player,"Вы не разблокировали этот сектор.");
            }
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.WHEAT)
                        .name("&#FFDAA4Аграрный сектор")
                        .lore("","&4Вы не разблокировали данный сектор.", "Цена: &#FFDAA4" + Settings.PRICEAGRAR)
                        .make();
            }
        };
    }
    private Button getfarmunlock(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                new farmsell(plugin).create(player).open(player);
            }
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.WHEAT)
                        .name("&#FFDAA4Аграрный сектор")
                        .lore("","&7Нажмите, чтобы открыть.")
                        .make();
            }
        };
    }
    private Button getGoldButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                new goldsell(plugin).create(player).open(player);
            }
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.GOLD_INGOT)
                        .name("&#FFDAA4Продажа золота.")
                        .lore("","&7Нажмите, чтобы открыть.")
                        .make();
            }
        };
    }
    private Button getOakLogButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                new oaksell(plugin).create(player).open(player);
            }
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.SPRUCE_LOG)
                        .name("&#FFDAA4Лесной сектор")
                        .lore("","&7Нажмите, чтобы открыть.")
                        .make();
            }
        };
    }
    private Button getOak(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player,"Вы не в городе");
            }
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.SPRUCE_LOG)
                        .name("&#FFDAA4Лесной сектор.")
                        .lore("","&4Вы не в городе.")
                        .make();
            }
        };
    }
    private Button getOaknotunlock(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player,"Вы не разблокировали этот сектор.");
            }
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.SPRUCE_LOG)
                        .name("&#FFDAA4Лесной сектор")
                        .lore("","&4Вы не разблокировали данный сектор.", "Цена: &#FFDAA4" + Settings.LESSECTOR)
                        .make();
            }
        };
    }
}
