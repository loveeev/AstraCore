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
public class farmsell extends BaseGUI {

    ExchangeService exchangeService;

    public farmsell(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }

    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Аграрный сектор"), 6);
        gui.setAutomaticPaginationEnabled(false);
        for (int i = 0; i <54 ; i++) {
            gui.addButton(new SGButton(ItemBuilder
                    .fromMaterial(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(" ")
                    .build()
            ));
        }

        gui.setButton(53, new SGButton(ItemBuilder
                .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                .setName("&4Вернуться назад")
                .build())
                .withListener((event -> exchangeService.sellmenuex( (Player) event.getWhoClicked()))));
        gui.setButton(20, new SGButton(ItemBuilder
                .fromMaterial(Material.WHEAT)
                .setName("&#FFDAA4Продать пшеницу")
                .setLore("&7Цена:&#FFDAA4 " + Settings.SELL_WHEAT,"Для продажи - " + "&7Нажмите ПКМ")
                .setAmount(64)
                .build())
                .withListener((event -> exchangeService.exchanges( (Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Settings.SELL_WHEAT, Material.WHEAT, 64))));
        gui.setButton(21, new SGButton(ItemBuilder
                .fromMaterial(Material.CARROT)
                .setName("&#FFDAA4Продать морковь")
                .setLore("&7Цена:&#FFDAA4 " + Settings.SELL_CARROT,"Для продажи - " + "&7Нажмите ПКМ")
                .setAmount(64)
                .build())
                .withListener((event -> exchangeService.exchanges( (Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Settings.SELL_CARROT, Material.CARROT, 64))));
        gui.setButton(22, new SGButton(ItemBuilder
                .fromMaterial(Material.POTATO)
                .setName("&#FFDAA4Продать картофель")
                .setAmount(64)
                .setLore("&7Цена:&#FFDAA4 " + Settings.SELL_POTATO,"Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges( (Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Settings.SELL_POTATO, Material.POTATO, 64))));
        gui.setButton(23, new SGButton(ItemBuilder
                .fromMaterial(Material.WHEAT_SEEDS)
                .setName("&#FFDAA4Продать семена пшеницы")
                .setAmount(64)
                .setLore("&7Цена:&#FFDAA4 " + Settings.SELL_SEED,"Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges( (Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Settings.SELL_SEED, Material.WHEAT_SEEDS, 64))));
        gui.setButton(24, new SGButton(ItemBuilder
                .fromMaterial(Material.BEETROOT)
                .setName("&#FFDAA4Продать свёклу")
                .setAmount(64)
                .setLore("&7Цена:&#FFDAA4 " + Settings.SELL_BEETROOT,"Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges( (Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Settings.SELL_BEETROOT, Material.BEETROOT, 64))));
        gui.setButton(29, new SGButton(ItemBuilder
                .fromMaterial(Material.SWEET_BERRIES)
                .setName("&#FFDAA4Продать сладкие ягоды")
                .setAmount(64)
                .setLore("&7Цена:&#FFDAA4 " + Settings.SELL_SWEET,"Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges( (Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Settings.SELL_SWEET, Material.SWEET_BERRIES, 64))));
        gui.setButton(30, new SGButton(ItemBuilder
                .fromMaterial(Material.BEEF)
                .setName("&#FFDAA4Продать говядину")
                .setAmount(32)
                .setLore("&7Цена:&#FFDAA4 " + Settings.SELL_BEEF,"Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges( (Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Settings.SELL_BEEF, Material.BEEF, 32))));
        gui.setButton(31, new SGButton(ItemBuilder
                .fromMaterial(Material.MUTTON)
                .setName("&#FFDAA4Продать баранину")
                .setAmount(32)
                .setLore("&7Цена:&#FFDAA4 " + Settings.SELL_MUTTON,"Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges( (Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Settings.SELL_MUTTON, Material.MUTTON, 32))));;
        gui.setButton(32, new SGButton(ItemBuilder
                .fromMaterial(Material.CHICKEN)
                .setName("&#FFDAA4Продать курятину")
                .setAmount(32)
                .setLore("&7Цена:&#FFDAA4 " + Settings.SELL_CHICK,"Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges( (Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Settings.SELL_CHICK, Material.CHICKEN, 32))));
        gui.setButton(33, new SGButton(ItemBuilder
                .fromMaterial(Material.PORKCHOP)
                .setName("&#FFDAA4Продать свинину")
                .setAmount(32)
                .setLore("&7Цена:&#FFDAA4 " + Settings.SELL_SN,"Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges( (Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Settings.SELL_SN, Material.PORKCHOP, 32))));
        return this;
    }


}
