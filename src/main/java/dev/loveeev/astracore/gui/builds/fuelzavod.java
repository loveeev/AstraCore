package dev.loveeev.astracore.gui.builds;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.gui.impl.BuildingMenu;
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
public class fuelzavod extends AdvancedMenu {

    Main plugin;

    private final TownData townData;

    public fuelzavod(Player player, TownData townData,Main plugin) {
        super(player);
        this.townData = townData;
        this.plugin = plugin;
    }

    @Override
    protected void setup() {
        setSize(9 * 3);
        setTitle("Нефтяная станция");

        // Добавить предметы и кнопки в меню
        addButton(14, getStatistic());
        addButton(13, getinv());
        addButton(12, getprice());
        addButton(26, getBackButton0());


        // Установить обертку на черную стеклянную панель
        setWrapper(CompMaterial.BLACK_STAINED_GLASS_PANE);

        setLockedSlots(0,1,2,3,4,5,6,7,8,9,10,11,15,16,17,18,19,20,21,22,23,24,25,26,27,28);
    }

    private Button getBackButton0() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                new BuildingMenu(player).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.RED_STAINED_GLASS_PANE)
                        .name("&4Выйти из меню.")
                        .make();
            }
        };
    }

    private Button getStatistic() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                new fuelstatistic(player,townData).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.PAPER)
                        .name("&#FFDAA4Статистика производства")
                        .lore(" ", "&7Статистика", "&7Нажмите ЛКМ")
                        .make();
            }
        };
    }

    private Button getinv() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                    if (townData.isFuelMenuOpened()){
                        Player viewer = townData.getInvfuel().getPlayer();
                        Main.getInstance().getChatUtility().sendColoredMessage(player, "Инвентарь завода сейчас просматривает " + viewer.getName() + ".");
                        closeMenu();
                        return;
                    }
                    new invFuel(player).display();
                }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.CHEST)
                        .name("&#FFDAA4Инвентарь станции")
                        .lore(" ", "&7Для открытия" + "&7Нажмите ЛКМ")
                        .make();
            }
        };
    }

    private Button getprice() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
            }

            @Override
            public ItemStack getItem() {
                MySQL mySQL = MySQL.getInstance();
                Town town = TownyAPI.getInstance().getTown(getPlayer());
                mySQL.selectstring("TOWNY_TOWNS","name",town.getName(),"fuel","integer");
                Integer proiz = (Integer)Main.getInstance().getseceltinteger();
                return ItemCreator.of(CompMaterial.INK_SAC)
                        .name("&#FFDAA4Производиться")
                        .lore(" ", "&7В день производиться: &#FFDAA4" + proiz)
                        .make();
            }
        };
    }
}
