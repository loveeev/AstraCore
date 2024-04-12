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
public class oaksell extends BaseGUI {

    ExchangeService exchangeService;

    public oaksell(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }

    @Override
    public BaseGUI create(Player player) {
        Double y = 1.0;
        Double cobl = Double.valueOf(Settings.PRICECOBL) * y;
        Double emerald = Double.valueOf(Settings.PRICEE) * y;
        Double gold = Double.valueOf(Settings.PRICEG) * y;
        Double iron = Double.valueOf(Settings.PRICEIRON) * y;
        Double eli = Double.valueOf(Settings.PRICEELI) * y;
        Double niga = Double.valueOf(Settings.PRICENIGA) * y;
        Double birch = Double.valueOf(Settings.PRICEBER) * y;
        Double dub = Double.valueOf(Settings.PRICEDUB) * y;
        Double aca = Double.valueOf(Settings.PRICEACA) * y;
        Double tropic = Double.valueOf(Settings.PRICETROPIC) * y;
        Double almaz = Double.valueOf(Settings.PRICEA) * y;
        gui = plugin.getSpiGui().create(getCenteredTitle("Лесной сектор."), 4);
        gui.setAutomaticPaginationEnabled(false);
        for (int i = 0; i < 45; i++) {
            gui.addButton(new SGButton(ItemBuilder
                    .fromMaterial(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(" ")
                    .build()
            ));
        }
        gui.setButton(35, new SGButton(ItemBuilder
                .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                .setName("&4Вернуться назад")
                .build())
                .withListener((event -> exchangeService.sellmenuex((Player) event.getWhoClicked()))));
        gui.setButton(12, new SGButton(ItemBuilder
                .fromMaterial(Material.SPRUCE_PLANKS)
                .setName("&#FFDAA4Продать доски ели")
                .setAmount(Integer.parseInt(Settings.AMOUNTELI))
                .setLore("&7Цена:&#FFDAA4 " + eli, "Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Double.valueOf(Settings.PRICEELI) * y, Material.SPRUCE_PLANKS, Integer.parseInt(Settings.AMOUNTELI)))));
        gui.setButton(13, new SGButton(ItemBuilder
                .fromMaterial(Material.BIRCH_PLANKS)
                .setName("&#FFDAA4Продать доски березы")
                .setAmount(Integer.parseInt(Settings.AMOUNTBEREZ))
                .setLore("&7Цена:&#FFDAA4 " + birch, "Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Double.valueOf(Settings.PRICEBER) * y, Material.BIRCH_PLANKS, Integer.parseInt(Settings.AMOUNTTROPIC)))));
        gui.setButton(14, new SGButton(ItemBuilder
                .fromMaterial(Material.JUNGLE_PLANKS)
                .setName("&#FFDAA4Продать доски тропического дерева")
                .setAmount(Integer.parseInt(Settings.AMOUNTTROPIC))
                .setLore("&7Цена:&#FFDAA4 " + tropic, "Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Double.valueOf(Settings.PRICETROPIC) * y, Material.JUNGLE_PLANKS, Integer.parseInt(Settings.AMOUNTTROPIC)))));
        gui.setButton(21, new SGButton(ItemBuilder
                .fromMaterial(Material.DARK_OAK_PLANKS)
                .setName("&#FFDAA4Продать доски темного дуба")
                .setAmount(Integer.parseInt(Settings.AMOUNTNIGA))
                .setLore("&7Цена:&#FFDAA4 " + niga, "Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Double.valueOf(Settings.PRICENIGA) * y, Material.DARK_OAK_PLANKS, Integer.parseInt(Settings.AMOUNTNIGA)))));
        gui.setButton(22, new SGButton(ItemBuilder
                .fromMaterial(Material.OAK_PLANKS)
                .setName("&#FFDAA4Продать доски дуба")
                .setAmount(Integer.parseInt(Settings.AMOUNTDUB))
                .setLore("&7Цена:&#FFDAA4 " + dub, "Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Double.valueOf(Settings.PRICEDUB) * y, Material.OAK_PLANKS, Integer.parseInt(Settings.AMOUNTDUB)))));
        gui.setButton(23, new SGButton(ItemBuilder
                .fromMaterial(Material.ACACIA_PLANKS)
                .setName("&#FFDAA4Продать доски акации")
                .setAmount(Integer.parseInt(Settings.AMOUNTACA))
                .setLore("&7Цена:&#FFDAA4 " + aca, "Для продажи - " + "&7Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked())))
                .withListener((event -> exchangeService.sellfarmall(player, Double.valueOf(Settings.PRICEACA) * y, Material.ACACIA_PLANKS, Integer.parseInt(Settings.AMOUNTDUB)))));
        return this;
    }
}
