package dev.loveeev.astracore.gui.builds;

import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.TownData;
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
public class patrongui extends AdvancedMenu {

    Main plugin;

    private final TownData townData;

    public patrongui(Player player, TownData townData) {
        super(player);
        this.townData = townData;
    }

    @Override
    protected void setup() {
        setSize(9 * 3);
        setTitle("Магазин патронов");

        // Добавить предметы и кнопки в меню
        addButton(0, getPistolGunsMenuButton9mm());
        addButton(1, getPatoronButton762());
        addButton(2, getPatoronButton920());
        addButton(22, getBackButton0());
        addButton(18, getPreviousButtonItem());
        addButton(26, setNextButtonItem());


        // Установить обертку на черную стеклянную панель
        setWrapper(CompMaterial.BLACK_STAINED_GLASS_PANE);

        // Установить закрытые слоты (на них будет обертка, которая установлена выше)
        setLockedSlots(18, 19, 20, 21, 22, 23, 24, 25, 26);
    }

    private Button setNextButtonItem() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {

            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.TIPPED_ARROW)
                        .name("&#FFDAA4Следующая страница")
                        .hideTags(true)
                        .make();
            }
        };
    }

    private Button getPreviousButtonItem() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {

            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.SPECTRAL_ARROW).name("&#FFDAA4Предыдущая страница").hideTags(true).make();
            }
        };
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

    private Button getPistolGunsMenuButton9mm() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                if (!checkInTown()) return;
                int gunpowderAmount = 8;

                int ironAmount = 16;

                if (player.getInventory().containsAtLeast(new ItemStack(Material.GUNPOWDER), gunpowderAmount) &&
                        player.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), ironAmount)) {
                    // Забираем ресурсы у игрока
                    player.getInventory().removeItem(new ItemStack(Material.GUNPOWDER, gunpowderAmount));
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, ironAmount));

                    // Выдаем покупку (мембрану фантома с модельдатой 6070)
                    player.getInventory().addItem(ItemCreator.of(CompMaterial.PHANTOM_MEMBRANE)
                            .name("&#ffdaa4Винтовочный патрон")
                            .modelData(192)
                            .amount(32)
                            .make());
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Успешная покупка");
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас недостаточно ресурсов для покупки!");
                }
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.PHANTOM_MEMBRANE)
                        .name("&#FFDAA4Винтовочный патрон")
                        .modelData(192)
                        .amount(32)
                        .lore(" ", "&#FFDAA4Цена: ", "&7- Железный слиток x16", "&7- Порох x8", "", "&7Для покупки - " + "&7Нажмите ЛКМ")
                        .make();
            }
        };
    }

    private Button getPatoronButton762() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                if (!checkInTown()) return;

                int gunpowderAmount = 16;
                int ironAmount = 16;
                if (player.getInventory().containsAtLeast(new ItemStack(Material.GUNPOWDER), gunpowderAmount) &&
                        player.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), ironAmount)) {
                    // Забираем ресурсы у игрока
                    player.getInventory().removeItem(new ItemStack(Material.GUNPOWDER, gunpowderAmount));
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, ironAmount));

                    // Выдаем покупку (мембрану фантома с модельдатой 6070)
                    player.getInventory().addItem(ItemCreator.of(CompMaterial.PHANTOM_MEMBRANE)
                            .name("&#FFDAA4Картечь")
                            .modelData(193)
                            .amount(8)
                            .make());
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Успешная покупка");
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас недостаточно ресурсов для покупки!");
                }
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.PHANTOM_MEMBRANE)
                        .name("&#FFDAA4Картечь")
                        .modelData(193)
                        .amount(8)
                        .lore(" ", "&#FFDAA4Цена: ", "&7- Железный слиток x16", "&7- Порох x16", "", "&7Для покупки - " + "&7Нажмите ЛКМ")
                        .make();
            }
        };
    }

    private Button getPatoronButton920() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                if (!checkInTown()) return;

                int gunpowderAmount = 4;
                int ironAmount = 16;
                if (player.getInventory().containsAtLeast(new ItemStack(Material.GUNPOWDER), gunpowderAmount) &&
                        player.getInventory().containsAtLeast(new ItemStack(Material.IRON_INGOT), ironAmount)) {
                    // Забираем ресурсы у игрока
                    player.getInventory().removeItem(new ItemStack(Material.GUNPOWDER, gunpowderAmount));
                    player.getInventory().removeItem(new ItemStack(Material.IRON_INGOT, ironAmount));

                    // Выдаем покупку (мембрану фантома с модельдатой 6070)
                    player.getInventory().addItem(ItemCreator.of(CompMaterial.PHANTOM_MEMBRANE)
                            .name("&#FFDAAAПистолетный патрон")
                            .modelData(191)
                            .amount(32)
                            .make());
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Успешная покупка");
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас недостаточно ресурсов для покупки!");
                }
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.PHANTOM_MEMBRANE)
                        .name("&#FFDAAAПистолетный патрон")
                        .modelData(191)
                        .amount(32)
                        .lore(" ", "&#FFDAA4Цена: ", "&7- Железный слиток x16", "&7- Порох x4", "", "&7Для покупки - " + "&7Нажмите ЛКМ")
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
