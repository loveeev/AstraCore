package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class listnation implements CommandExecutor {

    PreparedStatement ps;

    ResultSet rs;

    Statement s;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length < 1) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /nlist название нации.");
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Nation nation = TownyAPI.getInstance().getNation(args[0]);
            if (nation == null) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Такой нации не существует.");
            } else {
                try {
                    ps = MySQL.getInstance().getCon().prepareStatement("SELECT * FROM TOWNY_NATIONS WHERE name = ?"); // Обязательно нужно делать с ?, на строчке ниже ты передаешь аргументы которые над вставить
                    ps.setString(1, nation.getName()); // ЛуЧШЕ каждый арг. делать в отдельной строке - для каждого типа, INT, Double и тд
                    rs = ps.executeQuery();
                    if (!rs.next()) { // rs.next() - тут проверяется есть ли результат такой в БД
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Ошибка базы данных, обратитесь за помощью к администратору.");
                    } else {
                        int res = rs.getInt("limits");
                        int set = rs.getInt("soldiers");
                        int port = rs.getInt("portamount");
                        int war = rs.getInt("waramount");
                        int builds = rs.getInt("stamount");
                        int teleg = rs.getInt("bcamount");
                        int pred = rs.getInt("bmamount");
                        int bank = rs.getInt("bankamount");
                        int farm = rs.getInt("farmamount");
                        int warlimits = rs.getInt("warlimit");
                        int fuelamount = rs.getInt("fuelamount");
                        int fuelcraftamount = rs.getInt("fuelcraftamount");
                        int trainamount = rs.getInt("trainamount");
                        int caramount = rs.getInt("caramount");
                        int drillamount = rs.getInt("drillamount");
                        int planeamount = rs.getInt("planeamount");
                        int tankamount = rs.getInt("tankamount");
                        int raftamount = rs.getInt("raftamount");
                        int cargfabric = rs.getInt("fabricamount");
                        int MoneyYardAmount = rs.getInt("MoneyYardAmount");
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Нация: &x&f&f&d&a&a&4" + args[0] + "&f, лимит военных: &x&f&f&d&a&a&4" + res + "&f, количество военных: &x&f&f&d&a&a&4" + set);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Построенные здания:");
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Портов: &x&f&f&d&a&a&4" + port);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Военных заводов: &x&f&f&d&a&a&4" + war + "&f,лимит на них: &x&f&f&d&a&a&4" + warlimits);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Строительных фабрик: &x&f&f&d&a&a&4" + builds);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Аграрных секторов: &x&f&f&d&a&a&4" + farm);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Предприятий: &x&f&f&d&a&a&4" + pred);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Банков: &x&f&f&d&a&a&4" + bank);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Телеграфов: &x&f&f&d&a&a&4" + teleg);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Нефтяных станций: &x&f&f&d&a&a&4" + fuelamount);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Нефтеперерабатывающих заводов: &x&f&f&d&a&a&4" + fuelcraftamount);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Локомотивных заводов: &x&f&f&d&a&a&4" + trainamount);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "заводов легкой техники: &x&f&f&d&a&a&4" + caramount);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "заводов тяжелой техники: &x&f&f&d&a&a&4" + drillamount);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "верфей: &x&f&f&d&a&a&4" + raftamount);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Танкостроительных заводов: &x&f&f&d&a&a&4" + tankamount);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Авиационных заводов: &x&f&f&d&a&a&4" + planeamount);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Текстильная фабрика: &x&f&f&d&a&a&4" + cargfabric);
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Монетный-двор: &#ffdaa4" + MoneyYardAmount);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return false;
    }
}