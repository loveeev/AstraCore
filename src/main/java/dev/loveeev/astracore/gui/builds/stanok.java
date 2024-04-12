package dev.loveeev.astracore.gui.builds;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.module.impl.ExchangeService;
import dev.loveeev.astracore.util.ItemBuilder;
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
public class stanok extends AdvancedMenu {

    ExchangeService exchangeService;
    Main plugin;

    private final TownData townData;

    public stanok(Player player,Main plugin,TownData townData) {
        super(player);
        this.townData = townData;
        this.plugin = plugin;
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }

    @Override
    protected void setup() {
        MySQL mySQL = MySQL.getInstance();
        setSize(9*3);
        setTitle("Текстильная фабрика");

        addButton(12,barrier());
        addButton(14,barrierf());

        // Установить обертку на черную стеклянную панель
        setWrapper(CompMaterial.BLACK_STAINED_GLASS_PANE);

        // Установить закрытые слоты (на них будет обертка, которая установлена выше)
        setLockedSlots(0,1,2,3,4,5,6,7,8,9,10,11,13,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35);
    }
    /**
     * Получить кнопку, при нажатии на которую открывается меню выбора производимого оружия
     */

    private Button barrier() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                Material material1 = Material.IRON_INGOT;
                int amountitem1 = 24;
                Material material2 = Material.LEATHER;
                int amountitem2 = 16;
                Material material3 = Material.STRING;
                int amountitem3 = 8;
                if (exchangeService.getItemAmount(player, material1) < amountitem1) {
                    plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов");
                    return;
                }
                if (exchangeService.getItemAmount(player, material2) < amountitem2) {
                    plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов");
                    return;
                }
                if (exchangeService.getItemAmount(player, material3) < amountitem3) {
                    plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов");
                    return;
                }
                exchangeService.removeitem(player, amountitem1, material1);
                exchangeService.removeitem(player, amountitem2, material2);
                exchangeService.removeitem(player, amountitem3, material3);
                Resident resident = TownyAPI.getInstance().getResident(player);
                if (resident.hasNation()) {
                    MySQL mySQL = MySQL.getInstance();
                    Nation nation = TownyAPI.getInstance().getNation(player);
                    mySQL.selectstring("TOWNY_NATIONS", "name", nation.getName(), "setnation", "integer");
                    Integer set =(Integer) Main.getInstance().getseceltinteger();
                    mySQL.selectstring("TOWNY_NATIONS", "name", nation.getName(), "setnationc", "integer");
                    Integer setc = (Integer)Main.getInstance().getseceltinteger();
                    mySQL.selectstring("TOWNY_NATIONS", "name", nation.getName(), "setnationl", "integer");
                    Integer setl = (Integer)Main.getInstance().getseceltinteger();
                    mySQL.selectstring("TOWNY_NATIONS", "name", nation.getName(), "setnationb", "integer");
                    Integer setb = (Integer)Main.getInstance().getseceltinteger();
                    getitem(set, setc, setl, setb, player);
                } else {
                    ItemStack HELMET = new ItemStack(ItemBuilder
                            .fromMaterial(Material.DIAMOND_HELMET)
                            .setName("Национальный шлем")
                            .build());
                    player.getInventory().addItem(HELMET);
                    player.updateInventory();
                    ItemStack CHEST = new ItemStack(ItemBuilder
                            .fromMaterial(Material.DIAMOND_CHESTPLATE)
                            .setName("Национальный нагрудник")
                            .build());
                    player.getInventory().addItem(CHEST);
                    player.updateInventory();
                    ItemStack LEGGINGS = new ItemStack(ItemBuilder
                            .fromMaterial(Material.DIAMOND_LEGGINGS)
                            .setName("Национальные поножи")
                            .build());
                    player.getInventory().addItem(LEGGINGS);
                    player.updateInventory();
                    ItemStack BOOTS = new ItemStack(ItemBuilder
                            .fromMaterial(Material.DIAMOND_BOOTS)
                            .setName("Национальные ботинки")
                            .build());
                    player.getInventory().addItem(BOOTS);
                    player.updateInventory();
                }
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Успешно!");
            }
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.IRON_CHESTPLATE)
                        .name("Получить сет своей нации.")
                        .lore("",
                                "&7 - Железный слиток: &#FFDAA424",
                                "&7 - Кожа: &#FFDAA416",
                                "&7 - Нить: &#FFDAA48"

                        )
                        .make();
            }
        };
    }
            private Button barrierf(){
                return new Button() {
                    @Override
                    public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                        Material material1 = Material.IRON_INGOT;
                        int amountitem1 = 24;
                        Material material2 = Material.LEATHER;
                        int amountitem2 = 16;
                        Material material3 = Material.STRING;
                        int amountitem3 = 8;
                        if (exchangeService.getItemAmount(player, material1) < amountitem1) {
                            plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов");
                            return;
                        }
                        if (exchangeService.getItemAmount(player, material2) < amountitem2) {
                            plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов");
                            return;
                        }
                        if (exchangeService.getItemAmount(player, material3) < amountitem3) {
                            plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов");
                            return;
                        }
                        exchangeService.removeitem(player, amountitem1, material1);
                        exchangeService.removeitem(player, amountitem2, material2);
                        exchangeService.removeitem(player, amountitem3, material3);
                        Resident resident = TownyAPI.getInstance().getResident(player);
                        if (resident.isMayor() | resident.hasTownRank("assistant")) {
                            if (resident.hasNation()) {
                                MySQL mySQL = MySQL.getInstance();
                                Nation nation = TownyAPI.getInstance().getNation(player);
                                mySQL.selectstring("TOWNY_NATIONS", "name", nation.getName(), "setnationkomand", "integer");
                                Integer set = (Integer)Main.getInstance().getseceltinteger();
                                mySQL.selectstring("TOWNY_NATIONS", "name", nation.getName(), "setnationckomand", "integer");
                                Integer setc =(Integer) Main.getInstance().getseceltinteger();
                                mySQL.selectstring("TOWNY_NATIONS", "name", nation.getName(), "setnationlkomand", "integer");
                                Integer setl = (Integer)Main.getInstance().getseceltinteger();
                                mySQL.selectstring("TOWNY_NATIONS", "name", nation.getName(), "setnationbkomand", "integer");
                                Integer setb =(Integer) Main.getInstance().getseceltinteger();
                                getitem(set, setc, setl, setb, player);
                            } else {
                                ItemStack HELMET = new ItemStack(ItemBuilder
                                        .fromMaterial(Material.DIAMOND_HELMET)
                                        .setName("Национальный шлем")
                                        .build());
                                player.getInventory().addItem(HELMET);
                                player.updateInventory();
                                ItemStack CHEST = new ItemStack(ItemBuilder
                                        .fromMaterial(Material.DIAMOND_CHESTPLATE)
                                        .setName("Национальный нагрудник")
                                        .build());
                                player.getInventory().addItem(CHEST);
                                player.updateInventory();
                                ItemStack LEGGINGS = new ItemStack(ItemBuilder
                                        .fromMaterial(Material.DIAMOND_LEGGINGS)
                                        .setName("Национальные поножи")
                                        .build());
                                player.getInventory().addItem(LEGGINGS);
                                player.updateInventory();
                                ItemStack BOOTS = new ItemStack(ItemBuilder
                                        .fromMaterial(Material.DIAMOND_BOOTS)
                                        .setName("Национальные ботинки")
                                        .build());
                                player.getInventory().addItem(BOOTS);
                                player.updateInventory();
                            }
                            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Успешно!");
                        }else {
                            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Для получения сета командира вы должны быть мэром.");
                        }
                    }
            @Override
            public ItemStack getItem() {
                        Resident resident = TownyAPI.getInstance().getResident(getPlayer());
                        if(!resident.isMayor() | resident.hasTownRank("assistant")) {
                            return ItemCreator.of(CompMaterial.DIAMOND_CHESTPLATE)
                                    .name("Получить сет командира своей нации.")
                                    .lore("",
                                            "&7 - Железный слиток: &#FFDAA424",
                                            "&7 - Кожа: &#FFDAA416",
                                            "&7 - Нить: &#FFDAA48"

                                    )
                                    .make();
                        }else {
                            return ItemCreator.of(CompMaterial.DIAMOND_CHESTPLATE)
                                    .name("Получить сет командира своей нации.")
                                    .lore("",
                                            "&4Доступно только мэру и ассистенту города."

                                    )
                                    .make();
                        }
            }
        };
    }

            private Button barrierr(){
                return new Button() {
                    @Override
                    public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                        Material material1 = Material.IRON_INGOT;
                        int amountitem1 = 24;
                        Material material2 = Material.LEATHER;
                        int amountitem2 = 16;
                        Material material3 = Material.STRING;
                        int amountitem3 = 8;
                        if (exchangeService.getItemAmount(player, material1) < amountitem1) {
                            plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов");
                            return;
                        }
                        if(exchangeService.getItemAmount(player, material2) < amountitem2){
                            plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов");
                            return;
                        }
                        if(exchangeService.getItemAmount(player, material3) < amountitem3) {
                            plugin.getChatUtility().sendErrorNotification(player, "У вас недостаточно ресурсов");
                            return;
                        }
                        exchangeService.removeitem(player, amountitem1, material1);
                        exchangeService.removeitem(player, amountitem2, material2);
                        exchangeService.removeitem(player, amountitem3, material3);
                        Resident resident = TownyAPI.getInstance().getResident(player);
                        if (resident.hasNation()) {
                            MySQL mySQL = MySQL.getInstance();
                            Nation nation = TownyAPI.getInstance().getNation(player);
                            mySQL.selectstring("TOWNY_NATIONS", "name", nation.getName(), "setnation", "integer");
                            Integer set = (Integer)Main.getInstance().getseceltinteger();
                            mySQL.selectstring("TOWNY_NATIONS", "name", nation.getName(), "setnationc", "integer");
                            Integer setc = (Integer)Main.getInstance().getseceltinteger();
                            mySQL.selectstring("TOWNY_NATIONS", "name", nation.getName(), "setnationl", "integer");
                            Integer setl = (Integer)Main.getInstance().getseceltinteger();
                            mySQL.selectstring("TOWNY_NATIONS", "name", nation.getName(), "setnationb", "integer");
                            Integer setb = (Integer)Main.getInstance().getseceltinteger();
                            getitem(set,setc,setl,setb, player);
                        } else {
                            ItemStack HELMET = new ItemStack(ItemBuilder
                                    .fromMaterial(Material.DIAMOND_HELMET)
                                    .setName("Национальный шлем")
                                    .build());
                            player.getInventory().addItem(HELMET);
                            player.updateInventory();
                            ItemStack CHEST = new ItemStack(ItemBuilder
                                    .fromMaterial(Material.DIAMOND_CHESTPLATE)
                                    .setName("Национальный нагрудник")
                                    .build());
                            player.getInventory().addItem(CHEST);
                            player.updateInventory();
                            ItemStack LEGGINGS = new ItemStack(ItemBuilder
                                    .fromMaterial(Material.DIAMOND_LEGGINGS)
                                    .setName("Национальные поножи")
                                    .build());
                            player.getInventory().addItem(LEGGINGS);
                            player.updateInventory();
                            ItemStack BOOTS = new ItemStack(ItemBuilder
                                    .fromMaterial(Material.DIAMOND_BOOTS)
                                    .setName("Национальные ботинки")
                                    .build());
                            player.getInventory().addItem(BOOTS);
                            player.updateInventory();
                        }
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Успешно!");
                    }
                    @Override
                    public ItemStack getItem() {
                        return ItemCreator.of(CompMaterial.DIAMOND_CHESTPLATE)
                                .name("Получить сет своей нации.")
                                .lore("",
                                        "&7 - Железный слиток: &#FFDAA424",
                                        "&7 - Кожа: &#FFDAA416",
                                        "&7 - Нить: &#FFDAA48"

                                )
                                .make();
                    }
                };
            }

    public void getitem(Integer modeldatah,Integer modeldatac,Integer modeldatal,Integer modeldatab,Player player){
        ItemStack HELMET = new ItemStack(ItemBuilder
                .fromMaterial(Material.DIAMOND_HELMET)
                .setName("Национальный шлем")
                .setCustomModelData(modeldatah)
                .build());
        player.getInventory().addItem(HELMET);
        player.updateInventory();
        ItemStack CHEST = new ItemStack(ItemBuilder
                .fromMaterial(Material.DIAMOND_CHESTPLATE)
                .setName("Национальный нагрудник")
                .setCustomModelData(modeldatac)
                .build());
        player.getInventory().addItem(CHEST);
        player.updateInventory();
        ItemStack LEGGINGS = new ItemStack(ItemBuilder
                .fromMaterial(Material.DIAMOND_LEGGINGS)
                .setName("Национальные поножи")
                .setCustomModelData(modeldatal)
                .build());
        player.getInventory().addItem(LEGGINGS);
        player.updateInventory();
        ItemStack BOOTS = new ItemStack(ItemBuilder
                .fromMaterial(Material.DIAMOND_BOOTS)
                .setName("Национальные ботинки")
                .setCustomModelData(modeldatab)
                .build());
        player.getInventory().addItem(BOOTS);
        player.updateInventory();
    }
}
