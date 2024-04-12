package dev.loveeev.astracore.gui.builds;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.gui.impl.BuildingMenu;
import dev.loveeev.astracore.settings.Settings;
import org.bukkit.Material;
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
public class fuelcraftzavod extends AdvancedMenu {

    Main plugin;

    private final TownData townData;

    public fuelcraftzavod(Player player, TownData townData,Main plugin) {
        super(player);
        this.townData = townData;
        this.plugin = plugin;
    }

    @Override
    protected void setup() {
        setSize(9 * 3);
        setTitle("Нефтеперерабатывающая станция");

        // Добавить предметы и кнопки в меню
        addButton(14, getmoney());
        addButton(13, gettopfuel());
        addButton(12, getcoal());
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

    private Button getmoney() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                Double price = Settings.INKPRICE;
                Double balance = Main.getInstance().getEconomy().getBalance(player);
                if(balance>=price){
                    Main.getInstance().getEconomy().withdrawPlayer(player,price);
                    player.getInventory().addItem(new ItemStack(Material.GLOW_INK_SAC,1));
                    player.updateInventory();
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы купили 1 светящийся мешочек");
                    MySQL mySQL = MySQL.getInstance();
                    Town town = TownyAPI.getInstance().getTown(player);
                    mySQL.selectstring("TOWNY_TOWNS","name",town.getName(),"statfuelglow","integer");
                    Integer stat = (Integer)Main.getInstance().getseceltinteger() + 1;
                    mySQL.updateint("TOWNY_TOWNS","statfuelglow","name",stat,town.getName());
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас недостаточно средств!");
                }
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.WARPED_DOOR)
                        .name("&#FFDAA4Покупка")
                        .lore(" ", "&7Купить 1 светящийся мешочек за 10 монет", "&7Нажмите ЛКМ")
                        .make();
            }
        };
    }

    private Button getcoal() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                if (player.getInventory().containsAtLeast(new ItemStack(Material.INK_SAC), 1)) {
                    player.getInventory().removeItem(new ItemStack(Material.INK_SAC, 1));
                    player.getInventory().addItem(new ItemStack(Material.COAL,2));
                    player.updateInventory();
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы обменяли 1 чернильный мешочек на 1 уголь.");
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас недостаточно ресурсов!");
                }
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.COAL)
                        .name("&#FFDAA4Обмен")
                        .lore(" ", "&7Обменять 1 чернильный мешочек на 2 угля." ,"&7Нажмите ЛКМ")
                        .make();
            }
        };
    }

    private Button gettopfuel() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                if (player.getInventory().containsAtLeast(new ItemStack(Material.INK_SAC), 2)) {
                    player.getInventory().removeItem(new ItemStack(Material.INK_SAC, 2));
                    player.getInventory().addItem(new ItemStack(Material.GLOW_INK_SAC,1));
                    player.updateInventory();
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы обменяли 2 чернильных мешочка на 1 светящийся.");
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player,"У вас недостаточно ресурсов!");
                }
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.GLOW_INK_SAC)
                        .name("&#FFDAA4Обмен")
                        .lore(" ", "&7Обменять 2 чернильных мешочка на светящийся.","&7Нажмите ЛКМ" )
                        .make();
            }
        };
    }


    private boolean checkInTown() {
        if (!TownData.isPlayerInTown(getPlayer())) {
            closeMenu();
            Main.getInstance().getChatUtility().sendColoredMessage(getPlayer(), Settings.NOT_IN_TOWN_MESSAGE);
            return false;
        }
        return true;
    }
}
