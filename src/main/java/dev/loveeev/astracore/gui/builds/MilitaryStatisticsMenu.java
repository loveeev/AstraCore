package dev.loveeev.astracore.gui.builds;

import dev.loveeev.astracore.data.Gun;
import dev.loveeev.astracore.data.GunStorage;
import dev.loveeev.astracore.data.TownData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.AdvancedMenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.List;

public class MilitaryStatisticsMenu extends AdvancedMenuPagged<Gun> {

    private final TownData townData;

    public MilitaryStatisticsMenu(Player player, TownData data) {
        super(player);
        this.townData = data;
    }

    @Override
    protected List<Gun> getElements() {
        return GunStorage.getGuns();
    }

    @Override
    protected ItemStack convertToItemStack(Gun gun) {
        Integer stat0 = townData.getGunStatistics().get(gun);
        int stat = stat0 == null ? 0 : stat0;

        return ItemCreator.of(gun.getItemStack())
                .lore("", "&7Произведено: " + stat + " шт.")
                .make();
    }

    @Override
    protected void setup() {
        setTitle("Статистика производства");
        setSize(9*3);
        addButton(22, getBackButton0());

        setLockedSlots(18, 19, 20, 21, 22, 23, 24, 25, 26);
        setPreviousButtonItem(ItemCreator.of(CompMaterial.SPECTRAL_ARROW).name("&#FFDAA4Предыдущая страница").hideTags(true).make());
        setNextButtonItem(ItemCreator.of(CompMaterial.TIPPED_ARROW).name("&#FFDAA4Следующая страница").hideTags(true).make());
        setPageButtonsAlwaysEnabled(true);
    }

    private Button getBackButton0() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                new WarMenu(player, townData).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.YELLOW_STAINED_GLASS_PANE)
                        .name("&#FFDAA4Назад")
                        .make();
            }
        };
    }

}
