package dev.loveeev.astracore.gui.builds;

import dev.loveeev.astracore.Auction.Auction;
import dev.loveeev.astracore.Auction.ItemToSell;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.AuctionDatabaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.SoundUtil;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.AdvancedMenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.List;

/**
 * Меню предметов, выставленных игроком
 */
public class SelfAuctionMenu extends AdvancedMenuPagged<ItemToSell> {
    public SelfAuctionMenu(Player player) {
        super(player);
    }

    @Override
    protected List<ItemToSell> getElements() {
        return Auction.getInstance().getPlayerItems(getPlayer());
    }

    @Override
    protected ItemStack convertToItemStack(ItemToSell itemToSell) {
        return itemToSell.getOwnMenuView();
    }

    @Override
    protected void setup() {
        setSize(9*6);
        setTitle("Ваши предметы");
        setPreviousButtonSlot(45);
        setNextButtonSlot(53);
        setPageButtonsAlwaysEnabled(true);
        setLockedSlots(45, 46, 47, 48, 49, 50, 51, 52, 53);

        addButton(51, getAuctionInventoryButton());
        setPreviousButtonItem(ItemCreator.of(CompMaterial.SPECTRAL_ARROW).name("&#FFDAA4Предыдущая страница").hideTags(true).make());
        setNextButtonItem(ItemCreator.of(CompMaterial.TIPPED_ARROW).name("&#FFDAA4Следующая страница").hideTags(true).make());
    }

    private Button getAuctionInventoryButton() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                new AuctionMenu(player).display();
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.TNT_MINECART)
                        .name("&#FFDAA4Мировой рынок")
                        .lore("&7Нажмите, чтобы посмотреть предметы,", "&7выставленные всеми игроками.")
                        .make();
            }
        };
    }

    /**
     * Этот метод срабатывает, когда игрок нажимает на предмет, который можно забрать с аукциона.
     * НЕ срабатывает при нажатии на кнопки и закрытые слоты.
     */
    @Override
    protected void onElementClick(Player owner, ItemToSell sellItem, int slot, ClickType clickType) {
        // Если предмет, на который нажал игрок, уже куплен другим игроком, то выводим сообщение, воспроизводим звук и обновляем меню
        if (!Auction.getInstance().getItems().contains(sellItem)){
            SoundUtil.Play.NO(owner);
            Main.getInstance().getChatUtility().sendErrorNotification(owner, "Этот предмет уже купили.");
            refreshMenu();
            return;
        }

        // Если инвентарь игрока заполнен, то закрываем меню и выводим сообщение
        if (owner.getInventory().firstEmpty() == -1){
            Main.getInstance().getChatUtility().sendErrorNotification(owner, "Ваш инвентарь полон");
            closeMenu();
            return;
        }

        // Воспроизводим звук
        SoundUtil.Play.POP_LOW(owner);
        // Добавляем предмет в инвентарь игроку
        owner.getInventory().addItem(sellItem.getItem());
        // Удаляем предмет из списка предметов
        Auction.getInstance().getItems().remove(sellItem);
        // Удаляем предмет из базы данных
        AuctionDatabaseManager.removeAuctionItem(sellItem);
        // Обновляем меню
        refreshMenu();

        // Выводим сообщение
        Main.getInstance().getChatUtility().sendSuccessNotification(owner, "Вы забрали свой предмет с аукциона.");
    }
}
