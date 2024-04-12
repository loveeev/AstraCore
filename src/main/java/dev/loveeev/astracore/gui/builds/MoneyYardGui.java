package dev.loveeev.astracore.gui.builds;

import com.palmergames.bukkit.towny.TownyAPI;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.gui.impl.BuildingMenu;
import dev.loveeev.astracore.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;

public class MoneyYardGui extends AdvancedMenu {

    Main plugin;
    public MoneyYardGui(Player player,Main plugin) {
        super(player);
        this.plugin = plugin;
    }

    @Override
    protected void setup() {
        setSize(9*3);
        setTitle("Монетный двор");
        setWrapper(ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE)
                .name("")
                .make());
        setLockedSlots(0,1,2,3,4,5,6,7,8,9,10,11,12,14,15,16,17,19,20,21,22,23,24,25,27);
        addButton(13,getButtonInventory());
        addButton(18,getButtonTrade());
        addButton(26,getBackButton0());
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
    private Button getButtonTrade() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.ORANGE_STAINED_GLASS_PANE)
                        .name("&#ffdaa4Обменник")
                        .make();
            }
        };
    }
    private Button getButtonInventory() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                TownData townData = TownData.getOrCreate(TownyAPI.getInstance().getTown(player).getName());
                if (townData.isMoneyYardOpened()){
                    Player viewer = townData.getMoneyinv().getPlayer();
                    Main.getInstance().getChatUtility().sendColoredMessage(player, "Инвентарь сейчас просматривает " + viewer.getName() + ".");
                    closeMenu();
                    return;
                }
                new yardinv(player).display();
            }

            @Override
            public ItemStack getItem() {
                List<String> lore = new ArrayList<>();
                lore.add("&fВы получаете каждый день по &#ffdaa4" + Settings.MONEYYARD + " &fединиц вашей национальной валюты");
                return ItemCreator.of(CompMaterial.ORANGE_STAINED_GLASS_PANE)
                        .name("&#ffdaa4Валютный инвентарь нации")
                        .lore(lore)
                        .make();
            }
        };
    }
}
