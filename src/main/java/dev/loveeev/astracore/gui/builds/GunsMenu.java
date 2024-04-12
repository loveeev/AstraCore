package dev.loveeev.astracore.gui.builds;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.DependencyManager;
import dev.loveeev.astracore.data.Gun;
import dev.loveeev.astracore.data.GunStorage;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.settings.Settings;
import me.rubix327.itemslangapi.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.AdvancedMenuPagged;
import org.mineacademy.fo.menu.MenuSlots;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;

public class GunsMenu extends AdvancedMenuPagged<Gun> {

    private final TownData townData;

    public GunsMenu(Player player, TownData townData) {
        super(player);
        this.townData = townData;
    }

    @Override
    protected List<Gun> getElements() {
        return GunStorage.getGuns();
    }

    @Override
    protected ItemStack convertToItemStack(@NotNull Gun gun) {
        List<String> lores = new ArrayList<>();
        lores.add("");
        lores.add("&7Урон:&#FFDAA4" + " " + gun.getDamage());
        lores.add("&7Боеприпасы:" + " " + gun.getPatron());
        lores.add("&7Стоимость:");
        lores.add("");
        for (ItemStack itemStack : gun.getCost()) {
            String itemName;

            // Если у предмета есть кастомное название, то используем его
            if (itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName()){
                itemName = itemStack.getItemMeta().getDisplayName();
            // Если нет кастомного названия, и загружен ItemsLangApi, то переводим материал предмета на русский
            } else if (DependencyManager.ITEMS_LANG_API.isLoaded()){
                itemName = Main.getInstance().getLangAPI().translate(itemStack, Lang.RU_RU);
            // Если нет кастомного названия и ItemsLangApi не загружен, то выводим материал как есть
            } else {
                itemName = itemStack.getType().name();
            }

            // Если город премиальный, то показываем цену в 2 раза ниже и выделяем желтым цветом
            if (townData.isPremium()){
                int price = (int) (itemStack.getAmount() / Settings.PREMIUM_TOWN_DISCOUNT);
                lores.add("- " + itemName + " &ex" + price);
            } else {
                lores.add("- " + itemName + " x" + itemStack.getAmount());
            }
        }
        lores.add("");
        lores.add("&7Объем производства: &#ffdaaa" + gun.getProc() +"&7%");
        lores.add("");
        lores.add("&7Выбрать для производства");
        lores.add("&7Нажмите ЛКМ");

        return ItemCreator.of(CompMaterial.CROSSBOW)
                .name("&#FFDAA4" + gun.getName())
                .modelData(gun.getModelData())
                .lore(lores)
                .make();
    }

    @Override
    protected void setup() {
        setSize(9*3);
        setTitle("Выбрать оружие");
        setLockedSlots(MenuSlots.Shape.BOUNDS);
        addButton(22, getBackButton0());
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
                        .name("&eНазад")
                        .make();
            }
        };
    }

    @Override
    protected void onElementClick(Player player, Gun gun, int slot, ClickType clickType) {
        try{
            TownData data = TownData.getOrCreate(getPlayer());
            data.setProducingGun(gun);
            new WarMenu(player, data).display();
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "&fВы выбрали &#FFDAA4" + gun.getName() + "&f в качестве производимого оружия.");
        } catch (NotRegisteredException e){
            this.closeMenu();
            Main.getInstance().getChatUtility().sendColoredMessage(player, Settings.NOT_IN_TOWN_MESSAGE);
        }
    }

}
