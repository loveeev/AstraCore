package dev.loveeev.astracore.gui.impl;

import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
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
public class technolagi extends AdvancedMenu {

    Main plugin;

    public technolagi(Player player,Main plugin) {
        super(player);
        this.plugin = plugin;
    }

    @Override
    protected void setup() {
        MySQL mySQL = MySQL.getInstance();
        setSize(9*3);
        setTitle("Технологии");

        addButton(13,notunlock("pon",10.0,1));

        setWrapper(CompMaterial.BLACK_STAINED_GLASS_PANE);

        setLockedSlots(0,1,2,3,4,5,6,7,8,9,10,11,12,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35);
    }

    private Button notunlock(String name,Double price, Integer p){
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, AdvancedMenu menu, ClickType click) {

            }
            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.IRON_CHESTPLATE)
                        .name(name)
                        .lore("Дает: &#ffdaa4Доступ к ", "&fВы можете купить ее за: &#ffdaa4" + price,"&fДля покупки нажмите &#FFDAA4ПКМ")
                        .make();
            }
        };
    }
}
