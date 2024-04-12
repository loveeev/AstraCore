package dev.loveeev.astracore.gui;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.samjakob.spigui.buttons.SGButton;
import com.samjakob.spigui.menu.SGMenu;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.util.ItemBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zimoxy DEV: loveeev
 */

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
@Getter
public abstract class BaseGUI {

    Main plugin;
    @NonFinal
    SGMenu gui;

    public abstract BaseGUI create(Player player) throws NotRegisteredException;


    public void open(Player player) {
        player.openInventory(gui.getInventory());
    }

    public void close(Player player) {
        player.closeInventory();
    }

    protected String getCenteredTitle(String title) {
        return StringUtils.center(title, 34, " ");
    }


    protected void setParentGuiButton(SGMenu gui, BaseGUI parent) {
        gui.setButton(gui.getPageSize() - 9, new SGButton(ItemBuilder.fromMaterial(Material.MAP).setName("&#fa6666Назад").build()).withListener(inventoryClickEvent ->
        {
            try {
                parent.create((Player) inventoryClickEvent.getWhoClicked()).open((Player) inventoryClickEvent.getWhoClicked());
            } catch (NotRegisteredException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    protected void fillBorder(SGMenu gui) {
        ArrayList<Integer> border = new ArrayList<>();
        border.addAll(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8));
        int size = gui.getPageSize();
        int rows = size / 2;
        for (int i = 0; i < rows - 2; i++) {
            border.add(border.get(border.size() - 1) + 1);
            border.add(border.get(border.size() - 1) + 8);
        }
        for (int i = 0; i < 9; i++) {
            border.add(size - i);
        }
        for (int i : border) {
            gui.setButton(i, new SGButton(ItemBuilder.fromMaterial(Material.GRAY_STAINED_GLASS_PANE)
                    .setName(" ").build()));
        }
    }

}
