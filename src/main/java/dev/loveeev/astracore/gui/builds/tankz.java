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
public class tankz extends BaseGUI {

    ExchangeService exchangeService;

    public tankz(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }

    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Танкостроительный завод"), 3);
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
                .setName("Слабый танк")
                .setLore("","Цена: " + Settings.PRICETANK1,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"train","T1",Settings.PRICETANK1))));
        gui.setButton(13, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Средний танк")
                .setLore("","Цена: " + Settings.PRICETANK2,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"train","T2",Settings.PRICETANK2))));
        gui.setButton(14, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Большой танк")
                .setLore("","Цена: " + Settings.PRICETANK3,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"train","T3",Settings.PRICETANK3))));
        gui.setButton(26,new SGButton(ItemBuilder
                .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                .setName(" ")
                .build())
                .withListener((event -> exchangeService.exhcnageguii((Player) event.getWhoClicked()))));
        return this;
    }
}
