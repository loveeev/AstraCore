package dev.loveeev.astracore.command.impl;

import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class infotownpay implements CommandExecutor {
    PreparedStatement ps;
    ResultSet rs;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        if(args.length == 1){
            String town = args[0];
            try {
                ps = MySQL.getInstance().getCon().prepareStatement("SELECT * FROM `TOWNY_TOWNS` WHERE `name` = ?"); // Обязательно нужно делать с ?, на строчке ниже ты передаешь аргументы которые над вставить
                ps.setString(1, town); // ЛуЧШЕ каждый арг. делать в отдельной строке - для каждого типа, INT, Double и тд
                rs = ps.executeQuery();
                if (!rs.next()) { // rs.next() - тут проверяется есть ли результат такой в БД
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Ошибка базы данных, обратитесь за помощью к администратору.");
                } else {
                    double resulttf;
                    resulttf = rs.getDouble("payer");
                    int port = rs.getInt("carg");
                    int war = rs.getInt("cargw");
                    int warp = rs.getInt("premium");
                    int builds = rs.getInt("cargst");
                    int teleg = rs.getInt("bc");
                    int pred = rs.getInt("mayorbank");
                    int bank = rs.getInt("bank");
                    int farm = rs.getInt("farmer");
                    int spec = rs.getInt("spec");
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У города &x&f&f&d&a&a&4" + town + " &fотдано админам &x&f&f&d&a&a&4" + resulttf);
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У этого города построены здания :");
                    if (port == 1) {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "У этого города построен порт.");
                    }
                    if (war == 1) {
                        if (warp == 1) {
                            Main.getInstance().getChatUtility().sendSuccessNotification(player, "У этого города есть военный завод с &x&f&f&d&a&a&4premium");
                        } else {
                            Main.getInstance().getChatUtility().sendSuccessNotification(player, "У этого города есть военный завод.");
                        }
                    }
                    if (teleg == 1) {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "У этого города построен телеграф");
                    }
                    if (pred == 1) {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "У этого города построено предприятие, а так же город получает &x&f&f&d&a&a&4" + spec + " &7монет с него.");
                    }
                    if (bank == 1) {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "У этого города построен банк");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /ta infotown название города");
        }
        return false;
    }
}
