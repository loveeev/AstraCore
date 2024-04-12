package dev.loveeev.astracore.gui;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.gui.impl.BuildingMenu;
import dev.loveeev.astracore.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.AdvancedMenu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class MoneyTrade extends AdvancedMenu {

    Main plugin;
    public MoneyTrade(Player player,Main plugin) {
        super(player);
        this.plugin = plugin;
    }

    @Override
    protected void setup() {
        setSize(9 * 3);
        setTitle(" Обменник");
        setWrapper(ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE)
                .name("")
                .make());
        setLockedSlots(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 27);
        Nation nation = TownyAPI.getInstance().getNation(getPlayer());
        try {
            ResultSet resultSet = MySQL.getInstance().executeQuery("SELECT * FROM `AstraEconomy` WHERE nation = ?", nation.getName());
            if(resultSet.next()) {
                int modeldata = resultSet.getInt("modeldata");
                String name = resultSet.getString("name");
                addButton(26, getBackButton0());
                //ПКМ
                addButton(11, TradeButton(modeldata, name, 10, 1, Material.SCULK_VEIN,Material.SCULK));
//ПРоблема с ПКМ У ВТОРОГО ПРЕДМЕТА С ЛКМ
                //ЛКМ
                addButton(12, TradeButton(modeldata, name, 2, 1, Material.SCULK_CATALYST,Material.SCULK_CATALYST));
                addButton(13, TradeButton(modeldata, name, 3, 1, Material.SCULK_SENSOR,Material.SCULK_SENSOR));
                addButton(14, TradeButton(modeldata, name, 5, 1,Material.SCULK_SHRIEKER,Material.SCULK_SHRIEKER));
                addButton(15, TradeButton(modeldata, name, 10, 1, Material.SCULK_VEIN,Material.SCULK_VEIN));

            }else {
                Main.getInstance().getLogger().log(Level.SEVERE,"Ошибка базы данных");
            }
        }catch (SQLException E){
            throw new RuntimeException(E);
        }

    }
    private Button TradeButton(Integer modeldata,String name,Integer var1,Integer var2,Material sucess,Material info) {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu advancedMenu, ClickType clickType) {
                    if (clickType.isRightClick()) {
                        int p = getItemAmount(player, Material.SCULK, modeldata);
                        if(p >= var2){
                            ItemStack itemStack = new ItemStack(ItemBuilder.fromMaterial(sucess)
                                    .setName("&#ffdaa4"+name + "&fx"+var2)
                                    .setCustomModelData(modeldata)
                                    .build());
                            ItemStack itemStackre = new ItemStack(ItemBuilder.fromMaterial(Material.SCULK)
                                    .setCustomModelData(modeldata)
                                    .setAmount(var1)
                                    .build());
                            player.getInventory().addItem(itemStack);
                            player.getInventory().removeItem(itemStackre);
                        }else {
                            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Недостаточно ресурсов.");
                        }
                    }
                    if (clickType.isLeftClick()) {
                        int p = getItemAmount(player,sucess,modeldata);
                        if(p > var1){
                            ItemStack itemStack = new ItemStack(ItemBuilder.fromMaterial(sucess)
                                    .setName("&#ffdaa4"+name + "&fx"+var1)
                                    .setCustomModelData(modeldata)
                                    .setAmount(var2)
                                    .build());
                            ItemStack itemStackre = new ItemStack(ItemBuilder.fromMaterial(Material.SCULK)
                                    .setCustomModelData(modeldata)
                                    .setAmount(var1)
                                    .build());
                            player.getInventory().addItem(itemStack);
                            player.getInventory().removeItem(itemStackre);
                        }else {
                            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Недостаточно ресурсов.");
                        }
                    }
            }

            @Override
            public ItemStack getItem() {
                List<String> lore = new ArrayList<>();
                lore.add("&fПри нажатие &#ffdaa4ПКМ &fвы обменяете &#ffdaa4"+var1+" &fна &#ffdaa4" +var2);
                lore.add("&fПри нажатие &#ffdaa4ЛКМ &fвы обменяете &#ffdaa4"+var2+" &fна &#ffdaa4" +var1);
                return ItemBuilder.fromMaterial(info)
                        .setName("&4Обменять "+var1+" единицу валюты.")
                        .setLore(lore)
                        .setCustomModelData(modeldata)
                        .build();
            }
        };
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
    public int getItemAmount(Player player,Material material,Integer modeldata) {
        AtomicInteger out = new AtomicInteger();
        Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {
            if (itemStack != null && itemStack.getType() == material && itemStack.getItemMeta().getCustomModelData() == modeldata) out.addAndGet(itemStack.getAmount());
        });
        return out.get();
    }
}
