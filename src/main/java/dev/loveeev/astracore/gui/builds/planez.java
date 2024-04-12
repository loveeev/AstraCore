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
public class planez extends BaseGUI {

    ExchangeService exchangeService;

    public planez(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }

    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Авиационно строительный завод"), 3);
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
                .setName("Медленный самолет")
                .setLore("","Цена: " + Settings.PRICEPLANE1,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"plane","P3", Settings.PRICEPLANE1))));
        gui.setButton(13, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Средний самолет")
                .setLore("","Цена: " + Settings.PRICEPLANE2,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"plane","P1",Settings.PRICEPLANE1))));
        gui.setButton(14, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Быстрый самолет")
                .setLore("","Цена: " + Settings.PRICEPLANE3,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"plane","P2",Settings.PRICEPLANE1))));
        gui.setButton(26,new SGButton(ItemBuilder
                .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                .setName(" ")
                .build())
                .withListener((event -> exchangeService.exhcnageguii((Player) event.getWhoClicked()))));
        return this;
    }


}
