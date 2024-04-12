package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.data.TownData;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class bc implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;
        Resident resident = TownyAPI.getInstance().getResident(player);
        TownData data;
        Town town = TownyAPI.getInstance().getTown(player);
        try {
            data = TownData.getOrCreate(player);
        } catch (NotRegisteredException e) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы не состоите в городе.");
            return true;
        }
        try {
            MySQL mySQL = MySQL.getInstance();
            mySQL.selectstring("TOWNY_TOWNS","name",town.getName(),"bc","integer");
            int resulttf = (Integer)Main.getInstance().getseceltinteger();
            if (resulttf == 0) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас не построен телеграф");
                return true;
            } else {
                if (args.length == 0) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /telegraph сообщение");
                } else {
                    StringBuilder message = new StringBuilder(args[0]);
                    for (int i = 1; i < args.length; i++) message.append(" ").append(args[i]);
                    String rank = "Телеграфист";
                    if (resident.isMayor() || resident.hasTownRank("assistant")) {
                        try {
                            Main.getInstance().getChatUtility().sendColoredMessage(player, "&x&f&f&d&a&a&4&lТелеграмма &fиз &x&f&f&d&a&a&4" + resident.getTown().getName() + " &f(Отправитель: " + player.getName() + ") — &7" + message.toString());
                        } catch (NotRegisteredException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (resident.hasTownRank(rank)) {
                        try {
                            for (Player playerf : Bukkit.getOnlinePlayers()) {
                                playerf.sendMessage("&x&f&f&d&a&a&4&lТелеграмма &fиз &x&f&f&d&a&a&4" + resident.getTown().getName() + " &f(Отправитель: " + player.getName() + ") — &7" + message.toString());
                            }
                        } catch (NotRegisteredException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас нет доступа к использованию этой команды.");
                    }
                }
            }
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}