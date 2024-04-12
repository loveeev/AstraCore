package dev.loveeev.astracore.gui.PostOffice;

import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.database.PostOfficeDatabaseManager;
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
import java.util.List;

public class CenteredMenu extends AdvancedMenu {
    PostOfficeDatabaseManager databaseManager = new PostOfficeDatabaseManager();
    public CenteredMenu(Player player) {
        super(player);
    }

    @Override
    protected void setup() {
        setSize(9*6);
        setTitle("Меню почты");
        setWrapper(ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE)
                .name("")
                .make());
        setLockedSlots(45,46,47,48,49,50,51,52,53);
        int columnCountForPlayer = databaseManager.getColumnCountForPlayer(getPlayer());
        if(columnCountForPlayer == 0){
            addButton(columnCountForPlayer,button());
        }
        if(columnCountForPlayer < 45){
        for(int i = 0; i < columnCountForPlayer; i++) {
            addButton(i, getItem());
        }
        }else {
            closeMenu();
            Main.getInstance().getChatUtility().sendSuccessNotification(getPlayer(),"Обратитесь к администрации,у вас слишком много писем.");
        }
    }
    private Button getItem() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                try {
                    ResultSet resultSet = MySQL.getInstance().executeQuery("SELECT * FROM `PostOfficeBase` WHERE player = ?", player.getName());
                    if (resultSet.next()) { // Проверяем, есть ли строки в результате запроса

                        int amount = resultSet.getInt("amount");
                        String s = resultSet.getString("data");
                        System.out.println(amount + s);
                        ItemStack itemStack = new ItemStack(ItemCreator.of(CompMaterial.GOLD_INGOT)
                                .amount(amount)
                                .make());
                        player.getInventory().addItem(itemStack);
                        databaseManager.removeMessage(player.getName(), amount, s);
                        closeMenu();
                    } else {
                        // Если нет строк в результате запроса, отправляем сообщение об ошибке или делаем что-то еще
                        Main.getInstance().getChatUtility().sendErrorNotification(player, "Вы уже забрали данное письмо.");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public ItemStack getItem() {
                List<String> lores = new ArrayList<>();
                lores.add("&fКакие-то предметы с аукционна.");
                lores.add("&fНажмите &#ffdaa4ЛКМ,чтобы забрать.");
                return ItemCreator.of(CompMaterial.GOLD_INGOT)
                        .name("&#ffdaa4Предмет доставленный почтой.")
                        .lore(lores)
                        .make();
            }
        };
    }

    private Button button(){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player,"Похоже вы не кому не нужны :(");
            }
            @Override
            public ItemStack getItem() {

                return ItemCreator.of(CompMaterial.GOLD_INGOT)
                        .name("&#ffdaa4У вас нет предметов на почте :(")
                        .make();
            }
        };
    }
}
