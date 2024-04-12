package dev.loveeev.astracore.gui.impl;

import com.samjakob.spigui.buttons.SGButton;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.gui.BaseGUI;
import dev.loveeev.astracore.module.impl.ExchangeService;
import dev.loveeev.astracore.util.ItemBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class menu extends BaseGUI {

    ExchangeService exchangeService;

    public menu(Main plugin) {
        super(plugin);
        exchangeService = plugin.getServiceLayer().get(ExchangeService.class);
    }


    @Override
    public BaseGUI create(Player player) {
        gui = plugin.getSpiGui().create(getCenteredTitle("Меню сервера"), 3);
        gui.setAutomaticPaginationEnabled(false);
        for (int i = 0; i < 27; i++) {
            gui.addButton(new SGButton(ItemBuilder
                    .fromMaterial(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(" ")
                    .build()
            ));
        }
        String killplayer = PlaceholderAPI.setPlaceholders(player, "%statistic_player_kills%");
        String kill = PlaceholderAPI.setPlaceholders(player, "%statistic_mob_kills%");
        String death = PlaceholderAPI.setPlaceholders(player, "%statistic_deaths%");
        String Balance = PlaceholderAPI.setPlaceholders(player, "%vault_eco_balance_commas%");
        String town = PlaceholderAPI.setPlaceholders(player, "%townychat_town%");
        String nation = PlaceholderAPI.setPlaceholders(player, "%townychat_nation%");
        String tps = PlaceholderAPI.setPlaceholders(player, "%server_tps_1_colored%");
        String ping = PlaceholderAPI.setPlaceholders(player, "%player_ping%");
        String online = PlaceholderAPI.setPlaceholders(player, "%server_online%");

        gui.setButton(4, new SGButton(ItemBuilder
                .fromMaterial(Material.DIAMOND_HELMET)
                .setName("&7[&b&l⚡&7] &#FFDAA4Ваша статистика")
                .setLore("", "&f→ &7Основная статистика вашего аккаунта","&#FFDAA4Никнейм: &f" + player.getName()," ","&#FFDAA4Убийств игроков: &f" + killplayer, "&#FFDAA4Убийств мобов: &f"+kill, "&#FFDAA4Смертей: &f" + death," ", "&#FFDAA4Баланс: &f" + Balance, "&#FFDAA4Город: &f" + town, "&#FFDAA4Нация: &f" + nation)
                .setAmount(1)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        gui.setButton(10, new SGButton(ItemBuilder
                .fromMaterial(Material.GOLD_INGOT)
                .setName("&#FFDAA4Скупщик")
                .setLore("","&f→ &7Скупщиков ресурсов, основной способ заработка на сервере")
                .addEnchantment(Enchantment.ARROW_DAMAGE, 1)
                .setAmount(1)
                .build())
                .withListener((event -> exchangeService.exchangesell((Player) event.getWhoClicked()))));
        gui.setButton(12, new SGButton(ItemBuilder
                .fromMaterial(Material.DIAMOND)
                .setName("&#FFDAA4Донат")
                .setLore("","&f→ &7Донат на сервере и его преимущества")
                .setAmount(1)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 1)
                .build())
                .withListener((event -> exchangeService.exchangesdon((Player) event.getWhoClicked()))));
        gui.setButton(13, new SGButton(ItemBuilder
                .fromMaterial(Material.CHAIN_COMMAND_BLOCK)
                .setName("&#FFDAA4Статистика сервера")
                .setLore("","&f→ &7Статистика сервера на текущий момент", "&fTPS: &#FFDAA4" + tps, "&fПинг: &#FFDAA4"+ping +"&fms" ,"&fОнлайн: &#FFDAA4"+ online,"")
                .setAmount(1)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        gui.setButton(14, new SGButton(ItemBuilder
                .fromMaterial(Material.BOOK)
                .setName("&#FFDAA4Помощь по серверу")
                .setLore("","&f→ &7Основные команды:","&f/money, /balance, bal - &7показать свой баланс","","&f/money pay <игрок> <сумма_денег> - &7перечислить","&7деньги определенному игроку","","&f/baltop - &7показать топ игроков по балансу","","&f/msg <ник игрока> <сообщение> - &7написать личное","&7сообщение определенному игроку","","&f/spawn - &7телепортироваться на спавн сервера","&f/t spawn - &7телепортироваться на спавн города","","&f/n spawn - &7телепортироваться на спавн нации","","&f/t new <название_города> - &7создать город","","&f/t add <никнейм_игрока> - &7пригласить игрока в","&7город","","&f/t deposit <сумма_денег> - &7положить деньги в","&7казну города","","&f/t withdraw <сумма_денег> - &7снять деньги с казны","&7города","","&f/t claim - &7заприватить чанк, на котором стоите вы","","&f/t unclaim - &7снять приват с чанка, на котором стоите вы","","&f/t rank add <никнейм_игрока> <ранк> - &7выдать ранк игроку","","&f/t rank remove <никнейм_игрока> <ранк> - &7снять ранк с игрока","","&f/helpop, /report <текст> - &7задать вопрос или","&7пожаловаться на что-либо администрации сервера")
                .setAmount(1)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        gui.setButton(16, new SGButton(ItemBuilder
                .fromMaterial(Material.GOLD_INGOT)
                .setName("&#FFDAA4Магазин")
                .setLore("","&7Для открытия магазина вы должны состоять в городе!")
                .setAmount(1)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 1)
                .build())
                .withListener((event -> exchangeService.exchanges((Player) event.getWhoClicked()))));
        gui.setButton(22, new SGButton(ItemBuilder
                .fromMaterial(Material.NETHER_STAR )
                .setName("&#FFDAA4Ссылки")
                .setLore("&f→ &7Ссылки на наши ресурсы","","&fДонат&7: https://astraworld.pro",
                        "&fГруппа ВК&7: https://vk.com/astraworld",
                        "&fДискорд&7: https://discord.gg/gSJmxTsaP8")
                .setAmount(1)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 1)
                .build())
                .withListener((event -> exchangeService.exchangechat((Player) event.getWhoClicked()))));
        gui.setButton(1, new SGButton(ItemBuilder
                .fromMaterial(Material.ORANGE_STAINED_GLASS_PANE)
                .setName(" ")
                .build()));
        gui.setButton(3, new SGButton(ItemBuilder
                .fromMaterial(Material.ORANGE_STAINED_GLASS_PANE)
                .setName(" ")
                .build()));
        gui.setButton(5, new SGButton(ItemBuilder
                .fromMaterial(Material.ORANGE_STAINED_GLASS_PANE)
                .setName(" ")
                .build()));
        gui.setButton(7, new SGButton(ItemBuilder
                .fromMaterial(Material.ORANGE_STAINED_GLASS_PANE)
                .setName(" ")
                .build()));
        gui.setButton(9, new SGButton(ItemBuilder
                .fromMaterial(Material.ORANGE_STAINED_GLASS_PANE)
                .setName(" ")
                .build()));
        gui.setButton(15, new SGButton(ItemBuilder
                .fromMaterial(Material.ORANGE_STAINED_GLASS_PANE)
                .setName(" ")
                .build()));
        gui.setButton(17, new SGButton(ItemBuilder
                .fromMaterial(Material.ORANGE_STAINED_GLASS_PANE)
                .setName(" ")
                .build()));
        gui.setButton(19, new SGButton(ItemBuilder
                .fromMaterial(Material.ORANGE_STAINED_GLASS_PANE)
                .setName(" ")
                .build()));
        gui.setButton(21, new SGButton(ItemBuilder
                .fromMaterial(Material.ORANGE_STAINED_GLASS_PANE)
                .setName(" ")
                .build()));
        gui.setButton(23, new SGButton(ItemBuilder
                .fromMaterial(Material.ORANGE_STAINED_GLASS_PANE)
                .setName(" ")
                .build()));

        gui.setButton(25, new SGButton(ItemBuilder
                .fromMaterial(Material.ORANGE_STAINED_GLASS_PANE)
                .setName(" ")
                .build()));
        return this;
    }


}
