package dev.loveeev.astracore.gui.builds;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.Gun;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Военное меню
 */
public class WarMenu extends AdvancedMenu {

    private final TownData townData;

    public WarMenu(Player player, TownData townData) {
        super(player);
        this.townData = townData;
    }

    @Override
    protected void setup() {
        setSize(9*4);
        setTitle(townData.isPremium() ? "Военный завод+" : "Военный завод");

        // Добавить предметы и кнопки в меню
        addItem(12, getInProgressButton());
        addButton(13, getPistolGunsMenuButton());
        addButton(14, getStatisticsButton());
        addButton(22, getFactoryInventoryButton());
        addButton(21, getPatoronButton());
        if (townData.isPremium()){
            addItem(23, getPremiumButton());
        }
        addButton(27,getInfoWarButton());

        // Установить обертку на черную стеклянную панель
        setWrapper(CompMaterial.BLACK_STAINED_GLASS_PANE);

        // Установить закрытые слоты (на них будет обертка, которая установлена выше)
        setLockedSlots(0,1,2,3,4,5,6,7,8,9,10,11,15,16,17,18,19,20,24,25,26,28,29,30,31,32,33,34,35);
    }

    private ItemStack getPremiumButton() {
        return ItemCreator.of(CompMaterial.TOTEM_OF_UNDYING)
                .name("&#FFDAA4Спасибо за покупку!")
                .lore("",
                        "&7Для вашего города действуют бустеры:",
                        "&7- Ограничение на производство оружие увеличено в 2 раза",
                        "&7- Производство оружия расходует в 1.5 раза меньше ресурсов")
                .make();
    }

    /**
     * Получить кнопку, показывающую производимое оружие
     */
    private ItemStack getInProgressButton(){
        Gun gun = getNowProducingGun();
        return ItemCreator.of(CompMaterial.PAPER)
                .name("&#FFDAA4Сейчас в производстве:")
                .lore(gun == null ? "Ничего" : gun.getName())
                .make();
    }

    /**
     * Получить оружие, которое производится городом в данный момент
     */
    @Nullable
    private Gun getNowProducingGun(){
        try{
            return TownData.getOrCreate(getPlayer()).getProducingGun();
        } catch (NotRegisteredException e){
            closeMenu();
            Main.getInstance().getChatUtility().sendColoredMessage(getPlayer(), Settings.NOT_IN_TOWN_MESSAGE);
            return null;
        }
    }
    private Button getInfoWarButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
            }

            @Override
            public ItemStack getItem() {
                Town town = TownyAPI.getInstance().getTown(getPlayer());
                List<String> lores = new ArrayList<>();
                try {
                    ResultSet rs = MySQL.getInstance().executeQuery("SELECT * FROM `TOWNY_TOWNS` WHERE name = ?", town.getName());
                    if (rs.next()) {
                        Integer pr = rs.getInt("WarProc");
                        lores.add("&fВаш процент производства равен: &#ffdaa4" + pr + "%");
                    } else {
                        lores.add("&fВаш процент производства равен: &#ffdaa4нет данных");
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                return ItemCreator.of(CompMaterial.FIRE_CHARGE)
                        .name("&#FFDAA4Информация о вашем военном заводе:")
                        .lore(lores)
                        .make();
            }
        };
    }

    /**
     * Получить кнопку, при нажатии на которую открывается меню выбора производимого оружия
     */
    private Button getPistolGunsMenuButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                if (!checkInTown()) return;
                new GunsMenu(player, townData).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.FIRE_CHARGE)
                        .name("&#FFDAA4Выбрать тип производимого оружия")
                        .lore("&7Нажмите ЛКМ")
                        .make();
            }
        };
    }

    private Button getPatoronButton() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                if(!checkInTown())return;
                new patrongui(player, townData).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.FLINT_AND_STEEL)
                        .name("&#FFDAA4Производство патронов")
                        .lore("&7Нажмите ЛКМ")
                        .make();
            }
        };
    }


    /**
     * Получить кнопку, при нажатии на которую открывается меню статистики произведенного оружия
     */
    private Button getStatisticsButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                if (!checkInTown()) return;
                new MilitaryStatisticsMenu(player, townData).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.WRITABLE_BOOK)
                        .name("&#FFDAA4Статистика производства")
                        .lore("&7Нажмите ЛКМ")
                        .make();
            }
        };
    }

    /**
     * Получить кнопку, при нажатии на которую открывается инвентарь военного завода
     */
    private Button getFactoryInventoryButton() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                if (!checkInTown()) return;
                if (townData.isMilitaryFactoryMenuOpened()){
                    Player viewer = townData.getMilitaryInventoryMenu().getPlayer();
                    Main.getInstance().getChatUtility().sendColoredMessage(player, "Инвентарь завода сейчас просматривает " + viewer.getName() + ".");
                    closeMenu();
                    return;
                }
                new MilitaryInventoryMenu(player).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.IRON_INGOT)
                        .name("&#FFDAA4Инвентарь завода")
                        .lore("&7Здесь вы можете положить ресурсы", "и забрать готовое оружие.", "&7Нажмите ЛКМ")
                        .make();
            }
        };
    }

    /**
     * Проверить, состоит ли игрок в городе. Если нет - закрыть меню и вывести сообщение.
     */
    private boolean checkInTown(){
        if (!TownData.isPlayerInTown(getPlayer())){
            closeMenu();
            Main.getInstance().getChatUtility().sendColoredMessage(getPlayer(), Settings.NOT_IN_TOWN_MESSAGE);
            return false;
        }
        return true;
    }
}
