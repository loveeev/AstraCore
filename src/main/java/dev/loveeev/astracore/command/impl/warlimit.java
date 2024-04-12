package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class warlimit implements CommandExecutor {

    PreparedStatement ps;

    ResultSet rs;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        if (args.length < 1) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /t zavodwar числто");
            return true;
        }
        try {
            String nation1 = args[0];
            Nation nation = TownyAPI.getInstance().getNation(nation1);
            int price = Integer.parseInt(args[1]);
                if(nation == null){
                    Main.getInstance().getChatUtility().sendSuccessNotification(player,"Неправильно указали название нации.");
                }else {
                    MySQL mySQL = MySQL.getInstance();
                    mySQL.update("TOWNY_NATIONS","warlimit","name",price,nation.getName());
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы установили лимит военных заводов на " + price + " для нации " + nation );
                }
        } catch (NumberFormatException e) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Введите число.");
        }
        return false;
    }
}
