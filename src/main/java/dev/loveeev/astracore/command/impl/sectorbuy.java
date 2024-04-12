package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.command.BaseCommand;
import dev.loveeev.astracore.database.MySQL;
import dev.loveeev.astracore.settings.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class sectorbuy extends BaseCommand {
    public sectorbuy(Main plugin) {
        super(plugin);
    }

    @Override
    public boolean handle(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) throws NotRegisteredException {
        Player player = (Player) sender;
        Double balance = Main.getInstance().getEconomy().getBalance(player);
        MySQL mySQL = MySQL.getInstance();
        Resident resident = TownyAPI.getInstance().getResident(player);
        if(!resident.hasTown()){
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Вы должны быть в городе.");
            return true;
        }
        Town town = TownyAPI.getInstance().getTown(player);
        if(args.length < 1){
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Команда должна выглядеть /t sectorbuy название сектора");
            return true;
        }
        String sector = args[0];
        switch (sector) {
            case "Аграрный-сектор" -> {
                Double price = Settings.PRICEAGRAR;
                if (price > balance) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас недостаточно монет.");
                    return true;
                }
                mySQL.selectstring("TOWNY_TOWNS", "name", town.getName(), "farmer", "integer");
                Object pon = Main.getInstance().getseceltinteger();
                if ((Integer) pon == 0) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы успешно купили аграрный сектор");
                    Main.getInstance().getEconomy().withdrawPlayer(player, price);
                    mySQL.updateint("TOWNY_TOWNS", "farmer", "name", 1, town.getName());
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы уже купили этот сектор.");
                }
            }
            case "Металлургический-сектор" -> {
                Double price = Settings.METALSECTOR;
                if (price > balance) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас недостаточно монеток.");
                    return true;
                }
                mySQL.selectstring("TOWNY_TOWNS", "name", town.getName(), "farmer", "integer");
                Object pon = Main.getInstance().getseceltinteger();
                if ((Integer)pon == 0) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы успешно купили металлургический сектор");
                    Main.getInstance().getEconomy().withdrawPlayer(player, price);
                    mySQL.updateint("TOWNY_TOWNS", "resourcesector", "name", 1, town.getName());
                } else {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы уже купили данный сектор");
                }
            }
            case "Лесной-сектор" -> {
                Double price = Settings.LESSECTOR;
                if (price > balance) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "У вас недостаточно монеток.");
                    return true;
                }
                mySQL.selectstring("TOWNY_TOWNS", "name", town.getName(), "farmer", "integer");
                Object pon2 = Main.getInstance().getseceltinteger();
                if ((Integer)pon2 == 0) {
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы успешно купили лесной сектор");
                    Main.getInstance().getEconomy().withdrawPlayer(player, price);
                    mySQL.updateint("TOWNY_TOWNS", "oaksector", "name", 1, town.getName());
                } else {    
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вы уже купили данный сектор.");
                }
            }
            default ->
                    Main.getInstance().getChatUtility().sendSuccessNotification(player, "Такого сектора не существует.");
        }
        return false;
    }
}
