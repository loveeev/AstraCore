package dev.loveeev.astracore.gui.customcrafts;

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
public class crafts extends BaseGUI {

    ExchangeService exchangeService;

    public crafts(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }

    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Кастомные крафты"), 6);
        gui.setAutomaticPaginationEnabled(false);
        for (int i = 0; i <1 ; i++) {
            gui.addButton(new SGButton(ItemBuilder
                    .fromMaterial(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(" ")
                    .build()
            ));
        }
        gui.setButton(0, new SGButton(ItemBuilder
                .fromMaterial(Material.GUNPOWDER)
                .setName("&#FFDAA4Порох")
                .build())
                .withListener(event -> exchangeService.exchangegunpowder((Player) event.getWhoClicked())));
        gui.setButton(1, new SGButton(ItemBuilder
                .fromMaterial(Material.NETHER_BRICKS)
                .setName("&#FFDAA4Незерские кирпичи")
                .build())
                .withListener((event -> exchangeService.exchangenetherbricks( (Player) event.getWhoClicked()))));
        gui.setButton(2, new SGButton(ItemBuilder
                .fromMaterial(Material.SADDLE)
                .setName("&#FFDAA4Седло")
                .build())
                .withListener((event -> exchangeService.exchangesaddle( (Player) event.getWhoClicked()))));
        gui.setButton(3, new SGButton(ItemBuilder
                .fromMaterial(Material.BLAZE_ROD)
                .setName("&#FFDAA4Огненный стержень")
                .build())
                .withListener((event -> exchangeService.exchangeblazerod( (Player) event.getWhoClicked()))));
        gui.setButton(4, new SGButton(ItemBuilder
                .fromMaterial(Material.QUARTZ)
                .setName("&#FFDAA4Квартц")
                .build())
                .withListener((event -> exchangeService.exchangequartz( (Player) event.getWhoClicked()))));
        gui.setButton(5, new SGButton(ItemBuilder
                .fromMaterial(Material.WARPED_STEM)
                .setName("&#FFDAA4Искаженный стебель")
                .build())
                .withListener((event -> exchangeService.exchangewarped( (Player) event.getWhoClicked()))));
        gui.setButton(7, new SGButton(ItemBuilder
                .fromMaterial(Material.CRIMSON_STEM)
                .setName("&#FFDAA4Багровый стебель")
                .build())
                .withListener((event -> exchangeService.exchangecrimson( (Player) event.getWhoClicked()))));
        gui.setButton(8, new SGButton(ItemBuilder
                .fromMaterial(Material.CHAINMAIL_CHESTPLATE)
                .setName("&#FFDAA4Кольчужный нагрудник")
                .build())
                .withListener((event -> exchangeService.exchangechestplate( (Player) event.getWhoClicked()))));
        gui.setButton(9, new SGButton(ItemBuilder
                .fromMaterial(Material.CHAINMAIL_LEGGINGS)
                .setName("&#FFDAA4Кольчужные штаны")
                .build())
                .withListener((event -> exchangeService.exchangeleggins( (Player) event.getWhoClicked()))));
        gui.setButton(10, new SGButton(ItemBuilder
                .fromMaterial(Material.CHAINMAIL_HELMET)
                .setName("&#FFDAA4Кольчужный шлем")
                .build())
                .withListener((event -> exchangeService.exchangehelmet ((Player) event.getWhoClicked()))));
        gui.setButton(11, new SGButton(ItemBuilder
                .fromMaterial(Material.CHAINMAIL_BOOTS)
                .setName("&#FFDAA4Кольчужные ботинки")
                .build())
                .withListener((event -> exchangeService.exchangeboots ((Player) event.getWhoClicked()))));
        gui.setButton(12, new SGButton(ItemBuilder
                .fromMaterial(Material.BONE_MEAL)
                .setName("&#FFDAA4Костная мука")
                .build())
                .withListener((event -> exchangeService.exchangebonemeal( (Player) event.getWhoClicked()))));
        gui.setButton(13, new SGButton(ItemBuilder
                .fromMaterial(Material.GREEN_STAINED_GLASS)
                .setName("&#FFDAA4Идеи")
                .setLore("","&7Новые идеи можете писать на наш форум.")
                .build())
                .withListener((event -> exchangeService.exchangechat( (Player) event.getWhoClicked()))));
        gui.setButton(53, new SGButton(ItemBuilder
                .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                .setName("&#FFDAA4Закрыть меню.")
                .build())
                .withListener((event -> exchangeService.exchangeclose( (Player) event.getWhoClicked()))));
        return this;
    }


}
