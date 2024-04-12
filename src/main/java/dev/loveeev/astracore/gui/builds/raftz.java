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
public class raftz extends BaseGUI {

    ExchangeService exchangeService;
    private String cooldownText = "";

    public raftz(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }

    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Верфь"), 3);
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
                .setName("Медленная субмарина")
                .setLore("","Цена: " + Settings.PRICEVERF1,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"submarine","S1",Settings.PRICEVERF1))));
        gui.setButton(13, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Защищенная субмарина")
                .setLore("","Цена: " + Settings.PRICEVERF2,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"submarine","S2",Settings.PRICEVERF2))));
        gui.setButton(14, new SGButton(ItemBuilder
                .fromMaterial(Material.CHEST)
                .setName("Быстрая субмарина")
                .setLore("","Цена: " + Settings.PRICEVERF3,"&7Для покупки - Нажмите ПКМ")
                .build())
                .withListener((event -> exchangeService.givetransport((Player) event.getWhoClicked(),"submarine","S3",Settings.PRICEVERF3))));
        gui.setButton(26,new SGButton(ItemBuilder
                .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                .setName(" ")
                .build())
                .withListener((event -> exchangeService.exhcnageguii((Player) event.getWhoClicked()))));
        return this;
    }


}
