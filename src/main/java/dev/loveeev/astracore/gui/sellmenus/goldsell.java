package dev.loveeev.astracore.gui.sellmenus;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.samjakob.spigui.buttons.SGButton;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.gui.BaseGUI;
import dev.loveeev.astracore.module.impl.ExchangeService;
import dev.loveeev.astracore.settings.Settings;
import dev.loveeev.astracore.util.ItemBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class goldsell extends BaseGUI {

    ExchangeService exchangeService;

    public goldsell(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }

    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Продажа золота"), 3);
        gui.setAutomaticPaginationEnabled(false);
        for (int i = 0; i < 29; i++) {
            gui.addButton(new SGButton(ItemBuilder
                    .fromMaterial(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(" ")
                    .build()
            ));
        }
        gui.setButton(11, new SGButton(ItemBuilder
                .fromMaterial(Material.GOLD_INGOT)
                .setName("&#FFDAA4Продать 1 золото")
                .setLore("","Цена: &#FFDAA4" + Settings.PRICEGOLD * 1, "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchangegolden(1, (Player) event.getWhoClicked()))));
        gui.setButton(12, new SGButton(ItemBuilder
                .fromMaterial(Material.GOLD_INGOT)
                .setAmount(4)
                .setName("&#FFDAA4Продать 4 золота")
                .setLore("","Цена: &#FFDAA4" + Settings.PRICEGOLD * 4, "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchangegolden(4, (Player) event.getWhoClicked()))));
        gui.setButton(13, new SGButton(ItemBuilder
                .fromMaterial(Material.GOLD_INGOT)
                .setAmount(16)
                .setName("&#FFDAA4Продать 16 золота")
                .setLore("","Цена: &#FFDAA4" + Settings.PRICEGOLD * 16, "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchangegolden(16, (Player) event.getWhoClicked()))));
        gui.setButton(14, new SGButton(ItemBuilder
                .fromMaterial(Material.GOLD_INGOT)
                .setAmount(64)
                .setName("&#FFDAA4Продать 64 золота")
                .setLore("","Цена: &#FFDAA4" + Settings.PRICEGOLD * 64, "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchangegolden(64, (Player) event.getWhoClicked()))));
        gui.setButton(15, new SGButton(ItemBuilder
                .fromMaterial(Material.GOLD_BLOCK)
                .setName("&#FFDAA4Продать всё золото")
                .setLore("","Цена: &#FFDAA4" + Settings.PRICEGOLD * exchangeService.getGoldAmount(player), "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchangegolden(exchangeService.getGoldAmount((Player) event.getWhoClicked()), (Player) event.getWhoClicked()))));;
        Resident resident = TownyAPI.getInstance().getResident(player);
        if(resident.hasTown()) {
            gui.setButton(26, new SGButton(ItemBuilder
                    .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                    .setName("&4Вернуться в меню")
                    .setLore("", "&7Нажмите ПКМ")
                    .build())
                    .withListener((event -> exchangeService.sellmenuex((Player) event.getWhoClicked()))));
        }
        return this;
    }


}
