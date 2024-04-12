package dev.loveeev.astracore.gui.builds;

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
public class drillz extends BaseGUI {

    ExchangeService exchangeService;

    public drillz(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }

    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Завод тяжелой техники"), 5);
        gui.setAutomaticPaginationEnabled(false);
        for (int i = 0; i < 35; i++) {
            gui.addButton(new SGButton(ItemBuilder
                    .fromMaterial(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(" ")
                    .build()
            ));
        }
        gui.setButton(12, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Слабая дрель")
                .setLore("","Цена: " + Settings.PRICEDRILL1,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"drill","D2",Settings.PRICEDRILL1))));
        gui.setButton(13, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Быстрая дрель ")
                .setLore("","Цена: " + Settings.PRICEDRILL2,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"drill","D3",Settings.PRICEDRILL2))));
        gui.setButton(14, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Сильная дрель")
                .setLore("","Цена: " + Settings.PRICEDRILL3,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"drill","D1",Settings.PRICEDRILL3))));
        gui.setButton(21, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Слабый трактор")
                .setLore("","Цена: " + Settings.PRICEKG1,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"tractor","YELLOW",Settings.PRICEKG1))));
        gui.setButton(22, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Средний трактор")
                .setLore("","Цена: " + Settings.PRICEKG2,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"tractor","RED",Settings.PRICEKG2))));
        gui.setButton(23, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Быстрый трактор")
                .setLore("","Цена: " + Settings.PRICEKG3,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"tractor","GREEN",Settings.PRICEKG3))));
        gui.setButton(30, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Маленький грузовик")
                .setLore("","Цена: " + Settings.PRICEKG1,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"train","T2",Settings.PRICEKG1))));
        gui.setButton(31, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Средний грузовик")
                .setLore("","Цена: " + Settings.PRICEKG2,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"train","T2",Settings.PRICEKG2))));
        gui.setButton(32, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Огромный грузовик")
                .setLore("","Цена: " + Settings.PRICEKG3,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"train","T2",Settings.PRICEKG3))));

        gui.setButton(44,new SGButton(ItemBuilder
                .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                .setName(" ")
                .build())
                .withListener((event -> exchangeService.exhcnageguii((Player) event.getWhoClicked()))));
        return this;
    }


}
