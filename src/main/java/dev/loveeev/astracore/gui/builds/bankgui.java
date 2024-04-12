package dev.loveeev.astracore.gui.builds;

import com.samjakob.spigui.buttons.SGButton;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.gui.BaseGUI;
import dev.loveeev.astracore.module.impl.ExchangeService;
import dev.loveeev.astracore.util.ItemBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class bankgui extends BaseGUI {

    ExchangeService exchangeService;

    public bankgui(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }

    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Банк"), 1);
        gui.setAutomaticPaginationEnabled(false);
        for (int i = 0; i <9 ; i++) {
            gui.addButton(new SGButton(ItemBuilder
                    .fromMaterial(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(" ")
                    .build()
            ));
        }
        gui.setButton(0, new SGButton(ItemBuilder
                .fromMaterial(Material.GOLD_INGOT)
                .setName("&#FFDAA4Получить монеты.")
                .setLore("", "&7Нажмите ПКМ")
                .build())
                .withListener(event -> exchangeService.givemoney((Player) event.getWhoClicked())));
        gui.setButton(8, new SGButton(ItemBuilder
                .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                .setName("&#FFDAA4Вернуться назад")
                .build())
                .withListener((event -> exchangeService.exhcnageguii( (Player) event.getWhoClicked()))));
        return this;
    }


}
