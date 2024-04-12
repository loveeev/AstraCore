package dev.loveeev.astracore.gui.builds;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.database.MySQL;
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
public class fuelstatistic extends AdvancedMenu {

    Main plugin;

    private final TownData townData;
    MySQL mySQL = MySQL.getInstance();

    public fuelstatistic(Player player, TownData townData) {
        super(player);
        this.townData = townData;
    }

    @Override
    protected void setup() {
        setSize(9 * 3);
        setTitle("Статистика производства");

        // Добавить предметы и кнопки в меню
        addButton(0, getInk());
        addButton(1, getGlowInk());
        addButton(26, getBackButton0());


        // Установить обертку на черную стеклянную панель
        setWrapper(CompMaterial.BLACK_STAINED_GLASS_PANE);

        setLockedSlots(18,19,20,21,22,23,24,25);
    }

    private Button getBackButton0() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                new fuelzavod(player,townData,plugin).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.RED_STAINED_GLASS_PANE)
                        .name("&4Выйти из меню.")
                        .make();
            }
        };
    }

    private Button getInk() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                new fuelstatistic(player,townData).display();
            }

            @Override
            public ItemStack getItem() {
                Town town = TownyAPI.getInstance().getTown(getPlayer());
                mySQL.selectstring("TOWNY_TOWNS","name",town.getName(),"statfuel","string");
                Object proiz = Main.getInstance().getseceltinteger();
                if(proiz == null){
                    proiz = "0";
                }
                return ItemCreator.of(CompMaterial.INK_SAC)
                        .name("&#FFDAA4Чернильный мешок")
                        .lore(" ", "&7Произведено: &#FFDAA4" + proiz)
                        .make();
            }
        };
    }

    private Button getGlowInk() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
            }

            @Override
            public ItemStack getItem() {
                Town town = TownyAPI.getInstance().getTown(getPlayer());
                mySQL.selectstring("TOWNY_TOWNS","name",town.getName(),"statfuelglow","string");
                Object proiz = Main.getInstance().getseceltinteger();
                if(proiz == null){
                    proiz = "0";
                }
                return ItemCreator.of(CompMaterial.GLOW_INK_SAC)
                        .name("&#FFDAA4Светящийся чернильный мешок")
                        .lore(" ", "&7Произведено: &#FFDAA4" + proiz)
                        .make();
            }
        };
    }
}
