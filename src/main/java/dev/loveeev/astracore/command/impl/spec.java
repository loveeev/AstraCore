package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class spec implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        MySQL mySQL = MySQL.getInstance();
        Player player = (Player) commandSender;
        Town town = TownyAPI.getInstance().getTown(player);
        Resident resident = TownyAPI.getInstance().getResident(player);
        if(resident.isMayor()) {
            Object money = Main.getInstance().getseceltinteger();
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Вам выдано " + money + " монет. Вы сможете получить еще через 24 часа.");
            Main.getInstance().getEconomy().depositPlayer(player,(Double) money);
        }else {
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Доступно только мэрам.");
        }
        return false;
    }
}
