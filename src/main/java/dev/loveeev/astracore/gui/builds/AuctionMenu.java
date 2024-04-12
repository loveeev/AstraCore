package dev.loveeev.astracore.gui.builds;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import dev.loveeev.astracore.Auction.Auction;
import dev.loveeev.astracore.Auction.ItemToSell;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.database.AuctionDatabaseManager;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.database.PostOfficeDatabaseManager;
import dev.loveeev.astracore.settings.Settings;
import dev.loveeev.astracore.util.TextUtility;
import lombok.Getter;
import me.rubix327.itemslangapi.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.SoundUtil;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.AdvancedMenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class AuctionMenu extends AdvancedMenuPagged<ItemToSell> {

    MySQL mySQL = MySQL.getInstance();
    /**
     * Список игроков, просматривающих меню аукциона.
     * Используется, чтобы обновлять меню у всех игроков, когда кто-то купил или выставил предмет на аукционе.
     */
    @Getter
    private static final List<Player> viewers = new ArrayList<>();

    public AuctionMenu(Player player) {
        super(player);
    }

    @Override
    protected List<ItemToSell> getElements() {
        return Auction.getInstance().getNotExpiredItems();
    }

    @Override
    protected ItemStack convertToItemStack(ItemToSell item) {
        if (item.isOwner(getPlayer())){
            return item.getOwnMenuView();
        } else {
            try {
                return item.getMenuView(getPlayer());
            } catch (TownyException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void setup() {
        setTitle("Мировой рынок");
        setSize(9*6);
        setLockedSlots(45, 46, 47, 48, 49, 50, 51, 52, 53);
        setPreviousButtonSlot(45);
        setNextButtonSlot(53);
        setPageButtonsAlwaysEnabled(true);

        addItem(49, getInfoItem());
        addButton(51, getSelfInventoryButton());
        setPreviousButtonItem(ItemCreator.of(CompMaterial.SPECTRAL_ARROW).name("&#FFDAA4Предыдущая страница").hideTags(true).make());
        setNextButtonItem(ItemCreator.of(CompMaterial.TIPPED_ARROW).name("&#FFDAA4Следующая страница").hideTags(true).make());
    }

    /**
     * Этот метод срабатывает, когда игрок нажимает на предмет, который можно купить на аукционе.
     * НЕ срабатывает при нажатии на кнопки и закрытые слоты.
     */
    @Override
    protected void onElementClick(Player buyer, ItemToSell sellItem, int slot, ClickType clickType) {
        double price = sellItem.getPrice();
        Nation nation = TownyAPI.getInstance().getNation(buyer);
        PostOfficeDatabaseManager postOfficeDatabaseManager = new PostOfficeDatabaseManager();
        // Проверяем, включен ли режим использования экономики сервера
        if (!Settings.MARKETMONEY) {
            // Проверяем, достаточно ли у игрока денег на балансе
            if (!Main.getInstance().getEconomy().has(buyer, price)) {
                if (sellItem.getSellerUuid() == buyer.getUniqueId()) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(buyer, "Для получение предмета, откройте 'мои предметы'");
                } else {
                    SoundUtil.Play.NO(buyer);
                    Main.getInstance().getChatUtility().sendErrorNotification(buyer, "У вас недостаточно средств для покупки.");
                }
                return;
            }
        } else {
            // Проверяем, достаточно ли у игрока золота в инвентаре
            if (sellItem.getPrice() > getItemAmount(buyer, Material.GOLD_INGOT)) {
                if (sellItem.getSellerUuid() == buyer.getUniqueId()) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(buyer, "Для получение предмета, откройте 'мои предметы'");
                } else {
                    SoundUtil.Play.NO(buyer);
                    Main.getInstance().getChatUtility().sendErrorNotification(buyer, "У вас недостаточно золота для покупки.");
                }
                return;
            }
        }

        // Если предмет, на который нажал игрок, уже куплен другим игроком, то выводим сообщение, воспроизводим звук и обновляем меню
        if (!Auction.getInstance().getItems().contains(sellItem)) {
            SoundUtil.Play.NO(buyer);
            Main.getInstance().getChatUtility().sendErrorNotification(buyer, "Этот предмет уже купили.");
            refreshMenu();
            return;
        }

        try (Connection connection = MySQL.getInstance().getCon();
             PreparedStatement ps = connection.prepareStatement("SELECT poshlin FROM AHN_ALLOW WHERE nationowner = ? AND nationallow = ?")) {
            ps.setString(1, sellItem.getNation());
            ps.setString(2, TownyAPI.getInstance().getNation(buyer).getName());

            ResultSet rs = ps.executeQuery(); // ResultSet не в try-with-resources

            if (!rs.next()) {
                if(Objects.equals(sellItem.getNation(), TownyAPI.getInstance().getNation(buyer).getName())){
                    if (!Settings.MARKETMONEY) {
                        // Снимаем деньги с баланса игрока
                        Main.getInstance().getEconomy().withdrawPlayer(buyer, price);
                        // Добавляем деньги на баланс продавца
                        Main.getInstance().getEconomy().depositPlayer(Bukkit.getPlayer(sellItem.getSellerUuid()), price);
                        SoundUtil.Play.ORB(buyer);
                        // Добавляем предмет в инвентарь игроку
                        buyer.getInventory().addItem(sellItem.getItem());
                        // Удаляем предмет из списка предметов
                        Auction.getInstance().getItems().remove(sellItem);
                        // Удаляем предмет из базы данных
                        AuctionDatabaseManager.removeAuctionItem(sellItem);

                    } else {
                        // Проверяем, достаточно ли у игрока золота в инвентаре
                        if (sellItem.getPrice() <= getItemAmount(buyer, Material.GOLD_INGOT)) {
                            // Снимаем золото с баланса игрока
                            ItemStack itemStack = new ItemStack(ItemCreator.of(CompMaterial.GOLD_INGOT)
                                    .amount(sellItem.getPrice())
                                    .make());
                            buyer.getInventory().removeItem(itemStack);
                            SoundUtil.Play.ORB(buyer);
                            // Добавляем предмет в инвентарь игроку
                            buyer.getInventory().addItem(sellItem.getItem());
                            // Удаляем предмет из списка предметов
                            Auction.getInstance().getItems().remove(sellItem);
                            // Удаляем предмет из базы данных
                            AuctionDatabaseManager.removeAuctionItem(sellItem);
                            // Сообщаем продавцу о продаже
                            postOfficeDatabaseManager.addMessage(sellItem.getSellerName(), sellItem.getPrice(),sellItem.getData());
                        } else {
                            SoundUtil.Play.NO(buyer);
                            Main.getInstance().getChatUtility().sendErrorNotification(buyer, "У вас недостаточно золота для покупки.");
                            return;
                        }
                    }
                    refreshForAllViewers();
                } else {
                    Main.getInstance().getChatUtility().sendErrorNotification(buyer, "Данная нация не добавила вас в список наций разрешенных покупать их товары!");
                    return;
                }
            } else {
                double ro = rs.getDouble("poshlin");
                if (!Main.getInstance().getEconomy().has(buyer, ro + price)) {
                    Main.getInstance().getChatUtility().sendErrorNotification(buyer, "У вас недостаточно средств для оплаты!");
                    return;
                }

                if (!Settings.MARKETMONEY) {
                    // Снимаем деньги с баланса игрока
                    Main.getInstance().getEconomy().withdrawPlayer(buyer, price);
                    // Добавляем деньги на баланс продавца
                    Main.getInstance().getEconomy().depositPlayer(Bukkit.getPlayer(sellItem.getSellerUuid()), price);
                    SoundUtil.Play.ORB(buyer);
                    // Добавляем предмет в инвентарь игроку
                    buyer.getInventory().addItem(sellItem.getItem());
                    // Удаляем предмет из списка предметов
                    Auction.getInstance().getItems().remove(sellItem);
                    // Удаляем предмет из базы данных
                    AuctionDatabaseManager.removeAuctionItem(sellItem);
                } else {
                    // Проверяем, достаточно ли у игрока золота в инвентаре
                    if (sellItem.getPrice() <= getItemAmount(buyer, Material.GOLD_INGOT)) {
                        // Снимаем золото с баланса игрока
                        ItemStack itemStack = new ItemStack(ItemCreator.of(CompMaterial.GOLD_INGOT)
                                .amount(sellItem.getPrice())
                                .make());
                        buyer.getInventory().removeItem(itemStack);
                        SoundUtil.Play.ORB(buyer);
                        // Добавляем предмет в инвентарь игроку
                        buyer.getInventory().addItem(sellItem.getItem());
                        // Удаляем предмет из списка предметов
                        Auction.getInstance().getItems().remove(sellItem);
                        // Удаляем предмет из базы данных
                        AuctionDatabaseManager.removeAuctionItem(sellItem);
                        // Сообщаем продавцу о продаже
                        postOfficeDatabaseManager.addMessage(sellItem.getSellerName(), sellItem.getPrice(),sellItem.getData());
                    } else {
                        SoundUtil.Play.NO(buyer);
                        Main.getInstance().getChatUtility().sendErrorNotification(buyer, "У вас недостаточно золота для покупки.");
                        return;
                    }
                }
                refreshForAllViewers();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Выводим сообщения
        if (sellItem.getSellerUuid().equals(buyer.getUniqueId())) {
            Main.getInstance().getChatUtility().sendSuccessNotification(buyer, "Вы забрали свой предмет с аукциона.");
        } else {
            Player seller = Bukkit.getPlayer(sellItem.getSellerUuid());
            if (seller != null) {
                Main.getInstance().getChatUtility().sendSuccessNotification(seller, "Вы продали " + TextUtility.colorize("&#FFDAA4") + Main.getInstance().getLangAPI().translate(sellItem.getItem().getType(), Lang.RU_RU)  + " &fза " + TextUtility.colorize("&#FFDAA4") + price);
            }

            Main.getInstance().getChatUtility().sendSuccessNotification(buyer, "Вы успешно купили " + TextUtility.colorize("&#FFDAA4") + Main.getInstance().getLangAPI().translate(sellItem.getItem().getType(), Lang.RU_RU) + "&f за " + TextUtility.colorize("&#FFDAA4") + price);
        }
    }


    /**
     * Получить кнопку открытия меню предметов, выставленных этим игроком на аукцион
     */
    private org.mineacademy.fo.menu.button.Button getSelfInventoryButton(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                new SelfAuctionMenu(player).display();
                viewers.remove(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.CHEST_MINECART)
                        .name("&#FFDAA4Мои предметы")
                        .lore("&7Нажмите, чтобы посмотреть предметы,", "&7которые вы выставили на продажу.")
                        .make();
            }
        };
    }

    private ItemStack getInfoItem(){
        return ItemCreator.of(CompMaterial.TOTEM_OF_UNDYING)
                .name("&#FFDAA4Информация")
                .lore("&fДля выставления предмета используйте /ah sell цена",
                        "&fДля использование мирового рынка у вас должен быть построен Порт, и не ломаться.",
                        "&fВсе предметы которые вы тут видите, можно покупать за внутри игровую валюту.",
                        "&fДля того, чтобы забрать золото с продажи /mailmenu")
                .make();
    }

    @Override
    protected void onMenuClose(Player player, Inventory inventory) {
        viewers.remove(player);
    }

    /**
     * Этот метод обновляет меню для всех игроков, просматривающих аукцион.
     * Выше (внутри метода onElementClick) есть подробное описание того, как он работает.
     */
    public static void refreshForAllViewers(){
        for (Player viewer : viewers) {
            if (viewer == null || !viewer.isOnline()) continue;
            AdvancedMenu menu = TownData.getOpenedMenu(viewer);
            if (menu instanceof AuctionMenu am){
                am.refreshMenu();
            }
        }
    }
    public int getItemAmount(Player player, Material material) {
        AtomicInteger out = new AtomicInteger();
        Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {
            if (itemStack != null && itemStack.getType() == material) out.addAndGet(itemStack.getAmount());
        });
        return out.get();
    }
}
