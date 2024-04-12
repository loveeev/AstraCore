package dev.loveeev.astracore.command.impl;

import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class adminsoldier implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        MySQL mySQL = MySQL.getInstance();
        String town = args[0];
        Integer limit = Integer.valueOf(args[1]);
        Player player = (Player) sender;
        if (args.length != 2) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Команда должна иметь 2 аргумента, название города и лимит.");
        } else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Лимит на военных для " + town + " установлен на " + limit);
            mySQL.updateint("TOWNY_NATIONS","limits","name",limit,town);
        }
        return false;
    }
}


