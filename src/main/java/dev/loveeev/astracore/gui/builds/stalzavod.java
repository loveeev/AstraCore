package dev.loveeev.astracore.gui.builds;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;
import com.samjakob.spigui.buttons.SGButton;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.gui.BaseGUI;
import dev.loveeev.astracore.module.impl.ExchangeService;
import dev.loveeev.astracore.settings.Settings;
import dev.loveeev.astracore.util.ItemBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class stalzavod extends BaseGUI {

    ExchangeService exchangeService;

    public stalzavod(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
        exchangeService.setCooldownText(""); // Установите текст по умолчанию
    }

    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Сталелитейный завод"), 3);
        gui.setAutomaticPaginationEnabled(false);
        for (int i = 0; i < 35; i++) {
            gui.addButton(new SGButton(ItemBuilder
                    .fromMaterial(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(" ")
                    .build()
            ));
        }

        updateMenu(player);
        return this;
    }


    private String formatTime(long millis) {
        long seconds = millis / 1000 % 60;
        long minutes = millis / (60 * 1000) % 60;
        long hours = millis / (60 * 60 * 1000) % 24;
        long days = millis / (24 * 60 * 60 * 1000);

        String timeString = "";
        if (days > 0) {
            timeString += days + " дн. ";
        }
        if (hours > 0) {
            timeString += hours + " ч. ";
        }
        if (minutes > 0) {
            timeString += minutes + " мин. ";
        }
        if (seconds > 0) {
            timeString += seconds + " сек.";
        }

        return timeString.trim();
    }

    public void updateMenu(Player player) {
        String cooldownText = exchangeService.getCooldownText();

        if (exchangeService.isCooldownActive(player)) {
            cooldownText = "Подождите пока пройдет 10 минут"; // Замените на свой текст
        }
        // Оставьте остальную часть кода без изменений
        String buttonName = cooldownText.isEmpty() ? "Обмен" : "Обмен (КД)";
        String buttonLore = cooldownText.isEmpty() ?
                "Обменять ресурсы на незеритовый лом:" :
                "&7";
        UUID playerUUID = player.getUniqueId();


        try {
            Town town = TownyAPI.getInstance().getTown(player);
            ResultSet rs = MySQL.getInstance().executeQuery("SELECT * FROM `TOWNY_TOWNS` WHERE name = ?", town.getName());
            if (rs.next()) {
                if (exchangeService.getCooldownsnet().containsKey(playerUUID)) {
                    int pr = rs.getInt("WarProc");
                    gui.setButton(18, new SGButton(ItemBuilder
                            .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setName("&7Ваш процентр произвосдтва равен: &#ffdaaa" + pr)
                            .build())
                            .withListener((event -> exchangeService.exhcnageguii((Player) event.getWhoClicked()))));
                    long lastClaimTime = exchangeService.getCooldownsnet().get(playerUUID);
                    long currentTime = System.currentTimeMillis();

                    long cooldown = 10 * 60 * 1000; // 10 минут
                    String cooldownMessage = "Подожди еще " + formatTime(lastClaimTime + cooldown - currentTime) + ".";
                    if (exchangeService.isCooldownActive(player)) {
                        gui.setButton(13, new SGButton(ItemBuilder
                                .fromMaterial(Material.NETHERITE_SCRAP)
                                .setName("&#FFDAA4" + buttonName)
                                .setLore(" ", "&7" + buttonLore,
                                        "&7Обмен на перезарядке",
                                        "&4" + cooldownMessage,
                                        "",
                                        "&7Нажмите ЛКМ")
                                .build())
                                .withListener((event -> exchangeService.givenet((Player) event.getWhoClicked()))));
                        gui.setButton(26, new SGButton(ItemBuilder
                                .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                                .setName("&7Вернуться в меню")
                                .build())
                                .withListener((event -> exchangeService.exhcnageguii((Player) event.getWhoClicked()))));
                    }
                } else {
                    int pr = rs.getInt("WarProc");
                    gui.setButton(13, new SGButton(ItemBuilder
                            .fromMaterial(Material.NETHERITE_SCRAP)
                            .setName("&#FFDAA4" + buttonName)
                            .setLore(" ", "&7" + buttonLore,
                                    "&f- Железный слиток: &#FFDAA4" + Settings.IRONAMOUNT * pr / 5,
                                    "&f- Уголь: &#FFDAA4 " + Settings.COALAMOUNT * pr / 5,
                                    "&7" + cooldownText,
                                    "",
                                    "&7Нажмите ЛКМ")
                            .build())
                            .withListener((event -> exchangeService.givenet((Player) event.getWhoClicked()))));
                    gui.setButton(26, new SGButton(ItemBuilder
                            .fromMaterial(Material.RED_STAINED_GLASS_PANE)
                            .setName("&7Вернуться в меню")
                            .build())
                            .withListener((event -> exchangeService.exhcnageguii((Player) event.getWhoClicked()))));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
