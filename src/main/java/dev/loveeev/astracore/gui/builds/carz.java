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
public class carz extends BaseGUI {

    ExchangeService exchangeService;

    public carz(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }

    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Железнострительный завод"), 3);
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
                .setName("Слабая машина")
                .setLore("","Цена: " + Settings.PRICEEZ1,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"car","BLACK",Settings.PRICEEZ1))));
        gui.setButton(13, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Средняя машина")
                .setLore("","Цена: " + Settings.PRICEEZ2,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"car","YELLOW",Settings.PRICEEZ2))));
        gui.setButton(14, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Быстрая машина")
                .setLore("","Цена: " + Settings.PRICEEZ3,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"car","MAGENTA",Settings.PRICEEZ3))));
        gui.setButton(21, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Слабый мотоцикл")
                .setLore("","Цена: " + Settings.PRICEBIKE1,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"bike","BLACK",Settings.PRICEEZ1))));
        gui.setButton(22, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Средний мотоцикл")
                .setLore("","Цена: " + Settings.PRICEBIKE2,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"bike","YELLOW",Settings.PRICEEZ2))));
        gui.setButton(23, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Быстрый мотоцикл")
                .setLore("","Цена: " + Settings.PRICEBIKE3,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"bike","MAGENTA",Settings.PRICEEZ3))));
        gui.setButton(26,new SGButton(ItemBuilder
                .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                .setName(" ")
                .build())
                .withListener((event -> exchangeService.exhcnageguii((Player) event.getWhoClicked()))));
        return this;
    }


}
