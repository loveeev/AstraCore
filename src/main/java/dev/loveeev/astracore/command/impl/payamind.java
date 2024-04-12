package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class payamind implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        MySQL mySQL = MySQL.getInstance();
        Player player = (Player) sender;
            if (args.length < 1){
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Используйте /t payadmin число");
                return true;
            }
            try {
                double price = Double.parseDouble(args[0]);
                double balance = Main.getInstance().getEconomy().getBalance(player);
                Resident resident = TownyAPI.getInstance().getResident(player);
                if (price <= balance) {
                    Main.getInstance().getEconomy().withdrawPlayer(player, price);
                    String time = PlaceholderAPI.setPlaceholders(player, "%localtime_timezone_Europe/Moscow,HH:mm:ss dd.MM.YY%");
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Время: &7" + time);
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Ник: &x&f&f&d&a&a&" + resident.getName());
                        mySQL.selectstring("TOWNY_TOWNS","name",String.valueOf(TownyAPI.getInstance().getTown(player).getName()),"payer","double");
                            Object resulttf;
                            resulttf = Main.getInstance().getseceltinteger();
                            double result = (Integer) resulttf + price;
                            mySQL.update("TOWNY_TOWNS","payer","name",result,String.valueOf(TownyAPI.getInstance().getTown(player).getName()));
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас недостаточно средств для совершения перевода");
                }
            } catch (NumberFormatException e) {
                Main.getInstance().getChatUtility().sendSuccessNotification(player, "Введите число.");
            }
        return false;
    }
}
