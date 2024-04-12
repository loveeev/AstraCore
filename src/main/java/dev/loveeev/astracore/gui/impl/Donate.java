package dev.loveeev.astracore.gui.impl;

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
public class Donate extends BaseGUI {

    ExchangeService exchangeService;

    public Donate(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }


    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Донаты"), 3);
        gui.setAutomaticPaginationEnabled(false);
        for (int i = 0; i < 27; i++) {
            gui.addButton(new SGButton(ItemBuilder
                    .fromMaterial(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(" ")
                    .build()
            ));
        }
        gui.setButton(0, new SGButton(ItemBuilder
                .fromMaterial(Material.REDSTONE)
                .setName("Ғ&#FFDAA4/tpa")
                .setLore("&7Дает вам возможность использовать /tpa")
                .setAmount(1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        gui.setButton(9, new SGButton(ItemBuilder
                .fromMaterial(Material.CHORUS_FRUIT)
                .setName("Ғ&#FFDAA4Военный завод+")
                .setLore("&7Дает вам возможность использовать военный завод+ в /t build")
                .setAmount(1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        gui.setButton(18, new SGButton(ItemBuilder
                .fromMaterial(Material.BOOK)
                .setName("Ғ&#FFDAA4+x1.5 /sell")
                .setLore("&7Покупается для города, при продаже в /sell у вас будет добавлен множить x1.5")
                .setAmount(1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        gui.setButton(12, new SGButton(ItemBuilder
                .fromMaterial(Material.REDSTONE)
                .setName("Ғ&#FFDAA4RUBY")
                .setLore("&7Дает вам: ", " ", "Цена: &#FFDAA4199.99Р &71 месяц","Цена: &#FFDAA42299Р &71 год")
                .setAmount(1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        gui.setButton(13, new SGButton(ItemBuilder
                .fromMaterial(Material.GOLD_INGOT)
                .setName("Ҕ&#FFDAA4GOLD")
                .setLore("&7Дает вам: ", "", "Цена: &#FFDAA4499.99Р &71 месяц","Цена: &#FFDAA45500Р &71 год")
                .setAmount(1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        gui.setButton(14, new SGButton(ItemBuilder
                .fromMaterial(Material.NETHERITE_INGOT)
                .setName("Ґ&#FFDAA4PLATINUM")
                .setLore("&7Дает вам: ", "", "Цена: &#FFDAA4999.99Р &71 месяц","Цена: &#FFDAA4999.99 &71 месяц","Цена: &#FFDAA411000Р &71 год")
                .setAmount(1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        gui.setButton(26, new SGButton(ItemBuilder
                .fromMaterial(Material.YELLOW_TERRACOTTA)
                .setName("Ґ&#FFDAA4Кит строителя")
                .setLore("&7Дает вам кит строителя в котором находится большое количество разных блоков", "подробно можете посмотреть на нашем сайте.")
                .setAmount(1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        gui.setButton(17, new SGButton(ItemBuilder
                .fromMaterial(Material.IRON_INGOT)
                .setName("Ґ&#FFDAA4Кит ресурсов")
                .setLore("&7Дает вам кит ресурсов в котором находится большое количество разных ресурсов", "подробно можете посмотреть на нашем сайте.")
                .setAmount(1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        gui.setButton(8, new SGButton(ItemBuilder
                .fromMaterial(Material.DIAMOND_HELMET)
                .setName("Ґ&#FFDAA4Кит PVP")
                .setLore("&7В этом ките находится все то, что нужно для PVP","подробно можете посмотреть на нашем сайте.")
                .setAmount(1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        return this;
    }


}
