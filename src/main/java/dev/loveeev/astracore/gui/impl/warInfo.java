package dev.loveeev.astracore.gui.impl;

import com.samjakob.spigui.buttons.SGButton;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.gui.BaseGUI;
import dev.loveeev.astracore.handler.War;
import dev.loveeev.astracore.module.impl.ExchangeService;
import dev.loveeev.astracore.settings.Settings;
import dev.loveeev.astracore.util.DataUtil;
import dev.loveeev.astracore.util.ItemBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class warInfo extends BaseGUI {

    ExchangeService exchangeService;

    public warInfo(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }


    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Информация о войне"), 1);
        War war = DataUtil.getFirstEnabledWar();
        gui.setAutomaticPaginationEnabled(false);
        int time = Settings.TIMEFOROCCUOATION;
        int capt = Settings.CAPITULATIONPERCENT;
        String forb = Settings.forbiddenCommands;
        for (int i = 0; i < 10; i++) {
            gui.addButton(new SGButton(ItemBuilder
                    .fromMaterial(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(" ")
                    .build()
            ));
        }
        gui.setButton(4, new SGButton(ItemBuilder
                .fromMaterial(Material.NETHER_STAR)
                .setName("&#FFDAA4Настройки войны на данный момент")
                .setLore("&7","- Для захвата города нужно -" + time + " секунд.","- На войне запрещены команды - " + forb,"- Для захвата другой нации вы должны оккупировать " + capt + "% городов.")
                .setAmount(1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        return this;
    }

}
