package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.command.BaseCommand;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class market extends BaseCommand {
    public market(Main plugin) {
        super(plugin);
    }

    @Override
    public boolean handle(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NotRegisteredException {
        Player player = (Player) sender;
        MySQL mySQL = MySQL.getInstance();
        Nation nation = TownyAPI.getInstance().getNation(player);
        if(args.length < 1){
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Используйте /market add/remove");
            return true;
        }
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 3) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /t market add нация пошлина");
                return true;
            }
            if (args[1].equals(nation.getName())) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы не можете добавить свою нацию.");
                return true;
            }
            int price = Integer.parseInt(args[2]);
            try {
                try (Connection connection = mySQL.getCon();
                     PreparedStatement ps = connection.prepareStatement("SELECT poshlin FROM AHN_ALLOW WHERE nationowner = ? AND nationallow = ?")) {
                    ps.setString(1, nation.getName());
                    ps.setString(2, args[1]);

                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        try (Connection connectionp = mySQL.getCon();
                             PreparedStatement pss = connectionp.prepareStatement("INSERT INTO `AHN_ALLOW` (`nationowner`, `nationallow`, `poshlin`) VALUES (?, ?, ?)")) {
                            pss.setString(1, nation.getName());
                            pss.setString(2, args[1]);
                            pss.setInt(3, price);

                            pss.executeUpdate();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        Main.getInstance().getChatUtility().sendSuccessNotification(player,"Вы успешно добавили нацию &#FFDAA4" + args[1]);
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы уже добавили эту нацию!");
                        return true;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } catch (NumberFormatException e) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Введите число.");
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /t market remove нация");
                return true;
            }
            if (args[1].equals(nation.getName())) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы не можете удалить свою нацию.");
                return true;
            }
            try {
                try (Connection connection = mySQL.getCon();
                     PreparedStatement ps = connection.prepareStatement("SELECT poshlin FROM AHN_ALLOW WHERE nationowner = ? AND nationallow = ?")) {
                    ps.setString(1, nation.getName());
                    ps.setString(2, args[1]);

                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас не добавлена эта нация!");
                        return true;
                    } else {
                        try (Connection connectionp = mySQL.getCon();
                             PreparedStatement pss = connectionp.prepareStatement("DELETE FROM `AHN_ALLOW` WHERE `nationallow` = ? AND `nationowner` = ?")) {
                            pss.setString(1, args[1]);
                            pss.setString(2, nation.getName());

                            pss.executeUpdate();
                            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Вы успешно удалили нацию &#FFDAA4" + args[1]);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } catch (NumberFormatException e) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Введите число.");
            }
        }
        return false;
    }
}
