package dev.loveeev.astracore.command.impl;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;
import dev.loveeev.astracore.Main;
import dev.loveeev.astracore.database.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class setfuel implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        MySQL mySQL = MySQL.getInstance();
        if(strings.length < 2){
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Укажите город.");
            return true;
        }
       Town town = TownyAPI.getInstance().getTown(strings[1]);
        if(strings[0].equalsIgnoreCase("info")) {
            mySQL.selectstring("TOWNY_TOWNS", "name", town.getName(), "fuel", "integer");
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"Нефтяная станция производит &#FFDAA4" + Main.getInstance().getseceltinteger() + " &fнефти");
        }
        if (strings[0].equalsIgnoreCase("set")) {
        if(strings.length < 3) {
            Main.getInstance().getChatUtility().sendSuccessNotification(player, "Установите значение.");
        }else{
            String znah = strings[2];
            mySQL.update("TOWNY_TOWNS", "fuel", "name", znah,town.getName());
            Main.getInstance().getChatUtility().sendSuccessNotification(player,"В городе &#FFDAA4" + town.getName() + " &fтеперь производиться &#FFDAA4" + znah + " &fнефти");
            }
        }
        return false;
    }
}
