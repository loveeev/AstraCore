package dev.loveeev.astracore.gui.sellmenus;

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
public class resourcesell extends BaseGUI {

    ExchangeService exchangeService;

    public resourcesell(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }

    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Металлургический сектор"), 3);
        gui.setAutomaticPaginationEnabled(false);
        for (int i = 0; i < 45; i++) {
            gui.addButton(new SGButton(ItemBuilder
                    .fromMaterial(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(" ")
                    .build()
            ));
        }
        double y = 1.0;
        gui.setButton(26, new SGButton(ItemBuilder
                .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                .setName("&#FFDAA4Вернуться назад")
                .build())
                .withListener((event -> exchangeService.sellmenuex((Player) event.getWhoClicked()))));
        gui.setButton(11, new SGButton(ItemBuilder
                .fromMaterial(Material.valueOf(Settings.ITEMCOBL))
                .setName("&4Продать булыжник")
                .setLore("&7Цена:&#FFDAA4 " + Settings.PRICECOBL, "Для продажи - " + "&7Нажмите ПКМ")
                .setAmount(Integer.parseInt(Settings.AMOUNTCOBL))
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Double.valueOf(Settings.PRICECOBL) * y, Material.valueOf(Settings.ITEMCOBL), Integer.parseInt(Settings.AMOUNTCOBL)))));
        gui.setButton(12, new SGButton(ItemBuilder
                .fromMaterial(Material.valueOf(Settings.ITEMIRON))
                .setName("&#FFDAA4Продать железо")
                .setLore("&7Цена:&#FFDAA4 " + Settings.PRICEIRON, "Для продажи - " + "&7Нажмите ПКМ")
                .setAmount(Integer.parseInt(Settings.AMOUNTIRON))
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Double.valueOf(Settings.PRICEIRON) * y, Material.valueOf(Settings.ITEMIRON), Integer.parseInt(Settings.AMOUNTIRON)))));
        gui.setButton(13, new SGButton(ItemBuilder
                .fromMaterial(Material.COPPER_INGOT)
                .setName("&#FFDAA4Продать медь")
                .setAmount(Settings.AMOUNTCOPPER)
                .setLore("&7Цена:&#FFDAA4 " + Settings.PRICECOPPER, "Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Settings.PRICECOPPER * y, Material.COPPER_INGOT, Settings.AMOUNTCOPPER))));
        gui.setButton(14, new SGButton(ItemBuilder
                .fromMaterial(Material.valueOf(Settings.ITEMA))
                .setName("&#FFDAA4Продать алмазы")
                .setAmount(Integer.parseInt(Settings.AMOUNTA))
                .setLore("&7Цена:&#FFDAA4 " + Settings.PRICEA, "Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Double.valueOf(Settings.PRICEA) * y, Material.valueOf(Settings.ITEMA), Integer.parseInt(Settings.AMOUNTA)))));
        gui.setButton(15, new SGButton(ItemBuilder
                .fromMaterial(Material.valueOf(Settings.ITEME))
                .setName("&#FFDAA4Продать изумруды")
                .setAmount(Integer.parseInt(Settings.AMOUNTE))
                .setLore("&7Цена:&#FFDAA4 " + Settings.PRICEE, "Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Double.valueOf(Settings.PRICEE) * y, Material.valueOf(Settings.ITEME), Integer.parseInt(Settings.AMOUNTE)))));
        return this;
    }
}
