package dev.loveeev.astracore.command.impl;

import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class specset implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;

        if (args.length < 2) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Использование: /specset <город из тауни> <сумма>");
            return true;
        }
        String town = args[0];
        int money = Integer.parseInt(args[1]);
        Main.getInstance().getChatUtility().sendSuccessNotification(player, "Теперь " + town + " получает по " + money + " монет каждые 24 часа.");
        try {
            MySQL mySQL = MySQL.getInstance();
            mySQL.selectstring("TOWNY_TOWNS","name",town,"spec","integer");
            Object in = Main.getInstance().getseceltinteger();
            int gotov = (Integer) in+money;
            String updateq = "UPDATE TOWNY_TOWNS SET spec = ";
            String update = " WHERE name = ?";
            String updateQuery = updateq + gotov + update;
            PreparedStatement statement = MySQL.getInstance().getCon().prepareStatement(updateQuery);
            statement.setString(1, town);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Количество измененных строк: " + rowsAffected);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}